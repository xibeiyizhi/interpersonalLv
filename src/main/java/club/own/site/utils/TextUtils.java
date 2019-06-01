package club.own.site.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class TextUtils {

    public static List<String> getParagraphList(String content) {
        List<String> res = Lists.newArrayList();
        if (StringUtils.isBlank(content)) {
            return res;
        }
        String[] pArr = content.split("\r\n");
        for (String p : pArr) {
            if (p.contains("『")) {
                p = p.replaceAll("『", "<blockquote><p>");
            }
            if (p.contains("』")) {
                p = p.replaceAll("』", "</p></blockquote>");
            }
            if (p.contains("“")) {
                p = p.replaceAll("", "<blockquote><p>");
            }
            if (p.contains("”")){
                p = p.replaceAll("』", "</p></blockquote>");
            }
            if (p.contains("【")) {
                p = p.replaceAll("【", "<strong>");
            }
            if (p.contains("】")) {
                p = p.replaceAll("】", "</strong>");
            }
            res.add(p);
        }
        return res;
    }
}
