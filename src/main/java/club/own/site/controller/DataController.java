package club.own.site.controller;

import club.own.site.bean.CpuInfo;
import club.own.site.bean.DiskInfo;
import club.own.site.bean.MachineInfo;
import club.own.site.utils.NumberUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/statistics")
public class DataController extends BaseController{

    @RequestMapping(value = "/cpu", method = RequestMethod.GET)
    public String cpu() throws Exception {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor processor = hal.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        List<String> res = Lists.newArrayList();
        for (int i = 0; i < ticks.length; i++) {

            long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
            CpuInfo cpuInfo = new CpuInfo();

            cpuInfo.setUser(NumberUtils.formatFloat(user  * 100D / totalCpu));
            cpuInfo.setSys(NumberUtils.formatFloat(sys * 100D / totalCpu));
            cpuInfo.setWait(NumberUtils.formatFloat(iowait * 100D / totalCpu));
            cpuInfo.setIdle(NumberUtils.formatFloat(idle * 100D / totalCpu));
            cpuInfo.setOther(NumberUtils.formatFloat((steal + irq + softirq + nice)  * 100D));

            res.add(toEChartsPieData(cpuInfo));
        }
        return JSON.toJSONString(res);
    }

    @RequestMapping(value = "/disk", method = RequestMethod.GET)
    public String disk() throws Exception {
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        DiskInfo diskInfo = new DiskInfo();
        for (OSFileStore fs : fsArray) {
            long usable = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            if (fs.getType().toLowerCase().equals("ntfs") || fs.getType().toLowerCase().equals("apfs")) {
                diskInfo.setTotal(NumberUtils.formatFloat(total / 1024d /1024d / 10d));
                diskInfo.setFree(NumberUtils.formatFloat(usable / 1024d /1024d / 10d));
                diskInfo.setUsed(NumberUtils.formatFloat((total - usable) / 1024d /1024d / 10d));
                diskInfo.setUsePercent(NumberUtils.formatFloat((total - usable) * 100 / total));
                break;
            }
        }
        return toEChartsBarData(diskInfo);
    }

    @RequestMapping(value = "/mem", method = RequestMethod.GET)
    public String machine(){
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        GlobalMemory memory = hal.getMemory();
        MachineInfo vo = new MachineInfo();
        try{
            double totalMemory = new BigDecimal(memory.getTotal() / 1024.00 /1024.00 /1024.00).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            vo.setTotalMemory(totalMemory);

            double useMemory = new BigDecimal((memory.getTotal() - memory.getAvailable()) / 1024.00 /1024.00 /1024.00).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            vo.setUseMemory(useMemory);

            double freeMemory = new BigDecimal(memory.getAvailable() / 1024.00 /1024.00 /1024.00).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            vo.setFreeMemory(freeMemory);

            double ratioMemory = new BigDecimal(useMemory /totalMemory).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("10")).doubleValue();
            vo.setRatioMemory(ratioMemory);
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
