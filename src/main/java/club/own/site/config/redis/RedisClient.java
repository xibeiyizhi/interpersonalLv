package club.own.site.config.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BookThief on 2016/6/6.
 */
public class RedisClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    private JedisConnectionFactory jedisConnectionFactory;

    public RedisClient(JedisConnectionFactory factory) {
        this.jedisConnectionFactory = factory;
        LOGGER.info("RedisClient initial...");
    }

    /**
     * Strings 相关接口
     */
    public String get(String key) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().get(key);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public String set(String key, Object value) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            String valueJson;
            if (value instanceof String) {
                valueJson = (String) value;
            } else {
                valueJson = JSONObject.toJSONString(value);
            }
            return jedisConnection.getNativeConnection().set(key, valueJson);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public Long del(String key) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().del(key);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public boolean exists(String key) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().exists(key);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public Long expire(String key, int seconds) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().expire(key, seconds);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public String setEx(String key, int seconds, Object value) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            String valueJson;
            if (value instanceof String) {
                valueJson = (String) value;
            } else {
                valueJson = JSON.toJSONString(value);
            }
            return jedisConnection.getNativeConnection().setex(key, seconds, valueJson);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public Long setnx(String key, String value) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().setnx(key, value);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public String getSet(String key, String value) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().getSet(key, value);
        } finally {
            returnResource(jedisConnection);
        }
    }


    /**
     * Hash 相关接口
     */
    public Long hset(String key, String field, Object value) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            String valueJson;
            if (value instanceof String) {
                valueJson = (String) value;
            } else {
                valueJson = JSON.toJSONString(value);
            }
            return jedisConnection.getNativeConnection().hset(key, field, valueJson);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public Long hdel(String key, String... fields) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().hdel(key, fields);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public String hget(String key, String field) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().hget(key, field);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public boolean hExists(String key, String field) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().hexists(key, field);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public Map<String, String> hgetAll(String key) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().hgetAll(key);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public <T> Map<String, T> hgetAll(String key, Class<T> tClass) {
        Map<String, T> ret = new HashMap<>();
        Map<String, String> all = hgetAll(key);
        for ( Map.Entry<String, String> entry : all.entrySet() ) {
            ret.put(entry.getKey(), JSON.parseObject(entry.getValue(), tClass));
        }
        return ret;
    }

    public List<String> hgetAllValue(String key) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().hvals(key);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public <T> List<T> hgetAllValue(String key, Class<T> tClass) {
        List<T> ret = new ArrayList<>();
        List<String> values = hgetAllValue(key);
        for (String v : values) {
            ret.add(JSON.parseObject(v, tClass));
        }
        return ret;
    }

    /**
     * List 相关接口
     */
    public Long llen(String key) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().llen(key);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public String lpop(String key) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().lpop(key);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public Long lrem(String key, String value) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().lrem(key, 0L, value);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public List<String> lrange(String key, Long start, Long stop) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().lrange(key, start, stop);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public Long rpush(String key, String... objs) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().rpush(key,objs);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public String rpop(String key) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().rpop(key);
        } finally {
            returnResource(jedisConnection);
        }
    }

    public Long incr(String key) {
        checkKey(key);
        JedisConnection jedisConnection = null;
        try {
            jedisConnection = getJedisConnection();
            return jedisConnection.getNativeConnection().incr(key);
        } finally {
            returnResource(jedisConnection);
        }
    }


    private JedisConnection getJedisConnection() {
        return (JedisConnection) jedisConnectionFactory.getConnection();
    }

    private void checkKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Cache key is null or not a length of 0.");
        }
    }

    private void returnResource(JedisConnection jedisConnection) {
        if (jedisConnection != null) {
            jedisConnection.close();
        }
    }

}
