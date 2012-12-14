package li.people;

import java.util.List;

import li.annotation.Bean;
import li.aop.AopChain;
import li.aop.AopFilter;
import li.mvc.Context;

@Bean
public class AuthFilter implements AopFilter {
    public void doFilter(AopChain chain) {
        String path = Context.getRequest().getServletPath();
        List<String> permissions = (List<String>) Context.getSession().getAttribute("permissions");
        if (permissions.contains(path)) {
            chain.doFilter();
        } else {
            Context.view("deny");
        }
    }
}