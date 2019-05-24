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

    public String getTitle() {
        try {
            return URLEncoder.encode(title, Charsets.UTF_8.name());
        } catch (Exception e) {
            return title;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        try {
            return URLEncoder.encode(content, Charsets.UTF_8.name());
        } catch (Exception e) {
            return content;
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBrief() {
        try {
            return URLEncoder.encode(brief, Charsets.UTF_8.name());
        } catch (Exception e) {
            return brief;
        }
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getFirstImgUrl() {
        try {
            return URLEncoder.encode(firstImgUrl, Charsets.UTF_8.name());
        } catch (Exception e) {
            return firstImgUrl;
        }
    }

    public void setFirstImgUrl(String firstImgUrl) {
        this.firstImgUrl = firstImgUrl;
    }
}
