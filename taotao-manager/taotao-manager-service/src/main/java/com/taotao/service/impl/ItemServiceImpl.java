package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import com.taotao.utils.IDUtils;
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Override
	public TbItem getItemById(long itemId) {
		TbItem tbItem = tbItemMapper.getItemById(itemId);
		return tbItem;
	}
	@Override
	public EasyUIDataGridResult getItem(int page, int rows) {
		//设置分页信息
				PageHelper.startPage(page, rows);
				//执行查询
				
				List<TbItem> list = tbItemMapper.getTbItemPaging();
				//取分页信息
				PageInfo<TbItem> pageInfo = new PageInfo<>(list);
				
				//创建返回结果对象
				EasyUIDataGridResult result = new EasyUIDataGridResult();
				
				result.setTotal(pageInfo.getTotal());
				result.setRows(list);
				
				return result;
	}
	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		long itemId = IDUtils.genItemId();
		// 2、补全TbItem对象的属性
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		// 3、向商品表插入数据
		tbItemMapper.addTbItem(item);
		// 4、创建一个TbItemDesc对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 5、补全TbItemDesc的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		// 6、向商品描述表插入数据
		tbItemDescMapper.addItemDesc(itemDesc);
		// 7、TaotaoResult.ok()
		return TaotaoResult.ok();
		
		
		
	}
	@Override
	public TaotaoResult deleteItem(List<Long> idList) {
		tbItemMapper.deleteItem(idList);
		return TaotaoResult.ok();
	}
	@Override
	public TaotaoResult instockItem(List<Long> idList) {
		tbItemMapper.instock(idList);
		return TaotaoResult.ok();
	}
	@Override
	public TaotaoResult reshelfItem(List<Long> idList) {
		tbItemMapper.reshelf(idList);
		return TaotaoResult.ok();
	}

}