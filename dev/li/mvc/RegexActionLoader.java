package li.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import li.annotation.Arg;
import li.ioc.IocContext;
import li.model.Action;
import li.model.Bean;
import li.util.Reflect;
import li.util.Verify;

public class RegexActionLoader implements ActionLoader {
    private String typeRegex = "";

    private String methodRegex = "";

    public Map<String, Action> getActions() {
        Map<String, Action> actionMap = new HashMap<String, Action>();// 保存Action
        List<Bean> beans = IocContext.getInstance().getBeans();
        for (Bean bean : beans) {
            if (Verify.regex(bean.type.getName(), typeRegex)) {
                Method[] methods = bean.type.getDeclaredMethods();
                for (Method method : methods) {
                    if (Verify.regex(method.getName(), methodRegex)) {
                        Action action = new Action();
                        action.actionInstance = bean.instance;
                        action.actionMethod = method;
                        action.argTypes = method.getParameterTypes();
                        action.argNames = Reflect.argNames(method);
                        action.argAnnotations = Reflect.argAnnotations(method, Arg.class);
                        action.path = this.getPath(bean.type, method);
                        action.httpMethod = this.getHttpMethod(method);
                        actionMap.put(action.path + "#" + action.httpMethod, action);
                    }
                }
            }
        }
        return actionMap;
    }

    private String getPath(Class<?> type, Method method) {
        return type.getSimpleName().toLowerCase().replace("action", "") + "_" + method.getName();
    }

    private String getHttpMethod(Method method) {
        if (method.getName().contains("save") || method.getName().contains("update")) {
            return RequestMethod.POST;
        } else {
            return RequestMethod.GET;
        }
    }
}