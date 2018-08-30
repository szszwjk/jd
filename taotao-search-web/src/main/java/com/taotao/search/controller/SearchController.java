package com.taotao.search.controller;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(@RequestParam("q")String queryString,
                         @RequestParam(defaultValue="1")Integer page, Model model) throws Exception {
        queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
        SearchResult result = searchService.search(queryString, page, 60);
        //传递给页面
        model.addAttribute("query", queryString);
        model.addAttribute("totalPages", result.getPageCount());
        model.addAttribute("itemList", result.getItemList());
        model.addAttribute("page", page);

        //返回逻辑视图
        return "search";

    }
}
