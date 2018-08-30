package com.taotao.search.listener;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.dao.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import com.taotao.search.service.impl.SearchItemServiceImpl;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemChangeListener implements MessageListener {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SearchItemService searchItemService;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage)
        {
            TextMessage textMessage = (TextMessage) message;
            try {
                String itemId = textMessage.getText();
                searchItemService.addDocument(Long.valueOf(itemId));



            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
