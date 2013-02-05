package li.ehcache;

import li.ioc.Ioc;

public class Demo {
    static Hello hello = Ioc.get(Hello.class);

    public static void main(String[] args) {
        System.out.println(hello.say("123"));
        System.out.println(hello.say("123"));
        System.out.println(hello.say("123"));
        System.out.println(hello.say("456"));
        System.out.println(hello.say("456"));
        System.out.println(hello.say("789"));
    }
}