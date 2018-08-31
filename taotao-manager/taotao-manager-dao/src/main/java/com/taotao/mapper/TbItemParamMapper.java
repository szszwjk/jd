package com.taotao.mapper;


import com.taotao.pojo.TbItemParam;

public interface TbItemParamMapper {
    /*
* 根据分类id查询数据库中的规格参数模板
*
* */
    TbItemParam getItemParamByCid(long cId);
    void addItemParam(TbItemParam tbItemParam);
}