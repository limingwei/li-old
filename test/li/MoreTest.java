package li;

import java.io.File;

import li.aop.test._Account;
import li.ioc.Ioc;
import li.lang.Code;
import net.sf.cglib.core.DebuggingClassWriter;

import org.junit.Test;

public class MoreTest {
    @Test
    public void codeAnalysis() {
        String[] srcs = { "framework", "more", "dev", "test", "demo" };
        for (String src : srcs) {
            System.out.println(Code.countingCode(new File(src), "java", true, null) + "\n");
        }
    }

    @Test
    public void debugClassWriter() {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\Users\\li\\Desktop\\classes");
        Ioc.get("");
    }

    public static void main(String[] args) throws InterruptedException {
        final _Account accountDao = Ioc.get(_Account.class);

        final int threadNum = 100, execNum = 100 * 100;
        long threadSleep = 100, stop = 0, start = System.currentTimeMillis();

        for (int i = 0; i < threadNum; i++) {
            new Thread() {
                public void run() {
                    for (int i = 0; i < execNum; i++) {
                        accountDao.update("SET flag=1 WHERE id=11");// 这是一个带Aop带Trans的Dao方法
                    }
                }
            }.start();
            Thread.sleep(threadSleep);
            stop += threadSleep;
        }

        long now = System.currentTimeMillis();
        System.out.println();
        System.out.println("线程总数\t\t" + threadNum);
        System.out.println("单线程操作数\t" + execNum);
        System.out.println("操作总数\t\t" + (threadNum * execNum));
        System.out.println("总计耗时(毫秒)\t" + (now - start));
        System.out.println("线程中断(毫秒)\t" + stop);
        System.out.println("实际耗时(毫秒)\t" + (now - start - stop));
    }
}