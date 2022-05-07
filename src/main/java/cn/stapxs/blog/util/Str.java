package cn.stapxs.blog.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Version: 1.0
 * @Date: 2021/12/10 14:22
 * @ClassName: Str
 * @Author: Stapxs
 * @Description TO DO
 **/
public class Str {

    public static List<String> SplitSpace(String str) {
        List<String> matchList = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(str);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                matchList.add(regexMatcher.group(2));
            } else {
                matchList.add(regexMatcher.group());
            }
        }
        return matchList;
    }

    public static String formatByte(long byteNumber, boolean showUnit) {
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if(showUnit) {
            if (kbNumber < FORMAT) {
                return new DecimalFormat("#.##KB").format(kbNumber);
            }
            double mbNumber = kbNumber / FORMAT;
            if (mbNumber < FORMAT) {
                return new DecimalFormat("#.##MB").format(mbNumber);
            }
            double gbNumber = mbNumber / FORMAT;
            if (gbNumber < FORMAT) {
                return new DecimalFormat("#.##GB").format(gbNumber);
            }
            double tbNumber = gbNumber / FORMAT;
            return new DecimalFormat("#.##TB").format(tbNumber);
        } else {
            if (kbNumber < FORMAT) {
                return new DecimalFormat("#.##").format(kbNumber);
            }
            double mbNumber = kbNumber / FORMAT;
            if (mbNumber < FORMAT) {
                return new DecimalFormat("#.##").format(mbNumber);
            }
            double gbNumber = mbNumber / FORMAT;
            if (gbNumber < FORMAT) {
                return new DecimalFormat("#.##").format(gbNumber);
            }
            double tbNumber = gbNumber / FORMAT;
            return new DecimalFormat("#.##").format(tbNumber);
        }
    }

}
