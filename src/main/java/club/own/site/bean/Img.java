package club.own.site.bean;

import lombok.Data;

@Data
public class Img {
    private String imgName;
    private String className;

    public Img(String name, String className) {
        this.imgName = name;
        this.className = className;
    }
}
