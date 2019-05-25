package club.own.site.bean;

import club.own.site.annotation.Trim;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
@Data
public class BaseModel {

    BaseModel(){
        super();
    }


    public <T>  T trim() {
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field :fields){
            if (field.isAnnotationPresent(Trim.class)) {
                Trim trim = field.getAnnotation(Trim.class);
                if (trim.value()) {
                    PropertyDescriptor pd = null;
                    try {
                        pd = new PropertyDescriptor(field.getName(), this.getClass());
                        Method rm = pd.getReadMethod();
                        String value = (String) rm.invoke(this);
                        if (StringUtils.isBlank(value)) {
                            continue;
                        }
                        Method wm = pd.getWriteMethod();
                        wm.invoke(this, value.trim());
                    } catch (Exception e) {
                        log.error("trim annotation parse error", e);
                    }

                }
            }
        }
        return (T)this;
    }
}
