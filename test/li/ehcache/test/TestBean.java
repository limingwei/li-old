package li.ehcache.test;

import li.annotation.Aop;
import li.annotation.Bean;
import li.ehcache.Cache;
import li.ehcache.CacheFilter;
import li.ehcache.Clear;

@Bean
public class TestBean {
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
