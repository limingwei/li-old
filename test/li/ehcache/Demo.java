package li.ehcache;

import li.ioc.Ioc;

public class Demo {
    static Hello hello = Ioc.get(Hello.class);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            System.out.println(i + "\t" + hello.say("参数相同"));
            Thread.sleep(500);
            if (i % 2 == 0) {
                hello.clear();
                // CacheUtil.removeCache("cache_name_1");
            }
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(i + "\t" + hello.say(i + "_参数不同"));
            Thread.sleep(500);
        }
    }
}