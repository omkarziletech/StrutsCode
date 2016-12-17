package com.gp.cong.common;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;

/**
 *
 * @author Lakshminarayanan
 */
public class CommonUtils implements Serializable {

    public static final Log log = LogFactory.getLog(CommonUtils.class);
    private static final long serialVersionUID = 6966186528410359291L;

    public static String getMimeType(String fileName, byte[] bytes) throws Exception {
        String mimeType = new MagicMimeTypeIdentifier().identify(bytes, fileName, null);
        return isNotEmpty(mimeType) ? mimeType : "application/octet-stream";
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static String concatenate(String str1, String str2) {
        return (isAllNotEmpty(str1, str2) ? (str1 + "," + str2) : (isNotEmpty(str1) ? str1 : (isNotEmpty(str2) ? str2 : "")));
    }

    public static boolean isStartsWith(String string, String prefix) {
        return null != string && null != prefix ? string.toUpperCase().startsWith(prefix.toUpperCase()) : false;
    }

    public static boolean isEmpty(String string) {
        return null == string || string.trim().isEmpty();
    }

    public static boolean isEmpty(StringBuffer stringBuffer) {
        return null == stringBuffer || stringBuffer.toString().trim().isEmpty();
    }

    public static boolean isEmpty(StringBuilder stringBuilder) {
        return null == stringBuilder || stringBuilder.toString().trim().isEmpty();
    }

    public static boolean isEmpty(Integer integer) {
        return null == integer || integer == 0;
    }

    public static boolean isEmpty(Long lng) {
        return null == lng || BigDecimal.valueOf(lng).compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isEmpty(BigDecimal bdm) {
        return null == bdm || bdm.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isEmpty(Boolean bool) {
        return null == bool || bool.compareTo(Boolean.valueOf(bool)) == 0;
    }

    public static boolean isEmpty(Double dbl) {
        return null == dbl || BigDecimal.valueOf(NumberUtils.round(dbl)).compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return null != str && !str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String[] strs) {
        return null != strs && strs.length > 0;
    }

    public static boolean isAtLeastOneNotEmpty(String... strs) {
        if (null != strs) {
            for (String str : Arrays.asList(strs)) {
                if (isNotEmpty(str)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
    public static boolean isAlpha(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean isAllEmpty(String... strs) {
        if (null != strs) {
            for (String str : strs) {
                if (isNotEmpty(str)) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean isAllNotEmpty(String... strs) {
        if (null != strs) {
            for (String str : strs) {
                if (isEmpty(str)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(StringBuffer buffer) {
        return null != buffer && !buffer.toString().trim().isEmpty();
    }

    public static boolean isNotEmpty(StringBuilder builder) {
        return null != builder && !builder.toString().trim().isEmpty();
    }

    public static boolean isNotEmpty(Integer integer) {
        return null != integer && integer != 0;
    }

    public static boolean isNotEmpty(Long lng) {
        return null != lng && BigDecimal.valueOf(lng).compareTo(BigDecimal.ZERO) != 0;
    }

    public static boolean isNotEmpty(Float flot) {
        return null != flot && BigDecimal.valueOf(NumberUtils.round(flot)).compareTo(BigDecimal.ZERO) != 0;
    }

    public static boolean isNotEmpty(Double dbl) {
        return null != dbl && BigDecimal.valueOf(NumberUtils.round(dbl)).compareTo(BigDecimal.ZERO) != 0;
    }
    
     public static boolean isNotEmpty(BigDecimal bgd) {
        return null != bgd && !bgd.toString().isEmpty();
    }

    public static boolean isNotEmpty(Collection collection) {
        return null != collection && !collection.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return null != map && !map.isEmpty();
    }

    public static boolean isEqual(String str1, String str2) {
        return (null == str1 && null == str2) || (null != str1 && null != str2 && str1.trim().equals(str2.trim()));
    }

    public static boolean isEqualIgnoreCase(String str1, String str2) {
        return (null == str1 && null == str2) || (null != str1 && null != str2 && str1.toUpperCase().trim().equals(str2.toUpperCase().trim()));
    }

    public static boolean isEqual(StringBuffer buffer1, StringBuffer buffer2) {
        return (null == buffer1 && null == buffer2) || (null != buffer1 && null != buffer2 && buffer1.toString().trim().equals(buffer2.toString().trim()));
    }

    public static boolean isEqualIgnoreCase(StringBuffer buffer1, StringBuffer buffer2) {
        return (null == buffer1 && null == buffer2)
                || (null != buffer1 && null != buffer2 && buffer1.toString().toUpperCase().trim().equals(buffer2.toString().toUpperCase().trim()));
    }

    public static boolean isEqual(StringBuilder builder1, StringBuilder builder2) {
        return (null == builder1 && null == builder2)
                || (null != builder1 && null != builder2 && builder1.toString().trim().equals(builder2.toString().trim()));
    }

    public static boolean isEqualIgnoreCase(StringBuilder builder1, StringBuilder builder2) {
        return (null == builder1 && null == builder2)
                || (null != builder1 && null != builder2 && builder1.toString().toUpperCase().trim().equals(builder2.toString().toUpperCase().trim()));
    }

    public static boolean isEqual(Integer integer1, Integer integer2) {
        return (null == integer1 && null == integer2)
                || (null != integer1 && null != integer2 && BigInteger.valueOf(integer1).compareTo(BigInteger.valueOf(integer2)) == 0);
    }

    public static boolean isEqual(Long long1, Long long2) {
        return (null == long1 && null == long2)
                || (null != long1 && null != long2 && BigInteger.valueOf(long1).compareTo(BigInteger.valueOf(long2)) == 0);
    }

    public static boolean isEqual(Float float1, Float float2) {
        return (null == float1 && null == float2) || (null != float1 && null != float2
                && BigDecimal.valueOf(NumberUtils.round(float1)).compareTo(BigDecimal.valueOf(NumberUtils.round(float2))) == 0);
    }

    public static boolean isEqual(Double double1, Double double2) {
        return (null == double1 && null == double2) || (null != double1 && null != double2
                && BigDecimal.valueOf(NumberUtils.round(double1)).compareTo(BigDecimal.valueOf(NumberUtils.round(double2))) == 0);
    }

    public static boolean isEqual(Boolean boolean1, Boolean boolean2) {
        return (null == boolean1 && null == boolean2) || (null != boolean1 && null != boolean2 && boolean1 == boolean2);
    }

    public static boolean isNotEqual(String str1, String str2) {
        return (null == str1 && null != str2) || (null != str1 && null == str2) || (null != str1 && null != str2 && !str1.trim().equals(str2.trim()));
    }

    public static boolean isNotEqualIgnoreCase(String str1, String str2) {
        return (null == str1 && null != str2) || (null != str1 && null == str2)
                || (null != str1 && null != str2 && !str1.toUpperCase().trim().equals(str2.toUpperCase().trim()));
    }

    public static boolean isNotEqual(StringBuffer buffer1, StringBuffer buffer2) {
        return (null == buffer1 && null != buffer2) || (null != buffer1 && null == buffer2)
                || (null != buffer1 && null != buffer2 && !buffer1.toString().trim().equals(buffer2.toString().trim()));
    }

    public static boolean isNotEqualIgnoreCase(StringBuffer buffer1, StringBuffer buffer2) {
        return (null == buffer1 && null != buffer2) || (null != buffer1 && null == buffer2)
                || (null != buffer1 && null != buffer2 && !buffer1.toString().toUpperCase().trim().equals(buffer2.toString().toUpperCase().trim()));
    }

    public static boolean isNotEqual(StringBuilder builder1, StringBuilder builder2) {
        return (null == builder1 && null != builder2) || (null != builder1 && null == builder2)
                || (null != builder1 && null != builder2 && !builder1.toString().trim().equals(builder2.toString().trim()));
    }

    public static boolean isNotEqualIgnoreCase(StringBuilder builder1, StringBuilder builder2) {
        return (null == builder1 && null != builder2) || (null != builder1 && null == builder2)
                || (null != builder1 && null != builder2 && !builder1.toString().toUpperCase().trim().equals(builder2.toString().toUpperCase().trim()));
    }

    public static boolean isNotEqualIgnoreEmpty(String str1, String str2) {
        return (null != str1 && !str1.trim().isEmpty()
                && null != str2 && !str2.trim().isEmpty()
                && !str1.toUpperCase().trim().equals(str2.toUpperCase().trim()));
    }

    public static boolean isNotEqual(Integer integer1, Integer integer2) {
        return (null == integer1 && null != integer2) || (null != integer1 && null == integer2)
                || (null != integer1 && null != integer2 && BigInteger.valueOf(integer1).compareTo(BigInteger.valueOf(integer2)) != 0);
    }

    public static boolean isNotEqual(Long long1, Long long2) {
        return (null == long1 && null != long2) || (null != long1 && null == long2)
                || (null != long1 && null != long2 && BigInteger.valueOf(long1).compareTo(BigInteger.valueOf(long2)) != 0);
    }

    public static boolean isNotEqual(Float float1, Float float2) {
        return (null == float1 && null != float2) || (null != float1 && null == float2) || (null != float1 && null != float2
                && BigDecimal.valueOf(NumberUtils.round(float1)).compareTo(BigDecimal.valueOf(NumberUtils.round(float2))) != 0);
    }

    public static boolean isNotEqual(Double double1, Double double2) {
        return (null == double1 && null != double2) || (null != double1 && null == double2) || (null != double1 && null != double2
                && BigDecimal.valueOf(NumberUtils.round(double1)).compareTo(BigDecimal.valueOf(NumberUtils.round(double2))) != 0);
    }

    public static boolean isNotEqual(Boolean boolean1, Boolean boolean2) {
        return (null == boolean1 && null != boolean2) || (null != boolean1 && null == boolean2) || (null != boolean1 && null != boolean2 && boolean1 != boolean2);
    }

    public static boolean in(String str, String... strs) {
        if (null != str && null != strs) {
            for (String string : strs) {
                if (isEqualIgnoreCase(str, string)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean notIn(String str, String... strs) {
        return !in(str, strs);
    }

    public static String wrapText(String text, int len) {
        StringBuffer sb = new StringBuffer("");
        // return empty string for null text
        if (!isNotEmpty(text)) {
            return "";
        }
        // return text if len is zero or less
        if (len <= 0) {
            return text;
        }
        // return text if less than length
        if (text.length() <= len) {
            return text;
        }
        char[] chars = text.toCharArray();
        Vector lines = new Vector();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);
            if (chars[i] == ' ') {
                if ((line.length() + word.length()) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }

                line.append(word);
                word.delete(0, word.length());
            }
        }
        // handle any extra chars in current word
        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }
        // handle extra line
        if (line.length() > 0) {
            lines.add(line.toString());
        }

        String[] ret = new String[lines.size()];
        int c = 0; // counter
        for (Enumeration e = lines.elements(); e.hasMoreElements(); c++) {
            ret[c] = (String) e.nextElement();
            sb.append(ret[c] + "<br>");
        }
        return sb.toString().substring(0, sb.length() - 4);
    }

    public static String getFileSize(File f) throws Exception {
        if (null != f) {
            Long fileSize = f.length();
            if (Math.round((double) fileSize / (1024 * 1024)) > 0) {
                return NumberUtils.formatNumber((double) fileSize / (1024 * 1024), "0.00") + " MB";
            } else {
                return Math.round((double) fileSize / 1024) + " KB";
            }
        } else {
            return "0 KB";
        }
    }

    public static List<String> splitString(String string) {
        List<String> strings = new ArrayList<String>();
        Pattern wrapText = Pattern.compile(".{0,33}(?:\\S(?:-| |$)|$)");
        Matcher matcher = wrapText.matcher(string);
        while (matcher.find()) {
            if (CommonUtils.isNotEmpty(matcher.group())) {
                strings.add(matcher.group());
            }
        }
        return strings;
    }

    public static List<String> splitString(String string, String regex) {
        List<String> list = new ArrayList<String>();
        String[] strs = string.split(regex);
        for(String str : strs){
            if(CommonUtils.isNotEmpty(str)){
                list.add(str);
            }
        }
        return list;
    }
    public static List<String> splitForContainerString(String string, String regex) {
        List<String> stringList = new ArrayList<String>();
        Pattern wrapText = Pattern.compile(regex);
        Matcher matcher = wrapText.matcher(string);
        while (matcher.find()) {
            if (CommonUtils.isNotEmpty(matcher.group())) {
                stringList.add(matcher.group());
            }
        }
        return stringList;
    }

    public static String splitString(String reference, int limit) throws Exception {
        String string = "";
        if (null != reference) {
            int beginIndex = 0;
            int endIndex = limit;
            while (reference.length() > endIndex) {
                string += reference.subSequence(beginIndex, endIndex).toString();
                beginIndex = endIndex;
                endIndex += limit;
            }
            string += reference.substring(beginIndex);
        }
        return string;
    }

    public static String trimTrailingZeros(String data) throws Exception {
        if (isNotEmpty(data)) {
            String regex = "0*$";
            String replacement = "";
            data = data.replaceAll(regex, replacement);
            if (data.endsWith(".")) {
                data = data.substring(0, data.length() - 1);
            }
        }
        return data;
    }

    public static String removeTrailingZeros(String data) throws Exception {
        if (isNotEmpty(data)) {
            char[] chars = data.toCharArray();
            int length, index;
            length = data.length();
            index = length - 1;
            for (; index >= 0; index--) {
                if (chars[index] != '0') {
                    break;
                }
            }
            data = (index == length - 1) ? data : data.substring(0, index + 1);
            if (data.endsWith(".")) {
                data = data.substring(0, data.length() - 1);
            }
        }
        return data;
    }

    public static String capitalize(String string) {
        string = WordUtils.capitalize(string.toLowerCase());
        if (StringUtils.contains(string, ".")) {
            String subStrBefore = StringUtils.substringBefore(string, ".") + ".";
            String subStrAfter = WordUtils.capitalize(StringUtils.substringAfter(string, "."));
            return subStrBefore + subStrAfter;
        } else if (StringUtils.contains(string, "/")) {
            String subStrBefore = StringUtils.substringBefore(string, "/") + "/";
            String subStrAfter = WordUtils.capitalize(StringUtils.substringAfter(string, "/"));
            return subStrBefore + subStrAfter;
        }
        return string;
    }

    public static boolean matches(String text, String regex) {
        return Pattern.compile(regex).matcher(text).find();
    }

    public static boolean notMatches(String text, String regex) {
        return !matches(text, regex);
    }

    public String escapeXml(String str) {
        str = replaceString(str, "&", "&amp;");
        str = replaceString(str, "<", "&lt;");
        str = replaceString(str, ">", "&gt;");
        str = replaceString(str, "\"", "&quot;");
        str = replaceString(str, "'", "&apos;");
        return str;
    }

    public String escapeGtXml(String str) {
        escapeXml(str);
        str.replaceAll(str, str);
        return str;
    }

    public String replaceString(String text, String repl, String with) {
        return replaceStringTo(text, repl, with, -1);
    }

    public String replaceStringTo(String text, String repl, String with, int max) {
        if (text == null) {
            return null;
        }

        StringBuffer buffer = new StringBuffer(text.length());
        int start = 0;
        int end = 0;
        while ((end = text.indexOf(repl, start)) != -1) {
            buffer.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }
        buffer.append(text.substring(start));

        return buffer.toString();
    }
    public static boolean isExcludeEdiCharacter(String validationField,String ecludeCharcter)throws Exception{
        ecludeCharcter=ecludeCharcter.replace("[","\\[").replace("]","\\]").replace("\\", "\\\\");
         Pattern p = Pattern.compile("["+ecludeCharcter+"]");
         Matcher m = p.matcher(validationField);
         return m.find();
    }
}
