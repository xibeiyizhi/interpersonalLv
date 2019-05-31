package club.own.site.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateTimeUtils {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String BLOG_SHOW_FORMAT = "MM dd, yyyy";

    public static String toBlogShowFormat(String date) {
        if (StringUtils.isNotBlank(date)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
            Date dateTime = null;
            try {
                dateTime = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                log.error("parse error", e);
            }
            simpleDateFormat = new SimpleDateFormat(BLOG_SHOW_FORMAT);
            return simpleDateFormat.format(dateTime);
        }
        return "";
    }

    public static String formatDate(Date date) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
        return simpleDateFormat.format(date);
    }

    public static String getCurrentFormatTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
        return simpleDateFormat.format(new Date());
    }
}
