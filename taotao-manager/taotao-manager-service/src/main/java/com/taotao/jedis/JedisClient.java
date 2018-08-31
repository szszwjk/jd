package com.taotao.jedis;

public interface JedisClient {
	/*
	* 设置key value
	* */
	String set(String key, String value);
	/*
	* 根据key得到value
	* */
	String get(String key);
	/*
	* key 是否存在
	* */
	Boolean exists(String key);
	/*
	* 设置key的生存时间
	* 已毫秒为单位
	* */
	Long expire(String key, int seconds);
	/*
	* 查看key的生存时间
	* */
	Long ttl(String key);
	/*
	*
	* */
	Long incr(String key);
	/*
	* 设置散列
	*
	* */
	Long hset(String key, String field, String value);
	/*
	* 得到散列
	* */
	String hget(String key, String field);
	Long hdel(String key, String... field);
}
