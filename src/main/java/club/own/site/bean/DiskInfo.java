package club.own.site.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class DiskInfo {
    private String total;//硬盘总大小
    private String free; //剩余大小
    private String avail; // 可用大小
    private String used; // 已使用
    private String usePercent; // 使用率

    public String toString(){
        return JSON.toJSONString(this);
    }
}
