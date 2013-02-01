package li.aop;

import li.annotation.Bean;
import li.util.Log;

@Bean
public class _LogFilter implements AopFilter {
    private static final Log log = Log.init();

    public void doFilter(AopChain chain) {
        log.info("? ?", chain.getTarget(), chain.getMethod());
        chain.doFilter();
    }
}