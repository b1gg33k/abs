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
import java.util.ArrayList;
import java.util.HashMap;
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
	private ApplicationManager applicationManager;
	private JedisPool pool = null;
	private int databaseIndex = 0;

	public static StorageManager getInstance() {
		return instance;
	}

	public StorageManager() {
		super();
		int poolSize = 5;
		if (null != properties){
			String dbIndex = properties.getProperty("database.index","0");
			if (null != dbIndex){
				databaseIndex = Integer.parseInt(dbIndex);
			}
			poolSize = Integer.parseInt(properties.getProperty("redis.pool.size", "5"));
		}
		applicationManager = ApplicationManager.getInstance();
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(poolSize);
		pool = new JedisPool(poolConfig, "localhost");
	}	
	public <T extends IEntity> Map<String,T> loadEntityMap(Class<T> entityClass){
		String key = applicationManager.getBaseNamespace() + "::" + entityClass.getSimpleName().toLowerCase();
		Jedis jedis = pool.getResource();
		jedis.select(databaseIndex);
		Map<String, String> redisMap = jedis.hgetAll(key);
		Map<String, T> entityMap = null;

		if (null != redisMap){
			entityMap = new HashMap<String, T>();
			for (String id : redisMap.keySet()){
				String json = redisMap.get(id);
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				try {
					T entity = mapper.readValue(json,entityClass);
					entityMap.put(id,entity);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return entityMap;
	}

//	public <T extends IEntity> List<T> loadEntityList(Class<T> entityClass){
//		String key = applicationManager.getBaseNamespace() + "::" + entityClass.getSimpleName().toLowerCase();
//		Jedis jedis = pool.getResource();
//		jedis.select(databaseIndex);
//		List<String> redisList = jedis.lrange(key, 0,-1);
//		pool.returnResource(jedis);
//		List<T> entityList = null;
//		if (null != redisList){
//			entityList = new ArrayList<T>();
//			for (String json : redisList){
//				ObjectMapper mapper = new ObjectMapper();
//				try {
//					T entity = mapper.readValue(json,entityClass);
//					if (null != entity) entityList.add(entity);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		return entityList;
//	}

	public <T extends IEntity> void saveEntityMap(Map<String, T> data){
		if (null == data || data.size()==0) return;
		Jedis jedis = pool.getResource();
		jedis.select(databaseIndex);
		Pipeline pipeline = jedis.pipelined();
		for (String dataKey : data.keySet()){
			IEntity entity = data.get(dataKey);

			String key = applicationManager.getBaseNamespace() + "::" + entity.getClass().getSimpleName().toLowerCase();
			try {
				pipeline.hset(key,dataKey,entity.toJson());
			} catch (JsonProcessingException e) {
				log.error(e.getLocalizedMessage(), e);
			}
		}
		pipeline.sync();
		pool.returnResource(jedis);
	}

//	public <T extends IEntity> void saveEntityList(List<T> data){
//		if (null == data || data.size()==0){
//			return;
//		}
//		Jedis jedis = pool.getResource();
//		jedis.select(databaseIndex);
//		Pipeline pipeline = jedis.pipelined();
//		for (IEntity value : data){
//			String key = applicationManager.getBaseNamespace()  + "::"  + value.getClass().getSimpleName().toLowerCase();
//			try {
//				pipeline.lpush(key, value.toJson());
//			} catch (JsonProcessingException e) {
//				log.error(e.getLocalizedMessage(),e);
//			}
//		}
//		pipeline.sync();
//		pool.returnResource(jedis);
//	}

	public void saveEntity(IEntity entity){
		saveEntity(entity, false);
	}


	public void saveEntity(IEntity entity, boolean asynchronous){
		//TODO: Make this asynchronous
		String key = applicationManager.getBaseNamespace()  + "::" + entity.getClass().getSimpleName().toLowerCase();
		String json = null;
		try {
			json = entity.toJson();
		} catch (JsonProcessingException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		if (null != json){
			Jedis jedis = pool.getResource();
			jedis.select(databaseIndex);
			jedis.hset(key,entity.getId(), json);
			pool.returnResource(jedis);
		}
	}

	public <T extends IEntity> T loadEntity(String entityId, Class<T> entityClass){
		String key = applicationManager.getBaseNamespace()  + "::" + entityClass.getSimpleName().toLowerCase();
		T entity = null;

		Jedis jedis = pool.getResource();
		jedis.select(databaseIndex);
		String json = jedis.hget(key,entityId);
		pool.returnResource(jedis);
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
		pool.destroy();
		pool = null;
	}

	public JedisPool getPool() {
		return pool;
	}
}
