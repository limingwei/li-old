package li.mvc.view.httl;

public class Methods {
    public static String left(Object self, int len) {
        return left(self + "", len);
    }

    public static String left(String text, int len) {
        if (text.length() <= len) {
            return text;
        }
        return text.substring(0, len);
    }

    public static String right(Object self, int len) {
        return right(self + "", len);
    }

    public static String right(String text, int len) {
        if (text.length() <= len) {
            return text;
        }
        return text.substring(text.length() - len, text.length());
    }
}