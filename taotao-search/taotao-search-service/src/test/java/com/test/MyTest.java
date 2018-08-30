package com.test;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class MyTest {
    /*@Test
    public void demo1() throws IOException, SolrServerException {
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.154:8080/solr");
        SolrInputDocument doc=new SolrInputDocument();
        doc.addField("id","001");
        doc.addField("item_title","蜡烛");
        doc.addField("item_price","5");
        solrServer.add(doc);
        solrServer.commit();
    }
    @Test
    public void demo2() throws IOException, SolrServerException {
        SolrServer solrServer=new HttpSolrServer("http://192.168.25.154:8080/solr");
        SolrInputDocument doc=new SolrInputDocument();
        solrServer.deleteById("001");

        solrServer.commit();
    }*/
}
