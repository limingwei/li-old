package li.mvc.auth;

import javax.servlet.http.HttpServletRequest;

public class DemoAuthFilter extends AuthFilter {
    public Boolean needAuth(HttpServletRequest request) {
        System.out.println("当前资源是否需要权限控制");
        return true;
    }

    public Boolean accessAuth(HttpServletRequest request) {
        System.out.println("检查是否有权访问这个资源");
        return false;
    }

    public void failAuth() {
        System.out.println("你无权访问这个资源");
    }
}