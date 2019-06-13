package club.own.site.bean;

import lombok.Data;

@Data
public class ProdItem extends BaseModel{

    private long id;
    private String title;
    private String type;

}
