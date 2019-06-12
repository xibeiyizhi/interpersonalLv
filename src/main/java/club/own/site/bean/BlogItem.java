package club.own.site.bean;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class BlogItem {
    private long id = System.currentTimeMillis();
    private String title;
    private Member author;
    private String content;
    private String brief;
    private String firstImgUrl;
    private List<String> imgList = Lists.newArrayList();
    private List<Comment> comments = Lists.newArrayList();
    private int viewCount;
    private String createTime;

    public String getFirstImgUrl() {
        if (StringUtils.isNotBlank(firstImgUrl) && !firstImgUrl.toLowerCase().startsWith("http")) {
            firstImgUrl += "/" + firstImgUrl;
        }
        return firstImgUrl;
    }
}
