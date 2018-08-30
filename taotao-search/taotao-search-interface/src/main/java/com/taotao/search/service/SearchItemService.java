package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

public interface SearchItemService {
    public TaotaoResult importAllItems();
    public TaotaoResult addDocument(long itemId);
}
