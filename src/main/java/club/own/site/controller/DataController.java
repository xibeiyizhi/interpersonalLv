package club.own.site.controller;

import club.own.site.bean.CpuInfo;
import club.own.site.bean.DiskInfo;
import club.own.site.bean.MachineInfo;
import club.own.site.utils.NumberUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/statistics")
public class DataController extends BaseController{

    @RequestMapping(value = "/cpu", method = RequestMethod.GET)
    public String cpu() throws Exception {
        Sigar sigar = new Sigar();
        CpuPerc cpuList[] = null;
        try {
            cpuList = sigar.getCpuPercList();
        } catch (SigarException e) {
            log.error("getCpuPercList error", e);
            return "";
        }
        List<String> res = Lists.newArrayList();
        for (int i = 0; i < cpuList.length; i++) {
            CpuPerc cpuPerc = cpuList[i];
            CpuInfo cpuInfo = new CpuInfo();
            cpuInfo.setUser(NumberUtils.formatFloat(cpuPerc.getUser() * 100D));
            cpuInfo.setSys(NumberUtils.formatFloat(cpuPerc.getSys() * 100D));
            cpuInfo.setWait(NumberUtils.formatFloat(cpuPerc.getWait() * 100D));
            cpuInfo.setIdle(NumberUtils.formatFloat(cpuPerc.getIdle() * 100D));
            cpuInfo.setOther(NumberUtils.formatFloat((cpuPerc.getCombined() - cpuPerc.getUser() - cpuPerc.getSys())  * 100D));

            res.add(toEChartsPieData(cpuInfo));
        }
        return JSON.toJSONString(res);
    }

    @RequestMapping(value = "/disk", method = RequestMethod.GET)
    public String disk() throws Exception {
        DiskInfo diskInfo = new DiskInfo();
        Sigar sigar = new Sigar();
        FileSystem[] fsList = sigar.getFileSystemList();
        for (FileSystem fs : fsList) {
            FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
            if (fs.getType() == 2) {
                diskInfo.setTotal(NumberUtils.formatFloat(usage.getTotal() / 1024d /1024d / 10d));
                diskInfo.setFree(NumberUtils.formatFloat(usage.getFree() / 1024d /1024d / 10d));
                diskInfo.setAvail(NumberUtils.formatFloat(usage.getAvail() / 1024d /1024d / 10d));
                diskInfo.setUsed(NumberUtils.formatFloat(usage.getUsed() / 1024d /1024d / 10d));
                diskInfo.setUsePercent(NumberUtils.formatFloat(usage.getUsePercent() * 100));
            }
        }
        return toEChartsBarData(diskInfo);
    }

    @RequestMapping(value = "/mem", method = RequestMethod.GET)
    public String machine(){
        Sigar sigar = new Sigar();
        MachineInfo vo = new MachineInfo();
        try{
            int ratioCPU = new BigDecimal(sigar.getCpuPerc().getCombined()).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).intValue();
            vo.setRatioCPU(ratioCPU);
            Mem mem = sigar.getMem();
            double totalMemory = new BigDecimal(mem.getTotal() / 1024.00 /1024.00 /1024.00).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            vo.setTotalMemory(totalMemory);

            double useMemory = new BigDecimal(mem.getUsed() / 1024.00 /1024.00 /1024.00).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            vo.setUseMemory(useMemory);

            double freeMemory = new BigDecimal(mem.getFree() / 1024.00 /1024.00 /1024.00).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            vo.setFreeMemory(freeMemory);

            double ratioMemory = new BigDecimal(useMemory/totalMemory).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).doubleValue();
            vo.setRatioMemory(ratioMemory + "%");
        } catch (Exception e) {
            log.error("获取服务器运行信息失败", e);
        }
        return toEChartsBarData(vo);
    }


    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
        String property = System.getProperty("java.library.path");
        log.info("java.library.path: {}",property);
        return property;
    }
}
