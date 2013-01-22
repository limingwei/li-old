package li.people;

import java.util.List;

import li.annotation.Bean;
import li.aop.AopChain;
import li.aop.AopFilter;
import li.mvc.Context;
import li.people.record.Account;

@Bean
public class AuthFilter implements AopFilter {
    public void doFilter(AopChain chain) {
        String path = Context.getRequest().getServletPath();
        Account account = (Account) Context.getSession().getAttribute("account");
        List<String> resources = (List<String>) Context.getSession().getAttribute("resources");
        if (null == account || null == resources) {
            Context.getResponse().setStatus(302);
        } else {
            if (resources.contains(path)) {
                chain.doFilter();
            } else {
                Context.view("deny");
            }
        }
    }
}