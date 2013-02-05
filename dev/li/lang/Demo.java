package li.lang;

import java.io.File;

import li.lang.Code.CodeAnalysisConf;

public class Demo {
    public static void main(String[] args) {
        String suffix = "java";
        boolean countSubFolder = true;
        CodeAnalysisConf conf = null;

        System.out.println(Code.countingCode(new File("D:\\workspace\\li\\framework"), suffix, countSubFolder, conf));
        System.out.println(Code.countingCode(new File("D:\\workspace\\li\\more"), suffix, countSubFolder, conf));
        System.out.println(Code.countingCode(new File("D:\\workspace\\li\\dev"), suffix, countSubFolder, conf));
        System.out.println(Code.countingCode(new File("D:\\workspace\\li\\test"), suffix, countSubFolder, conf));
        System.out.println(Code.countingCode(new File("D:\\workspace\\li\\demo"), suffix, countSubFolder, conf));
    }
}