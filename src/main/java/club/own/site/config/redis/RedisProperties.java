package club.own.site.config.redis;

import lombok.Data;

/**
 * Created by guopeng on 2017/5/18.
 */
@Data
public class RedisProperties {
    private String host;
    private Integer port;
    private Integer db;
    private RedisPool pool;
    private String password;
}
