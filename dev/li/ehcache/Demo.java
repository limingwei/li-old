package li.ehcache;

import li.ioc.Ioc;

public class Demo {
    static Hello hello = Ioc.get(Hello.class);

    public static void main(String[] args) throws InterruptedException {
        System.out.println(hello.say("123"));
        System.out.println(hello.say("123"));
        System.out.println(hello.say("123"));
        System.out.println(hello.say("456"));
        System.out.println(hello.say("456"));
        System.out.println(hello.say("789"));

        for (int i = 0; true; i++) {
            System.out.println(i + "\t" + hello.say("123"));
            Thread.sleep(2000);
        }
    }
}