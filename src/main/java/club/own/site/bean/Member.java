package club.own.site.bean;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import lombok.Data;

import java.net.URLEncoder;
import java.util.List;

@Data
public class Member {
    private long id = System.currentTimeMillis();
    private String name;
    private String profession;
    private String message;
    private String showMobile;
    private String mobile;
    private String email;
    private String address;
    private String firstImgUrl;
    private List<String> photos = Lists.newArrayList();

    public String getName() {
        try {
            return URLEncoder.encode(name, Charsets.UTF_8.name());
        } catch (Exception e) {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        try {
            return URLEncoder.encode(profession, Charsets.UTF_8.name());
        } catch (Exception e) {
            return profession;
        }
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMessage() {
        try {
            return URLEncoder.encode(message, Charsets.UTF_8.name());
        } catch (Exception e) {
            return message;
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        try {
            return URLEncoder.encode(address, Charsets.UTF_8.name());
        } catch (Exception e) {
            return address;
        }
    }

    public void setAddress(String address) {
        this.address = address;
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
