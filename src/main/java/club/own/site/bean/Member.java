package club.own.site.bean;

import club.own.site.annotation.Trim;
import club.own.site.factory.ModelFactory;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class Member extends BaseModel{
    private long id = System.currentTimeMillis();
    @Trim
    private String name;
    private String profession;
    private String message;
    private String showMobile;
    private String mobile;
    private String email;
    private String address;
    private String firstImgUrl;
    private List<String> photos = Lists.newArrayList();

    public static void main(String[] args) {
        ModelFactory factory = new ModelFactory();
        Member member = (Member) factory.createInstance(Member.class);
        member.setName("a b c ");
        member = member.trim();
        System.out.println(member.getName());
    }
}
