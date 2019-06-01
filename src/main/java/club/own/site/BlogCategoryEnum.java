package club.own.site;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public enum  BlogCategoryEnum {
    NEWS(0,"新闻时事"),
    NOTES(1,"公告通知"),
    TOPIC(2,"情感话题"),
    ARTS(3,"技术艺术"),
    STORY(4,"闲话杂说"),
    ;


    @Getter
    @Setter
    private String category;
    @Getter
    @Setter
    private int code;

    BlogCategoryEnum(int code, String category) {
        this.code = code;
        this.category = category;
    }

    public static List<String> getBlogCategorys() {
        List<String> cates = Lists.newArrayList();
        for (BlogCategoryEnum categoryEnum: BlogCategoryEnum.values()) {
            cates.add(categoryEnum.getCategory());
        }
        return cates;
    }

    public static String getNameByCode(String code) {
        int codeInt = StringUtils.isNotBlank(code) ? Integer.valueOf(code) : -1;
        BlogCategoryEnum categoryEnum = getEnumByCode(codeInt);
        if (categoryEnum != null) {
            return categoryEnum.name();
        }
        return "";
    }

    public static BlogCategoryEnum getEnumByCode(int code) {
        for (BlogCategoryEnum categoryEnum: BlogCategoryEnum.values()) {
            if (code == categoryEnum.getCode()) {
                return categoryEnum;
            }
        }
        return null;
    }
}
