package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.utils.JsonUtils;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Service
public class ItemServiceImpl implements ItemService {

	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${BASE}")
	private String BASE;
	@Value("${DESC}")
	private String DESC;
	@Value("${PARAM}")
	private String PARAM;
	@Value("${TIME}")
	private int TIME;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private ActiveMQTopic activeMQTopic;
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;
	@Override
	public TbItem getItemById(long itemId) {
		//从缓存中读取数据
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId + BASE);
			if (StringUtils.isNotBlank(json))
            {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbItem tbItem = tbItemMapper.getItemById(itemId);
		//把数据加入缓存
		try {
			jedisClient.set(ITEM_INFO+":"+itemId+BASE, JsonUtils.objectToJson(tbItem));
			jedisClient.expire(ITEM_INFO+":"+itemId+BASE,TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
	public TaotaoResult addItem(TbItem item, String desc,String paramData) {
		final long itemId = IDUtils.genItemId();
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

		//存入规格参数
		TbItemParamItem tbItemParamItem =new TbItemParamItem();
		tbItemParamItem.setItemId(itemId);
		tbItemParamItem.setParamData(paramData);
		tbItemParamItem.setCreated(date);
		tbItemParamItem.setUpdated(date);
		tbItemParamItemMapper.insertTbItemParamItem(tbItemParamItem);
		/*
		* 在这里发布消息 更新缓存
		* 1 用点对点还是 订阅预发布
		* 2 发送过去的数据是是什么类型 格式是什么
		* 只发送id 发送json
		*
		* */
		jmsTemplate.send(activeMQTopic, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		return TaotaoResult.ok ();
		
		
		
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

	@Override
	public TbItemDesc getItemDescById(long itemId) {

		//从缓存中读取数据
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId + DESC);
			if (StringUtils.isNotBlank(json))
			{
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbItemDesc itemDesc = tbItemDescMapper.getItemDescById(itemId);

		//把数据加入缓存
		try {
			jedisClient.set(ITEM_INFO+":"+itemId+DESC, JsonUtils.objectToJson(itemDesc));
			jedisClient.expire(ITEM_INFO+":"+itemId+DESC,TIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
