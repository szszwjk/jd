package com.taotao.mapper;


import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface TbContentMapper {
    public List<TbContent> findContentAll(long categoryId);
    void addContent(TbContent tbContent);
}