package li.util;

public class StringUtil {
    public static String getEncoding(String str) {
        String[] encodes = { "GB2312", "ISO-8859-1", "UTF-8", "GBK" };
        for (String encode : encodes) {
            try {
                if (str.equals(new String(str.getBytes(encode), encode))) {
                    return encode;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "unknown";
    }
}
