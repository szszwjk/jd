package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${COOKIE_TOKEN_KEY}")
    private String COOKIE_TOKEN_KEY;
    @Value("${SESSION_EXPIRE}")
    private int SESSION_EXPIRE;

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkData(@PathVariable String param, @PathVariable Integer type) {
        TaotaoResult taotaoResult = userService.checkData(param, type);
        return taotaoResult;
    }
    @RequestMapping(value="/user/register", method= RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser user) {
        TaotaoResult result = userService.createUser(user);
        return result;
    }
    @RequestMapping(value="/user/login", method=RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String userName, String passWord,
                              HttpServletRequest request, HttpServletResponse response) {
        // 1、接收两个参数。
        // 2、调用Service进行登录。
        TaotaoResult result = userService.login(userName, passWord);
        if(result.getData()==null)
        {
            return result;
        }

        // 3、从返回结果中取token，写入cookie。Cookie要跨域。

        String token = result.getData().toString();
        CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token,SESSION_EXPIRE);
        // 4、响应数据。Json数据。TaotaoResult，其中包含Token。
        return result;

    }
    @RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback) {
        TaotaoResult result = userService.getUserByToken(token);
        if(StringUtils.isNotBlank(callback))
        {
            String strResult = callback + "(" + JsonUtils.objectToJson(result) + ")";
            return strResult;
        }
        return JsonUtils.objectToJson(result);
    }
    @RequestMapping("/user/logout/{token}")

    public String logoutByToken(@PathVariable String token,HttpServletRequest request,HttpServletResponse response) {
        TaotaoResult result = userService.logoutByToken(token);
        CookieUtils.deleteCookie(request,response,COOKIE_TOKEN_KEY);

        return "login";
    }
}
