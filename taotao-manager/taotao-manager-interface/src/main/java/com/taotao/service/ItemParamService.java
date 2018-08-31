package com.taotao.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;

public interface ItemParamService {
    public TaotaoResult getItemParamByCid(long itemCatId);

    /**
     * 保存模板json数据到库中
     * @param tbItemParam
     * @return
     */
    public TaotaoResult addItemParam(TbItemParam tbItemParam);
}
