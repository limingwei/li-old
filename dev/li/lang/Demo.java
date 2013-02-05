package li.lang;

import java.io.File;

import li.lang.Code.CodeAnalysisConf;

public class Demo {
    public static void main(String[] args) {
        String suffix = "java";
        boolean countSubFolder = true;
        CodeAnalysisConf conf = null;

        System.out.println(Code.countingCode(new File("framework"), suffix, countSubFolder, conf));
        System.out.println();
        System.out.println(Code.countingCode(new File("more"), suffix, countSubFolder, conf));
        System.out.println();
        System.out.println(Code.countingCode(new File("dev"), suffix, countSubFolder, conf));
        System.out.println();
        System.out.println(Code.countingCode(new File("test"), suffix, countSubFolder, conf));
        System.out.println();
        System.out.println(Code.countingCode(new File("demo"), suffix, countSubFolder, conf));
    }
}