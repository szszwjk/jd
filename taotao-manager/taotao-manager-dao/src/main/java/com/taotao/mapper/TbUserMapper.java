package com.taotao.mapper;


import com.taotao.pojo.TbUser;

public interface TbUserMapper {
    TbUser getUserByUserName(String userName);
    TbUser getUserByPhone(String phone);
    TbUser getUserByEmail(String email);
    void insertUser(TbUser tbUser);
    TbUser getUserByUserAndPass(String userName,String passWord);
}