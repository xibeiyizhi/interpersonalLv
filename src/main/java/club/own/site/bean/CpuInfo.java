package club.own.site.bean;

import lombok.Data;

@Data
public class CpuInfo {
    private String user;
    private String sys;
    private String wait;
    private String idle;
    private String other;
}
