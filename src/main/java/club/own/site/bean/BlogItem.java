package club.own.site.bean;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import lombok.Data;

import java.net.URLEncoder;
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
    private List<Comment> comments;
    private int viewCount;
    private String createTime;

}
