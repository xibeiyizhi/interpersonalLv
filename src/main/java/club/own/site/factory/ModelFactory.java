package club.own.site.factory;

import club.own.site.bean.BaseModel;

public class ModelFactory {

    public BaseModel createInstance(Class<? extends BaseModel> clazz) {
        BaseModel model = null;
        try {
            model = clazz.newInstance();
            model.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
