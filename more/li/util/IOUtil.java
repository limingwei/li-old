package li.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

/**
 * IO工具类
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.1 (2013-02-04)
 */
public class IOUtil {
    /**
     * 从一个文件读取类容并返回
     */
    public static String read(File file) {
        try {
            return read(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * 从一个字节输入流读取类容并返回
     */
    public static String read(InputStream inputStream) {
        return read(new InputStreamReader(inputStream));
    }

    /**
     * 从一个字符输入流读取类容并返回
     */
    public static String read(Reader reader) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
        return stringBuffer.toString();
    }

    /**
     * 把内容写入一个文本文件
     */
    public static void write(File file, String content) {
        try {
            write(new FileWriter(file), content);
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * 把内容写到一个字节输出流
     */
    public static void write(OutputStream outputStream, String content) {
        write(new OutputStreamWriter(outputStream), content);
    }

    /**
     * 把内容写入一个字符输出流
     */
    public static void write(Writer writer, String content) {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(content));
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.close();
            bufferedReader.close();
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }
}
