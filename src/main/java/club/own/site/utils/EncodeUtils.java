package club.own.site.utils;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class EncodeUtils {

    public static String encode(String s) {
        if (StringUtils.isNotBlank(s)) {
            try {
                return URLEncoder.encode(s, Charsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                log.error("encode error, param={}", s, e);
            }
        }
        return s;
    }


    public static <T> T encodeObj(T t) {
        if (t == null) {
            return t;
        }
        if (t instanceof Collection) {
            for (Iterator it = ((Collection) t).iterator(); it.hasNext();) {
                encodeSingleObj(it.next());
            }
        } else if (t instanceof Map) {
            for (Iterator it = ((Map) t).values().iterator(); it.hasNext();) {
                encodeSingleObj(it.next());
            }
        } else {
            encodeSingleObj(t);
        }
        return t;
    }

    public static <T> T encodeSingleObj(T t) {
        if (t != null
                && !(t instanceof String)
                && !(t instanceof Number)
                && !(t instanceof Collection)
                && !(t instanceof Map)
                && !(t instanceof Boolean)) {
            Field[] fields = t.getClass().getDeclaredFields();
            try {
                for (Field field: fields) {
                    field.setAccessible(true);
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), t.getClass());
                    Method rm = pd.getReadMethod();
                    Object obj = rm.invoke(t);
                    if (obj instanceof String) {
                        String value = (String) rm.invoke(t);
                        if (StringUtils.isBlank(value)) {
                            continue;
                        }
                        Method wm = pd.getWriteMethod();
                        wm.invoke(t, URLEncoder.encode(value, Charsets.UTF_8.name()));
                    } else if (obj instanceof Collection) {
                        if (CollectionUtils.isEmpty((Collection)obj)) {
                            continue;
                        }
                        encodeObj(obj);
                    } else if (obj instanceof Map) {
                        if (MapUtils.isEmpty((Map)obj)) {
                            continue;
                        }
                        encodeObj(obj);
                    } else if (obj instanceof Number || obj instanceof Boolean){
                    } else {
                        encodeObj(obj);
                    }
                }
            } catch (Exception e) {
                log.error("encodeObj obj error, param:{}", t);
            }
        }
        return t;
    }
}
