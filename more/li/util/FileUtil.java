package li.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;

/**
 * 文件工具类
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2013-02-04)
 */
public class FileUtil extends Files {
    /**
     * 读取一个文本文件并返回其文本内容
     */
    public static String read(File file) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception in li.util.Files.read(File)", e);
        }
        return stringBuffer.toString();
    }

    /**
     * 把内容写入一个文本文件
     */
    public static void write(File file, String content) {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(content));
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.close();
            bufferedReader.close();
        } catch (Exception e) {
            throw new RuntimeException("Exception in li.util.Files.write(File, String)", e);
        }
    }
}
