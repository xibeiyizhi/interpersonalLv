package club.own.site.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by shenchangxing on 2018/5/8.
 */
@Configuration
public class RedisConfig {

    @Bean
    @ConfigurationProperties(prefix = "redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean(destroyMethod = "destroy")
    public JedisConnectionFactory redisConnectionFactory(
            @Qualifier("redisProperties") RedisProperties redisProperties) {
        return createConnectionFactory(redisProperties);
    }

    @Bean
    public RedisClient redisClient(@Qualifier("redisConnectionFactory") JedisConnectionFactory factory) {
        return new RedisClient(factory);
    }


    private JedisConnectionFactory createConnectionFactory(RedisProperties redisProperties) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getPool().getMaxIdle());
        config.setMinIdle(redisProperties.getPool().getMinIdle());
        config.setMaxTotal(redisProperties.getPool().getMaxActive());
        config.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
        config.setTestOnBorrow(true);
        JedisConnectionFactory factory = new JedisConnectionFactory(config);
        factory.setHostName(redisProperties.getHost());
        factory.setPort(redisProperties.getPort());
        factory.setPassword(redisProperties.getPassword());
        if( redisProperties.getDb() >= 0 ) {  //db=-1配置默认为Codis集群
            factory.setDatabase(redisProperties.getDb());
        }
        return factory;
    }

}
