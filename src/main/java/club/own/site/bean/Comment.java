package club.own.site.bean;

import lombok.Data;

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
