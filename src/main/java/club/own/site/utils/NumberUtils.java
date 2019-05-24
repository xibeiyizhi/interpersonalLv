package club.own.site.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

public class NumberUtils {

    public static String formatNumThreeDigits(int i) {
        if (i == 0) {
            return "000";
        }
        DecimalFormat df = new DecimalFormat("000");
        return df.format(i);
    }

    public static String formatFloat(double i) {
        if (i == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(i);
    }

    /**
     *
     * @param num source num
     * @param from start index, include
     * @param to end index, exclude
     * @return hide num
     */
    public static String hideNum(String num, int from, int to) {
        StringBuilder res = new StringBuilder();
        if (StringUtils.isNotBlank(num) && from >=0 && to < num.length() && from <= to) {
            String pre = num.substring(0, from);
            res.append(pre);
            for (int i=from; i < to; i++) {
                res.append("*");
            }
            res.append(num.substring(to));

        }
        return res.toString();
    }

    public static void main(String[] args) {
        System.out.println(formatNumThreeDigits(1));
        System.out.println(hideNum("13488788682", 3, 7));
    }
}
