package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
    TaotaoResult checkData(String param,int type);

    /**
     * 创建用户
     * @param tbUser
     * @return
     */
    TaotaoResult createUser(TbUser tbUser);

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    public TaotaoResult login(String username, String password);

    /**
     * 根据token查询用户
     * @param token
     * @return
     */
    public TaotaoResult getUserByToken(String token);
    public TaotaoResult logoutByToken(String token);
}
