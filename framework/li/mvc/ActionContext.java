package li.mvc;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import li.annotation.Arg;
import li.annotation.At;
import li.ioc.IocContext;
import li.model.Action;
import li.model.Bean;
import li.util.Log;
import li.util.Reflect;
import li.util.Verify;

/**
 * Action上下文,保存 了所有的Action
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.3 (2012-05-08)
 */
public class ActionContext {
    private static final Log log = Log.init();

    private static ActionContext ACTION_CONTEXT = null;// ActionContext的一个实例,用于单例地得到ActionContext的实例

    private final Map<String, Action> ACTION_MAP = new ConcurrentHashMap<String, Action>();// 保存所有Action

    /**
     * 单例的得到一个ActionContext对象,第一次时候会初始化ActionContext
     */
    public static/* synchronized */ActionContext getInstance() {
        if (ACTION_CONTEXT == null) {
            Long start = System.currentTimeMillis();

            ACTION_CONTEXT = new ActionContext();
            List<Bean> beans = IocContext.getInstance().getBeans();
            for (Bean bean : beans) {
                Method[] methods = bean.type.getDeclaredMethods();
                for (Method method : methods) {
                    At at = method.getAnnotation(At.class);
                    if (null != at) {
                        for (String path : at.value()) {
                            for (String httpMethod : at.method()) {
                                Action action = new Action();
                                action.actionInstance = bean.instance;
                                action.actionMethod = method;
                                action.argTypes = method.getParameterTypes();
                                action.argNames = Reflect.argNames(method);
                                action.argAnnotations = Reflect.argAnnotations(method, Arg.class);
                                action.path = Verify.isEmpty(path) ? "/" + method.getName() : Verify.startWith(path, "/") ? path : "/" + path;// Action请求路径,如果不以斜杠开始会被加上斜杠,如果注解value值为空则直接使用方法名
                                action.httpMethod = httpMethod.toUpperCase(); // HTTP请求类型,这里转换为大写
                                ACTION_CONTEXT.ACTION_MAP.put(action.path + "#" + action.httpMethod, action);

                                log.info("ADD ACTION: @At(value=\"?\",method=\"?\") ?", action.path, action.httpMethod, action.actionMethod);
                            }
                        }
                    }
                }
            }

            log.debug("? actions started up in ?ms", ACTION_CONTEXT.ACTION_MAP.size(), (System.currentTimeMillis() - start));
        }
        return ACTION_CONTEXT;
    }

    /**
     * 根据请求路径从ActionContext中得到对应的Action,或者NULL
     */
    public Action getAction(String path, String method) {
        return ACTION_MAP.get(path + "#" + method);
    }
}