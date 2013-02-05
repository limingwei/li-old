package li.ehcache;

import li.annotation.Aop;
import li.annotation.Bean;

@Bean
public class Hello {
    @Aop(CacheFilter.class)
    public String say(String name) {
        System.out.println("执行方法");
        return "hello," + name;
    }
}
