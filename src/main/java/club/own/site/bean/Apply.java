package club.own.site.bean;

import lombok.Data;

@Data
public class Apply extends Member {
    boolean isAgree;

    public Apply() {
    }

    public Apply(Member member) {
        this.setName(member.getName());
        this.setFirstImgUrl(member.getFirstImgUrl());
        this.setPhotos(member.getPhotos());
        this.setShowMobile(member.getShowMobile());
        this.setAddress(member.getAddress());
        this.setEmail(member.getEmail());
        this.setId(member.getId());
        this.setMessage(member.getMessage());
        this.setMobile(member.getMobile());
        this.setProfession(member.getProfession());
    }
}
