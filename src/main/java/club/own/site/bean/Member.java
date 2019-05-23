package club.own.site.bean;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class Member {
    private long id;
    private String name;
    private String profession;
    private String message;
    private String mobile;
    private String email;
    private String address;
    private String firstImgUrl;
    private List<String> photos = Lists.newArrayList();
}
