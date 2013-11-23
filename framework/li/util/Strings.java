package li.util;

public class Strings {
    /**
     * 处理log信息
     */
    public static String replace(Object input, char placeholder, Object... replacement) {
        if (null == replacement || replacement.length < 1 || !input.toString().contains("" + placeholder)) {
            return input + "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        char[] chars = null == input ? new char[0] : input.toString().toCharArray();
        int arg_index = 0;
        for (int i = 0; i < chars.length; i++) {
            stringBuffer.append((arg_index < replacement.length && chars[i] == placeholder) ? replacement[arg_index++] : chars[i]);
        }
        return stringBuffer.toString();
    }
}
