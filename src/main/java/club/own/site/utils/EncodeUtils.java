package club.own.site.utils;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;

@Slf4j
public class EncodeUtils {

    public static <T> T encode(T t) {
        if (t != null) {
            Field[] fields = t.getClass().getDeclaredFields();
            try {
                for (Field field: fields) {
                    field.setAccessible(true);
                    if (field.getType().getName().endsWith("String")) {
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), t.getClass());
                        Method rm = pd.getReadMethod();
                        String value = (String) rm.invoke(t);
                        if (StringUtils.isBlank(value)) {
                            continue;
                        }
                        Method wm = pd.getWriteMethod();
                        wm.invoke(t, URLEncoder.encode(value, Charsets.UTF_8.name()));
                    }
                }
            } catch (Exception e) {
                log.error("encode obj error, param:{}", t, e);
            }
        }
        return t;
    }
}
