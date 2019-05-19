package club.own.site.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class MachineInfo {
    private double totalMemory;
    private double useMemory;
    private double freeMemory;
    private double ratioMemory;

    public double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public double getUseMemory() {
        return useMemory;
    }

    public void setUseMemory(double useMemory) {
        this.useMemory = useMemory;
    }

    public double getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(double freeMemory) {
        this.freeMemory = freeMemory;
    }

    public double getRatioMemory() {
        return ratioMemory;
    }

    public void setRatioMemory(double ratioMemory) {
        this.ratioMemory = ratioMemory;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
