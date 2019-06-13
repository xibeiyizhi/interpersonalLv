package club.own.site.enums;

import lombok.Getter;
import lombok.Setter;

public enum ProductionTypeEnum {
    ALL(0, "all", "全部"),
    PEOPLE(1, "people", "人物"),
    SCENERY(2, "scenery","风景"),
    OTHER(3, "other", "其他"),
    ;

    @Getter
    @Setter
    private int code;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String desc;

    ProductionTypeEnum(int code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public static String getNameByCode(int code) {
        for (ProductionTypeEnum typeEnum: ProductionTypeEnum.values()) {
            if (typeEnum.getCode() == code) {
                return typeEnum.getName();
            }
        }
        return "";
    }
}
