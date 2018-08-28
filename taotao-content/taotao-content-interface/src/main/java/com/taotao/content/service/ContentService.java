package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    public EasyUIDataGridResult findContentAll(long categoryId);
    public TaotaoResult addContent(TbContent tbContent);
    List<TbContent> getContent(long categoryId);
}
