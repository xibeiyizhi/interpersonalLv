package club.own.site.controller;

import club.own.site.bean.ProdItem;
import club.own.site.config.redis.RedisClient;
import club.own.site.enums.ProductionTypeEnum;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static club.own.site.constant.ProjectConstant.PROD_CATE_LIST_KEY;

@Slf4j
@Controller
public class BaseController {

    @Autowired
    private RedisClient redisClient;

    Map<String, String> parseRequestParam(Map<String, String[]> parameterMap) {
        Map<String, String> res = Maps.newHashMap();
        if(MapUtils.isNotEmpty(parameterMap)) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                for (String value : values) {
                    if (StringUtils.isNotBlank(value)) {
                        res.put(key, value);
                    }
                }
            }
        }
        return res;
    }

    public <T> String toEChartsBarData(T t) {
        List<String> nameArr = Lists.newArrayList();
        List<Object> dataArr = Lists.newArrayList();
        if (t instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) t;
            Set<Object> keys = ((Map<Object, Object>) t).keySet();
            for (Object key : keys) {
                Object value = map.get(key);
                if (key instanceof String && !(value instanceof Collection)) {
                    nameArr.add((String) key);
                    dataArr.add(value);
                }
            }
        } else if(t instanceof Collection) {
            log.info("toEChartsBarData t is a collection");
        } else {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field: fields) {
                field.setAccessible(true);
                nameArr.add(field.getName());
                try {
                    dataArr.add(String.valueOf(field.get(t)));
                } catch (IllegalAccessException e) {
                    log.error("reflect get field value error", e);
                }
            }
        }
        Map<String, Object> res = Maps.newHashMap();
        res.put("names", nameArr);
        res.put("datas", dataArr);
        return JSON.toJSONString(res);
    }

    public <T> String toEChartsPieData(T t) {
        List<Map<String, Object>> dataArr = Lists.newArrayList();
        if (t instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) t;
            Set<Object> keys = ((Map<Object, Object>) t).keySet();
            for (Object key : keys) {
                Map<String, Object> data = Maps.newHashMap();
                Object value = map.get(key);
                if (key instanceof String && !(value instanceof Collection)) {
                    data.put("value", value);
                    data.put("name", key);
                    dataArr.add(data);
                }
            }
        } else if(t instanceof Collection) {
            log.info("toEChartsBarData t is a collection");
        } else {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field: fields) {
                Map<String, Object> data = Maps.newHashMap();
                field.setAccessible(true);
                data.put("name", field.getName());
                try {
                    data.put("value", field.get(t));
                } catch (IllegalAccessException e) {
                    log.error("reflect get field value error", e);
                }
                dataArr.add(data);
            }
        }
        return JSON.toJSONString(dataArr);
    }

    public List<ProdItem> getProdItems() {
        List<String> typeNames = Lists.newArrayList();
        for (ProductionTypeEnum typeEnum : ProductionTypeEnum.values()) {
            if (typeEnum.getCode() != 0) {
                typeNames.add(typeEnum.getName());
            }
        }

        List<ProdItem> prodItems = Lists.newArrayList();
        for (String typeName : typeNames) {
            String key = PROD_CATE_LIST_KEY + typeName;
            List<String> prodList = redisClient.sort(key, "", 0, 10, false);
            prodList.forEach(id -> {
                if (StringUtils.isNotBlank(id)) {
                    ProdItem prodItem = new ProdItem();
                    prodItem.setId(Long.valueOf(id));
                    prodItem.setTitle(id + ".jpg");
                    prodItem.setType(typeName);
                    prodItems.add(prodItem);
                }
            });
        }
        return prodItems;
    }
}
