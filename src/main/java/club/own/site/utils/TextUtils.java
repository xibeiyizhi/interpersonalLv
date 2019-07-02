package club.own.site.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
                p = p.replaceAll("“", "<blockquote><p>");
            }
            if (p.contains("”")){
                p = p.replaceAll("”", "</p></blockquote>");
            }
            if (p.contains("【")) {
                p = p.replaceAll("【", "<strong>");
            }
            if (p.contains("】")) {
                p = p.replaceAll("】", "</strong>");
            }
            try {
                res.add(URLEncoder.encode(p, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                res.add(p);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String str = "7月1日，被称为“史上最严”垃圾分类措施的《上海市生活垃圾管理条例》正式实施。当日，上海各执法部门联合进行社区单位垃圾分类专项检查，指导民众做好垃圾分类工作。";
        List<String> list = getParagraphList(str);
        System.out.println(list);
    }
}
