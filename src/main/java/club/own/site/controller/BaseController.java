package club.own.site.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Controller
public class BaseController {

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
}
