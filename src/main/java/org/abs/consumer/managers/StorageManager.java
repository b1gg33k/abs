package org.abs.consumer.managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abs.consumer.entities.IEntity;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
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
		String key = applicationManager.getBaseNamespace() + id;
		Jedis jedis = pool.getResource();
		return jedis.hgetAll(key);
	}

	public List<String> loadList(String id){
		String key = applicationManager.getBaseNamespace() + id;
		Jedis jedis = pool.getResource();
		return jedis.lrange(key, 1,-1);
	}

	public void saveMap(String id, Map<String,String> data){
		String key = applicationManager.getBaseNamespace() + id;
		log.debug("Saving to: " + key);
		Jedis jedis = pool.getResource();
		Pipeline pipeline = jedis.pipelined();
		for (String dataKey : data.keySet()){
			pipeline.hset(key,dataKey,data.get(dataKey));
		}
		pipeline.sync();
	}

	public void saveList(String id, List<String> data){
		String key = applicationManager.getBaseNamespace()  + "::"  + id;
		Jedis jedis = pool.getResource();
		Pipeline pipeline = jedis.pipelined();
		for (String value : data){
			pipeline.lpush(key, value);
		}
		pipeline.sync();
	}

	public void saveEntity(IEntity entity){
		saveEntity(entity, false);
	}


	public void saveEntity(IEntity entity, boolean asynchronous){
		//TODO: Make this asynchronous
		String key = applicationManager.getBaseNamespace()  + "::" + entity.getClass().getSimpleName().toLowerCase();
		Jedis jedis = pool.getResource();
		String json = null;
		try {
			json = entity.toJson();
		} catch (JsonProcessingException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		if (null != json){
			jedis.hset(key,entity.getId(), json);
		}
	}

	public <T extends IEntity> T loadEntity(String entityId, Class<T> entityClass){
		String key = applicationManager.getBaseNamespace()  + "::" + entityClass.getSimpleName().toLowerCase();
		T entity = null;

		Jedis jedis = pool.getResource();
		String json = jedis.hget(key,entityId);
		if (null != json){
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			try {
				entity = mapper.readValue(json,entityClass);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return entity;
	}

	@Override
	protected void finalize() throws Throwable {
	}
}
