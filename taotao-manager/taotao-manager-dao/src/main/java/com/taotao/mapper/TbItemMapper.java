package com.taotao.mapper;

import java.util.List;


import com.taotao.pojo.TbItem;

public interface TbItemMapper {
	TbItem getItemById(long itemId);;
	List<TbItem> getTbItemPaging();
	void addTbItem(TbItem item);
	//http://localhost:8081/rest/page/item-edit?_=1535078512829 404 (Not Found)
	//void editItem(TbItem tbItem);
	void deleteItem(List<Long> idList);
	void instock(List<Long> idList);
	void reshelf(List<Long> idList);
}