package li;

import java.io.File;

import li.aop._Account;
import li.ioc.Ioc;
import li.lang.Code;
import li.lang.Code.CodeAnalysisConf;
import net.sf.cglib.core.DebuggingClassWriter;

import org.junit.Test;

public class MoreTest {
    @Test
    public void codeAnalysis() {
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

    @Test
    public void debugClassWriter() {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\Users\\li\\Desktop\\classes");
        Ioc.get("");
    }

    @Test
    public void multiThreadDao() throws InterruptedException {
        final _Account accountDao = Ioc.get(_Account.class);

        for (int i = 0; i < 1000; i++) {// 开启1000个线程
            new Thread() {
                public void run() {
                    for (; true;) {// 每个线程无限循环
                        accountDao.update("SET status=1 WHERE id=11");// 这是一个带Aop带Trans的Dao方法
                    }
                }
            }.start();
            Thread.sleep(1000);// 每隔1秒增加一个线程
        }
    }
}