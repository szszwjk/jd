package com.taotao.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(value="id", defaultValue="0") Long parentId)
	{
		List<EasyUITreeNode> list=contentCategoryService.getContentCategoryList(parentId);
		
		return list;
		
	}
	@RequestMapping("/create")
	@ResponseBody
	public TaotaoResult creatCategory(Long parentId,String name)
	{
		TaotaoResult result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
}
