package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {
	public TbItem getItemById(long itemId);
	public EasyUIDataGridResult getItem(int page, int rows);
	public TaotaoResult addItem(TbItem item, String itemDesc);
	public TaotaoResult deleteItem(List<Long> idList);
	public TaotaoResult instockItem(List<Long> idList);
	public TaotaoResult reshelfItem(List<Long> idList);
}
