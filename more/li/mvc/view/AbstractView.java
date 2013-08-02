package li.mvc.view;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import li.aop.AopChain;
import li.aop.AopFilter;
import li.mvc.Ctx;

/**
 * 基于Aop的视图基类
 * 
 * @author : 明伟 
 * @date : 2013年7月19日 下午3:09:30
 * @version 1.0 
 */
public abstract class AbstractView implements AopFilter {
    public void doFilter(AopChain chain) {
        try {
            chain.doFilter();
            render((String) chain.getResult(), Ctx.getResponse(), Ctx.getAttributes());
            chain.setResult("~!@#DONE");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void render(String tempPath, HttpServletResponse response, Map<String, Object> map) throws Exception;
}