package club.own.site.utils;

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
    public static void main(String[] args) {
        System.out.println(formatNumThreeDigits(1));
    }
}
