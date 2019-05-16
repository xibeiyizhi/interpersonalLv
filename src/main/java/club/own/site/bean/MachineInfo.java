package club.own.site.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class MachineInfo {
    private int ratioCPU;
    private double totalMemory;
    private double useMemory;
    private double freeMemory;
    private String ratioMemory;

    public String toString(){
        return JSON.toJSONString(this);
    }
}
