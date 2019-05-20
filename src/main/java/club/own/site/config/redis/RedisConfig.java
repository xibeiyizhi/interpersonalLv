package club.own.site.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by shenchangxing on 2018/5/8.
 */
@Configuration
public class RedisConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean(destroyMethod = "destroy", name = "redisConnectionFactory")
    public JedisConnectionFactory redisConnectionFactory(
            @Qualifier("redisProperties") RedisProperties redisProperties) {
        return createConnectionFactory(redisProperties);
    }

    @Bean
    public RedisClient redisClient(
            @Qualifier("redisConnectionFactory") JedisConnectionFactory factory) {
        return new RedisClient(factory);
    }


    private JedisConnectionFactory createConnectionFactory(RedisProperties redisProperties) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        config.setMinIdle(redisProperties.getJedis().getPool().getMinIdle());
        config.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
        config.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().toMillis());
        config.setTestOnBorrow(true);
        JedisConnectionFactory factory = new JedisConnectionFactory(config);
        factory.setHostName(redisProperties.getHost());
        factory.setPort(redisProperties.getPort());

        return factory;
    }

}
