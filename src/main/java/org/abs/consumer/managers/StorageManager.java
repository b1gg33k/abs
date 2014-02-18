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
	private static ApplicationManager applicationManager;
	private JedisPool pool = null;

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

	public Map<String,IEntity> loadEntityMap(Class<? extends IEntity> entityClass){
		String key = applicationManager.getBaseNamespace() + "::" + entityClass.getSimpleName().toLowerCase();
		Jedis jedis = pool.getResource();
		Map<String, String> redisMap = jedis.hgetAll(key);
		Map<String, IEntity> entityMap = null;

		if (null != redisMap){
			entityMap = new HashMap<String, IEntity>();
			for (String id : redisMap.keySet()){
				String json = redisMap.get(id);
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				try {
					IEntity entity = mapper.readValue(json,entityClass);
					entityMap.put(id,entity);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return entityMap;
	}

	public List<IEntity> loadEntityList(Class<? extends IEntity> entityClass){
		String key = applicationManager.getBaseNamespace() + "::" + entityClass.getSimpleName().toLowerCase();
		Jedis jedis = pool.getResource();
		List<String> redisList = jedis.lrange(key, 0,-1);
		List<IEntity> entityList = null;
		if (null != redisList){
			entityList = new ArrayList<IEntity>();
			for (String json : redisList){
				ObjectMapper mapper = new ObjectMapper();
				try {
					IEntity entity = mapper.readValue(json,entityClass);
					if (null != entity) entityList.add(entity);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return entityList;
	}

	public void saveMap(Class entityClass, Map<String,? extends IEntity> data){
		String key = applicationManager.getBaseNamespace() + "::" + entityClass.getSimpleName().toLowerCase();
		log.debug("Saving to: " + key);
		Jedis jedis = pool.getResource();
		Pipeline pipeline = jedis.pipelined();
		for (String dataKey : data.keySet()){
			IEntity entity = data.get(dataKey);

			try {
				pipeline.hset(key,dataKey,entity.toJson());
			} catch (JsonProcessingException e) {
				log.error(e.getLocalizedMessage(), e);
			}
		}
		pipeline.sync();
	}

	public void saveEntityList(Class entityClass, List<? extends IEntity> data){
		String key = applicationManager.getBaseNamespace()  + "::"  + entityClass.getSimpleName().toLowerCase();
		Jedis jedis = pool.getResource();
		Pipeline pipeline = jedis.pipelined();
		for (IEntity value : data){
			try {
				pipeline.lpush(key, value.toJson());
			} catch (JsonProcessingException e) {
				log.error(e.getLocalizedMessage(),e);
			}
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

	public JedisPool getPool() {
		return pool;
	}
}
