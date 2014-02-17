package org.abs.consumer.managers;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:35 AM
 */
public class StorageManager extends BaseManager {
	private static StorageManager instance = new StorageManager();
	private static ApplicationManager applicationManager;
	JedisPool pool = null;

	public static StorageManager getInstance() {
		return instance;
	}

	public StorageManager() {
		applicationManager = ApplicationManager.getInstance();
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//TODO: Read this from a prop file.
		poolConfig.setMaxActive(5);
		pool = new JedisPool(poolConfig, "localhost");
	}

	public Map<String,String> loadMap(String id){
		String key = applicationManager.getBaseNamespace()  + "::" + applicationManager.getApplicationName() + "::" + id;
		Jedis jedis = pool.getResource();
		return jedis.hgetAll(key);
	}

	public List<String> loadList(String id){
		String key = applicationManager.getBaseNamespace()  + "::" + applicationManager.getApplicationName() + "::" + id;
		Jedis jedis = pool.getResource();
		return jedis.lrange(key, 1,-1);
	}

	public void saveMap(String id, Map<String,String> data){
		String key = applicationManager.getBaseNamespace()  + "::" + applicationManager.getApplicationName() + "::" + id;
		log.debug("Saving to: " + key);
		Jedis jedis = pool.getResource();
		Pipeline pipeline = jedis.pipelined();
		for (String dataKey : data.keySet()){
			pipeline.hset(key,dataKey,data.get(dataKey));
		}
		pipeline.sync();
	}

	public void saveList(String id, List<String> data){
		String key = applicationManager.getBaseNamespace()  + "::" + applicationManager.getApplicationName() + "::" + id;
		Jedis jedis = pool.getResource();
		Pipeline pipeline = jedis.pipelined();
		for (String value : data){
			pipeline.lpush(key, value);
		}
		pipeline.sync();
	}

	@Override
	protected void finalize() throws Throwable {
	}
}
