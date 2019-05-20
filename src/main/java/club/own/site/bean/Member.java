package club.own.site.bean;

import lombok.Data;

@Data
public class Member {
    private String name;
    private String profession;
    private String message;
    private String mobile;
    private String email;
    private String address;
    private String firstImgUrl;

    public Member(String name, String profession, String message, String mobile, String email, String address) {
        this.name = name;
        this.profession = profession;
        this.message = message;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
    }

}
