package club.own.site.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Quotation {

    private List<String> fatherSay = new ArrayList<>();
    private List<String> monSay = new ArrayList<>();
    private List<String> sisSay = new ArrayList<>();
    private List<String> iSay = new ArrayList<>();

    public Quotation() {
        fatherSay.add("生命在于运动");
        fatherSay.add("成就来自坚持");
        fatherSay.add("我自横拍立马，谁敢向我呲牙，哈哈哈哈");
        fatherSay.add("啊………百……灵鸟……向蓝天……飞……");
        fatherSay.add("人无远虑，必有近忧");
        fatherSay.add("放开胸怀，拥抱变化");
        fatherSay.add("相当年，咱也是风流人物");
        fatherSay.add("男儿立身于世，自当奋勇向前");
        fatherSay.add("规规矩矩做事，认认真真做人");
        fatherSay.add("凡事三思而后行");
        fatherSay.add("行事进退要有理有节");

        monSay.add("保重身体呀");
        monSay.add("注意安全呀");
        monSay.add("吃好穿好呀");
        monSay.add("家里都好，你们放心");
        monSay.add("去跳广场舞啊");
        monSay.add("有什么心事和妈说……");
        monSay.add("快快结婚啊，快快生娃啊，给你们照顾孩子呀");
        monSay.add("有没有喜欢的人呀");
        monSay.add("人生是苦难的发酵");
        monSay.add("要有足够的时间");
        monSay.add("还要有足够的热情");
        monSay.add("才能得到苦尽甘来的美酒");

        sisSay.add("一二三四……二二三四……快来减肥啊");
        sisSay.add("妈妈节日快乐哦");
        sisSay.add("感谢妈妈");
        sisSay.add("来啊，互相伤害啊");
        sisSay.add("我为妈妈唱支歌");
        sisSay.add("啊……五环……百花丛中……嗯嗯嗯");
        sisSay.add("自己的事情自己做主");
        sisSay.add("姐没钱了，兄弟拿点出来花花呗");
        sisSay.add("妈，宝宝生病了……怎么办……在线等");

        iSay.add("别问我……瘦过");
        iSay.add("别问我……帅过");
        iSay.add("世间从无什么感同身受");
        iSay.add("妈妈节日快乐");
        iSay.add("感谢妈妈");
        iSay.add("我为妈妈唱支歌");
        iSay.add("啊……世上只有妈妈好……呃……爸爸来了");
        iSay.add("来啊，互相伤害啊");
        iSay.add("发工资了……怎么花啊……");
        iSay.add("哼，别问我工资多少，比你多就是了……");
    }

    public List<String> getMonSay() {
        return monSay;
    }

    public List<String> getFatherSay() {
        return fatherSay;
    }

    public List<String> getSisSay() {
        return sisSay;
    }

    public List<String> getiSay() {
        return iSay;
    }
}
