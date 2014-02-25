package org.abs.consumer.persistance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abs.consumer.entities.IEntity;
import org.abs.consumer.managers.ApplicationManager;
import org.abs.consumer.managers.BaseManager;
import org.abs.consumer.managers.PropertiesManager;
import org.apache.log4j.Logger;
import redis.clients.jedis.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:35 AM
 */
public class EntityDAO  {
	protected static Logger log = Logger.getLogger(EntityDAO.class);

	private static EntityDAO instance = new EntityDAO();
	private ApplicationManager applicationManager;
	private JedisPool pool = null;

	public static EntityDAO getInstance() {
		return instance;
	}

	public EntityDAO() {
		Properties properties = PropertiesManager.getInstance().getProperties();
		String hostname = properties.getProperty("database.host","localhost");
		String password = properties.getProperty("database.password");
		int databaseIndex = PropertiesManager.getInstance().getIntProperty("database.index", 0);
		int poolSize = PropertiesManager.getInstance().getIntProperty("redis.pool.size", 20);
		int port = PropertiesManager.getInstance().getIntProperty("database.port", 6379);
		int timeout = PropertiesManager.getInstance().getIntProperty("database.timeout", 300);

		applicationManager = ApplicationManager.getInstance();
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(poolSize);

		pool = new JedisPool(poolConfig, hostname, port, timeout, password, databaseIndex);
	}

	public <T extends IEntity> Map<String,T> loadEntityMap(Class<T> entityClass){
		String key = applicationManager.getBaseNamespace() + "::" + entityClass.getSimpleName().toLowerCase();
		Jedis jedis = pool.getResource();
		pool.returnResource(jedis);
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
		pool.returnResource(jedis);

		return entityMap;
	}

//	public <T extends IEntity> List<T> loadEntityList(Class<T> entityClass){
//		String key = applicationManager.getBaseNamespace() + "::" + entityClass.getSimpleName().toLowerCase();
//		Jedis jedis = pool.getResource();
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
			jedis.hset(key,entity.getId(), json);
			pool.returnResource(jedis);
		}
	}

	public <T extends IEntity> T loadEntity(String entityId, Class<T> entityClass){
		String key = applicationManager.getBaseNamespace()  + "::" + entityClass.getSimpleName().toLowerCase();
		T entity = null;

		Jedis jedis = pool.getResource();
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
