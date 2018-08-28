package com.taotao.mapper;

import java.util.List;


import com.taotao.pojo.TbContentCategory;


public interface TbContentCategoryMapper {
	List<TbContentCategory> getTbContentCategoryByParentId(long parentId);
	void insertTbContentCategory(TbContentCategory tbContentCategory);
	TbContentCategory selectByPrimaryKey(long parentId);
	void updateByPrimaryKey(TbContentCategory parentNode);
	
}