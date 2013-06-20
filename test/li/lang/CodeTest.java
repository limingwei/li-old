package li.lang;

import java.io.File;

import li.lang.Code.CodeStatisticsResult;

import org.junit.Test;

public class CodeTest {
    @Test
    public void test() {
        CodeStatisticsResult result = Code.countingCode(new File("D:/workspace/li/framework/"), "java", true, null);
        System.out.println(result);
    }
}