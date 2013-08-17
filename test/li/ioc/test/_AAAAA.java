package li.ioc.test;

import li.annotation.Bean;
import li.annotation.Inject;
import li.util.Log;

@Bean("beanA")
public class _AAAAA {
    private static final Log log = Log.init();

    @Inject
    public _AAAAA aaaaa;

    @Inject
    public _BBBBB bbbbb;

    @Inject("123")
    public int num;

    public String sayHi(String name) {
        log.debug("方法被执行 \t name=?", name);
        return "本来的返回值 \t name=" + name;
    }
}