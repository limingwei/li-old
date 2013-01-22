package li.people;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import li.ioc.Ioc;
import li.mvc.Context;
import li.people.record.Account;
import li.people.record.Resource;
import li.people.record.Role;
import li.people.record.RoleResource;

public class AuthFilter implements Filter {
    private Resource resourceDao;
    private RoleResource roleResourceDao;
    private Role roleDao;

    public void init(FilterConfig config) throws ServletException {
        resourceDao = Ioc.get(Resource.class);
        roleResourceDao = Ioc.get(RoleResource.class);
        roleDao = Ioc.get(Role.class);
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getServletPath().replace('/', ' ').trim();
        if (controled(path)) {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            if (null == account) {
                response.setStatus(302);// goto login
            } else if (allowed(path, account)) {
                chain.doFilter(request, response);
            } else {
                Context.init(request, response, null);
                Context.view("deny");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private Boolean controled(String path) {
        return 0 < resourceDao.count("WHERE name=?", path);
    }

    private Boolean allowed(String path, Account account) {
        Integer id = account.get(Integer.class, "id");
        String sql1 = "WHERE name='superadmin' AND id=(SELECT role_id FROM t_account WHERE id=?)";
        String sql2 = "WHERE resource_id=(SELECT id FROM t_resource WHERE name=?) AND role_id=(SELECT role_id FROM t_account WHERE id=?)";

        return 0 < roleDao.count(sql1, id) || 0 < roleResourceDao.count(sql2, path, id);
    }

    public void destroy() {
        System.out.println("enclosing_type.enclosing_method()");
    }
}