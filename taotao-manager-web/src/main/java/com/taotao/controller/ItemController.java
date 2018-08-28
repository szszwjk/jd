package com.taotao.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		//根据商品id查询商品信息
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}
	
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemPaging(Integer page,Integer rows)
	{
		EasyUIDataGridResult result = itemService.getItem(page, rows);
		return result;
		
	}
	
	@RequestMapping("/item/save")
	@ResponseBody
	public TaotaoResult saveItem(TbItem tbItem,String desc)
	{
		TaotaoResult result = itemService.addItem(tbItem, desc);
		return result;
		
	}
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public TaotaoResult deleteItem(String ids)
	{
		List<Long> idList=new ArrayList<>();
		String[] split = ids.split(",");
		for (String string : split) {
			long parseLong = Long.parseLong(string);
			idList.add(parseLong);
		}
		TaotaoResult result = itemService.deleteItem(idList);
		return result;
		
	}
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public TaotaoResult instockItem(String ids)
	{
		List<Long> idList=new ArrayList<>();
		String[] split = ids.split(",");
		for (String string : split) {
			long parseLong = Long.parseLong(string);
			idList.add(parseLong);
		}
		TaotaoResult result = itemService.instockItem(idList);
		return result;
		
	}
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public TaotaoResult reshelfItem(String ids)
	{
		List<Long> idList=new ArrayList<>();
		String[] split = ids.split(",");
		for (String string : split) {
			long parseLong = Long.parseLong(string);
			idList.add(parseLong);
		}
		TaotaoResult result = itemService.reshelfItem(idList);
		return result;
		
	}
	
	

	
}
