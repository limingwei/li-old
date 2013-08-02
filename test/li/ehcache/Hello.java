package li.ehcache;

import li.annotation.Aop;
import li.annotation.Bean;

@Bean
public class Hello {
    @Aop(CacheFilter.class)
    @Cache("cache_name_1")
    public String say(String name) {
        System.out.println("执行方法");
        return "hello," + name;
    }

    @Aop(CacheFilter.class)
    @Clear("cache_name_1")
    public void clear() {

    }
}
