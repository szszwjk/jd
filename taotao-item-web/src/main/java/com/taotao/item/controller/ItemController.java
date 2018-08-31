package com.taotao.item.controller;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;
    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable long itemId,Model model)
    {
        TbItem tbItem = itemService.getItemById(itemId);
        Item item=new Item(tbItem);
        //TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        //System.out.println(itemDesc);
        model.addAttribute(item);
        //model.addAttribute(itemDesc);
        return "item";

    }
    @RequestMapping("/item/desc/{itemId}")
    @ResponseBody
    public String showDesc(@PathVariable long itemId)
    {

        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        System.out.println(itemDesc);
        return itemDesc.getItemDesc();


    }
}
