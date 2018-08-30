package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {
    @Autowired
    private SolrServer solrServer;
    public SearchResult search(SolrQuery query){
        QueryResponse response = null;
        try {
            response = solrServer.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        //取商品列表
        SolrDocumentList solrDocumentList = response.getResults();
        //商品列表
        List<SearchItem> itemList = new ArrayList<>();
        for (SolrDocument solrDocument : solrDocumentList) {
            SearchItem item = new SearchItem();
            item.setId((String) solrDocument.get("id"));
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            item.setImage((String) solrDocument.get("item_image"));
            item.setPrice((long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = "";
            //有高亮显示的内容时。
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            item.setTitle(itemTitle);
            //添加到商品列表
            itemList.add(item);
        }
        SearchResult result = new SearchResult();
        //商品列表
        result.setItemList(itemList);
        //总记录数
        result.setRecordCount(solrDocumentList.getNumFound());

        return result;
    }
}
