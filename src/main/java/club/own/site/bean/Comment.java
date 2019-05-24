package club.own.site.bean;

import com.google.common.base.Charsets;

import java.net.URLEncoder;
import java.util.List;


public class Comment {
    private long id = System.currentTimeMillis();
    private Member author;
    private String createTime;
    private String content;
    private List<Reply> replyList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

    public class Reply {
        private long id = System.currentTimeMillis();
        private Member author;
        private String createTime;
        private String content;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Member getAuthor() {
            return author;
        }

        public void setAuthor(Member author) {
            this.author = author;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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
    }
}
