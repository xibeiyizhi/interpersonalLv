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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getAvail() {
        return avail;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getUsePercent() {
        return usePercent;
    }

    public void setUsePercent(String usePercent) {
        this.usePercent = usePercent;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
