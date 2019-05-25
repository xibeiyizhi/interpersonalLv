package club.own.site.bean;

import com.google.common.base.Charsets;
import lombok.Data;

import java.net.URLEncoder;
import java.util.List;

@Data
public class Comment {
    private long id = System.currentTimeMillis();
    private Member author;
    private String createTime;
    private String content;
    private List<Reply> replyList;

    @Data
    public class Reply {
        private long id = System.currentTimeMillis();
        private Member author;
        private String createTime;
        private String content;

    }
}
