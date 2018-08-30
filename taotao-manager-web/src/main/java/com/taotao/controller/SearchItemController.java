package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SearchItemController {
    @Autowired
    private SearchItemService searchItemService;
    @RequestMapping("/index/importall")
    public TaotaoResult importAllItems()
    {
        try {
            TaotaoResult result = searchItemService.importAllItems();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,"导入失败");
        }
    }
}
