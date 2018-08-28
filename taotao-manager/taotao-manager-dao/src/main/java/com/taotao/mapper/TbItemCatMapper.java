package com.taotao.mapper;

import java.util.List;

import com.taotao.pojo.TbItemCat;

public interface TbItemCatMapper {
	/**
	 * 
	 * @param parentId
	 * @return
	 */
	List<TbItemCat> geTbItemCatsByPatentId(long parentId);
}