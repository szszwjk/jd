package com.taotao.mapper;

import com.taotao.pojo.TbItemDesc;

public interface TbItemDescMapper {
	public void addItemDesc(TbItemDesc tbItemDesc);
	public TbItemDesc getItemDescById(long itemId);

}