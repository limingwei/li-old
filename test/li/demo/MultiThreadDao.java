package li.demo;

import li.aop._Account;
import li.ioc.Ioc;

public class MultiThreadDao {
    public static void main(String[] args) throws Exception {
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