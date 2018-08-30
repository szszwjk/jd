package com.taotao.search.dao;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;

import java.util.List;

public interface SearchItemMapper {
    List<SearchItem> getItemList();
    public SearchItem getItemById(long itemId);
}
