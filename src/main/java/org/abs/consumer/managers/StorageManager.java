package org.abs.consumer.managers;

import org.redisson.Redisson;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dale
 * Date: 2/13/14
 * Time: 2:35 AM
 */
public class StorageManager {
	private static StorageManager instance = new StorageManager();
	Redisson redisson = null;

	public static StorageManager getInstance() {
		return instance;
	}

	public StorageManager() {
		redisson = Redisson.create();
	}

	public Map<?,?> loadMap(String id){
		return redisson.getMap(id);
	}

	public List<?> loadList(String id){
		return redisson.getList(id);
	}

	public saveMap(String id, Map<?,?> data){
		redisson.;
	}

	@Override
	protected void finalize() throws Throwable {
		if (null != redisson){
			redisson.shutdown();
		}
		super.finalize();
	}
}
