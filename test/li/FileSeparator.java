package li;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class FileSeparator {
    private static final String CLASS_REGEX = "^.*\\.class$";

    @Test
    public void main() {
        File rootFolder = root();
        List<String> fileList = list(rootFolder, CLASS_REGEX, true);

        for (String classFileName : fileList) {
            System.err.println(classFileName + "\t" + getClassName1(classFileName) + "\t" + getClassName2(classFileName) + "\t" + getClassName3(classFileName));
        }

        System.err.println("System.getProperty = " + System.getProperty("file.separator"));
        System.err.println("File.separator = " + File.separator);
    }

    private static String getClassName1(String classFileName) {
        try {
            return classFileName.split("\\\\classes\\\\")[1].replaceAll("\\\\", ".").replace(".class", "");
        } catch (Exception e) {
            return "这个方法在非 win 下应抛异常 " + e;
        }
    }

    private static String getClassName2(String classFileName) {
        String separator = System.getProperty("file.separator");
        try {
            return classFileName.split("\\" + separator + "classes" + "\\" + separator)[1].replaceAll("\\" + separator, ".").replace(".class", "");
        } catch (Exception e) {
            return "" + e;
        }
    }

    private static String getClassName3(String classFileName) {
        try {
            return classFileName.substring(classFileName.indexOf(File.separator + "classes" + File.separator) + 9, classFileName.length() - 6).replace(File.separatorChar, '.');
        } catch (Exception e) {
            return "" + e;
        }
    }

    private static List<String> list(File file, String regex, Boolean increase) {
        List<String> list = new ArrayList<String>();
        if (file.isFile() && regex(file.getPath(), regex)) {
            list.add(file.getPath());
        } else if (increase && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                list.addAll(list(f, regex, increase)); // 递归调用本方法
            }
        }
        return list;
    }

    private static File root() {
        return new File(Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }

    private static Boolean regex(String input, String regex) {
        return Pattern.compile(regex).matcher(input).find();
    }
}