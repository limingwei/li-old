package li.aop;

import li.annotation.Bean;
import li.util.Log;

@Bean("log")
public class _LogFilter implements AopFilter {
    private static final Log log = Log.init();

    public void doFilter(AopChain chain) {
        log.info("before invoking method= ?", chain.getMethod());
        Long start = System.currentTimeMillis();
        chain.doFilter();
        log.info("invoked in ?ms method= ?", System.currentTimeMillis() - start, chain.getMethod());
    }
}