package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUIDataGridResult;


import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;

import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbContentMapper tbContentMapper;

    @Override
    public EasyUIDataGridResult findContentAll(long categoryId) {

        List<TbContent> contents = tbContentMapper.findContentAll(categoryId);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        //这里就是总记录条数
        result.setTotal(contents.size());
        //加入结果集
        result.setRows(contents);

        return result;
    }

    @Override
    public TaotaoResult addContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);

        tbContentMapper.addContent(tbContent);
        //数据变更 清除缓存
        jedisClient.hdel(CONTENT_KEY,tbContent.getCategoryId()+"");
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContent(long categoryId) {
        //这里取缓存
        try {
            String json=jedisClient.hget(CONTENT_KEY, categoryId+"");
            if (StringUtils.isNotBlank(json)){
                List<TbContent> result = JsonUtils.jsonToList(json, TbContent.class);
                System.out.println("查询缓存");
                return result;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        * 第一次请求 由于没有缓存 所以直接查询 查询之后在return之前加入redis
        * 第二次访问 直接查询缓存
        * */
        List<TbContent> contentAll = tbContentMapper.findContentAll(categoryId);
        System.out.println("查询数据库");
        try {
            jedisClient.hset(CONTENT_KEY,categoryId+"", JsonUtils.objectToJson(contentAll));
            System.out.println("加入缓存");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //这里加缓存
        return contentAll;
    }
}
