package club.own.site.config.redis;

import lombok.Data;

/**
 * Created by guopeng on 2017/5/18.
 */
@Data
public class RedisPool {
    private Integer maxActive;
    private Integer maxWait;
    private Integer maxIdle;
    private Integer minIdle;
}
