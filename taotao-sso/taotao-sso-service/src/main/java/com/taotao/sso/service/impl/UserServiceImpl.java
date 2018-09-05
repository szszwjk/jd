package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserService;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private int SESSION_EXPIRE;
    @Value("${USER_INFO}")
    private String USER_INFO;
    @Override
    public TaotaoResult checkData(String param, int type) {
        if(type==1){
            TbUser tbUser = tbUserMapper.getUserByUserName(param);
            if(tbUser!=null){
                return TaotaoResult.ok(false);
            }
        }else if (type==2){
            TbUser tbUser = tbUserMapper.getUserByPhone(param);
            if(tbUser!=null){
                return TaotaoResult.ok(false);
            }
        }else if (type==3){
            TbUser tbUser = tbUserMapper.getUserByEmail(param);
            if(tbUser!=null){
                return TaotaoResult.ok(false);
            }
        }else{
            return TaotaoResult.build(500,"参数错误","参数错误");
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult createUser(TbUser tbUser) {
        if (StringUtils.isBlank(tbUser.getUserName())) {
            return TaotaoResult.build(400, "用户名不能为空");
        }
        if (StringUtils.isBlank(tbUser.getPassWord())) {
            return TaotaoResult.build(400, "密码不能为空");
        }
        //校验数据是否可用
        TaotaoResult result = checkData(tbUser.getUserName(), 1);
        if (!(boolean) result.getData()) {
            return TaotaoResult.build(400, "此用户名已经被使用");
        }
        //校验电话是否可以
        if (StringUtils.isNotBlank(tbUser.getPhone())) {
            result = checkData(tbUser.getPhone(), 2);
            if (!(boolean) result.getData()) {
                return TaotaoResult.build(400, "此手机号已经被使用");
            }
        }
        //校验email是否可用
        if (StringUtils.isNotBlank(tbUser.getEmail())) {
            result = checkData(tbUser.getEmail(), 3);
            if (!(boolean) result.getData()) {
                return TaotaoResult.build(400, "此邮件地址已经被使用");
            }
        }
        // 2、补全TbUser其他属性。
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        // 3、密码要进行MD5加密。
        String md5Pass = DigestUtils.md5DigestAsHex(tbUser.getPassWord().getBytes());
        tbUser.setPassWord(md5Pass);
        // 4、把用户信息插入到数据库中。
        tbUserMapper.insertUser(tbUser);
        // 5、返回TaotaoResult。
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult login(String username, String password) {
        if (StringUtils.isBlank(username)) {
            return TaotaoResult.build(400, "用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return TaotaoResult.build(400, "密码不能为空");
        }
        TbUser tbUser = tbUserMapper.getUserByUserAndPass(username, DigestUtils.md5DigestAsHex(password.getBytes()));
        if(tbUser==null)
        {
            return TaotaoResult.build(400,"用户名或密码错误");
        }
        //asd-adsa-sdfs-sfsf
        String uuid = UUID.randomUUID().toString();
        //去除横杠
        String token = uuid.replace("-", "");
        tbUser.setPassWord(null);
        jedisClient.set(USER_INFO + ":" + token, JsonUtils.objectToJson(tbUser));
        // 5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
        jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        // 6、返回TaotaoResult包装token。
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        // 2、根据token查询redis。
        String json = jedisClient.get(USER_INFO + ":" + token);
        if (StringUtils.isBlank(json)) {
            // 3、如果查询不到数据。返回用户已经过期。
            return TaotaoResult.build(400, "用户登录已经过期，请重新登录。");
        }
        // 4、如果查询到数据，说明用户已经登录。
        // 5、需要重置key的过期时间。
        jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        // 6、把json数据转换成TbUser对象，然后使用TaotaoResult包装并返回。
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return TaotaoResult.ok(user);
    }

    @Override
    public TaotaoResult logoutByToken(String token) {
        Long result = jedisClient.del(USER_INFO + ":" + token);
        System.out.println(result);
        return TaotaoResult.ok();
    }
}
