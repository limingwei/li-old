package li.people.action;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import li.annotation.Aop;
import li.annotation.Arg;
import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.annotation.Trans;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.mvc.Context;
import li.mvc.Ctx;
import li.mvc.adapter.FileMeta;
import li.mvc.adapter.UploadAdapter;
import li.mvc.view.BeetlView;
import li.mvc.view.HttlView;
import li.mvc.view.VelocityView;
import li.people.record.Account;

@Bean
public class DemoAction extends AbstractAction {
    @Inject
    Account accountDao;

    @At("1.do1")
    public void do1_1() {
        write("1.do1 sucess");
    }

    @At("1.do2")
    public void do2_1() {
        write("1.do2 sucess");
    }

    @At("1.do3")
    public void do3_1() {
        write("1.do3 sucess");
    }

    @At("1.do4")
    public void do4_1() {
        write("1.do4 sucess");
    }

    @At("injar.htm")
    public void classInJar() throws Exception {
        String path = getClass().getResource("/").toURI().getPath();
        File root = new File(path).getParentFile().getParentFile();

        System.err.println(root + "\t" + root.getCanonicalPath());

        write(root + "\t" + root.getCanonicalPath());
    }

    @At("performance_test_action.htm")
    public void performanceTestAction() {
        setRequest("accounts", accountDao.list(new Page().count(false)));
        freemarker("/WEB-INF/view_performance_test/performance_test.htm");
    }

    @At("upload_adapter.do")
    @Aop({ UploadAdapter.class, HttlView.class })
    public String upload(FileMeta[] fileMetas, HttpServletRequest request, String name_1, String[] name_2) {
        if (null != fileMetas) {
            for (FileMeta fileMeta : fileMetas) {
                System.out.println(fileMeta);
            }
        }
        System.out.println(request.getContentType());
        System.out.println("name_1\t" + name_1);
        for (String string : name_2) {
            System.out.println("name_2\t" + string);
        }
        System.out.println(Context.getRequest());
        return "/WEB-INF/view_ht/httl_view.htm";
    }

    @At("httl_view.htm")
    @Aop(HttlView.class)
    public String testHttlView() {
        return "/WEB-INF/view_ht/httl_view.htm";
    }

    @At("beetl_view.htm")
    @Aop(BeetlView.class)
    public String testBeetlView() {
        return "/WEB-INF/view_bt/beetl_view.htm";
    }

    @At("velocity_view.htm")
    @Aop(VelocityView.class)
    public String testVelocityView() {
        return "/WEB-INF/view_vl/velocity_view.htm";
    }

    @At("smarty4j.htm")
    public String smarty4j() {
        return "smarty4j:smarty4j.htm";
    }

    @At("httl.do")
    public void httl() {
        setRequest("date", new Date());
        Ctx.httl("/WEB-INF/view_ht/httl.httl");
    }

    /**
     * 可以使用继承AbstractAction和Context中静态方法，两者提供一一对应的方法
     */
    @At("testcaa.htm")
    public void testContextAndAbstractAction() {
        super.write("用li.mvc.AbstractAction.write(String)展示视图");
        Context.write("用li.mvc.Context.write(String)展示视图");
    }

    /**
     * 返回状态码
     */
    @At("404.htm")
    public void test404() {
        getResponse().setStatus(404);
    }

    /**
     * 返回freemarker视图
     */
    @At("fm.do")
    @Trans
    public void testFreemarker() {
        setRequest("str1", "床前明月光 testFreemarker");
        Page page = new Page();
        setSession("pg", page);
        setRequest("accounts", accountDao.list(page, "SELECT * FROM t_account LIMIT 5"));
        freemarker("WEB-INF/view_fm/fm.htm");
    }

    /**
     * 返回freemarker视图
     */
    @At("fm2.do")
    public void testFreemarker2() {
        setRequest("str1", "床前明月光 testFreemarker").setRequest("accounts", accountDao.list(new Page(), "SELECT * FROM t_account LIMIT 5"));
        freemarker("WEB-INF/view_fm/fm.htm");
    }

    /**
     * 返回velocity视图
     */
    @At("vl.do")
    public void testVelocity() {
        setRequest("str1", "床前明月光 testVelocity");
        Page page = new Page();
        setSession("page", page);
        setRequest("accounts", accountDao.list(page, "SELECT * FROM t_account LIMIT 5"));
        velocity("WEB-INF/view_vl/vl.htm");
    }

    /**
     * 返回velocity视图
     */
    @At("vl2.do")
    public void testVelocity2() {
        setRequest("str1", "床前明月光 testVelocity").setRequest("accounts", accountDao.list(new Page(), "SELECT * FROM t_account LIMIT 5"));
        velocity("WEB-INF/view_vl/vl.htm");
    }

    /**
     * 返回beetl视图
     */
    @At("bt.do")
    public void testBeetl() {
        setRequest("str1", "床前明月光 testBeetl").setRequest("accounts", accountDao.list(new Page(), "SELECT * FROM t_account LIMIT 5"));
        beetl("WEB-INF/view_bt/bt.htm");
    }

    /**
     * 返回jsp视图
     */
    @At("testjsp.do")
    public void testJSP() {
        setRequest("str1", "床前明月光 testJSP").setRequest("accounts", accountDao.list(new Page(), "SELECT * FROM t_account LIMIT 5"));
        forward("WEB-INF/view_jsp/test.jsp");
    }

    /**
     * 返回json
     */
    @At("json.htm")
    public void testJson() {
        write("{ \"firstName\":\"John\" , \"lastName\":\"Doe中文试试看\" }");
    }

    /**
     * 返回xml
     */
    @At("xml.htm")
    public void testXml() {
        write("<note><heading>Reminder</heading><body>中文的内容</body></note>");
    }

    /**
     * 返回文本
     */
    @At("text.htm")
    public void testText() {
        write("床前明月光，ABCDE");
    }

    /**
     * 测试不匹配的视图类型
     */
    @At("testViewType.htm")
    public String testViewType() {
        return "视图类型:视图地址";
    }

    /**
     * 测试HTTP Method
     */
    @At(value = "testPost.htm", method = POST)
    public void testPost() {
        write("POST");
    }

    /**
     * 测试HTTP Method
     */
    @At(value = "testGet.htm", method = GET)
    public void testGet() {
        write("GET");
    }

    /**
     * 测试参数适配
     */
    @At("test_dev_filter.htm")
    public String testAtPar(HttpServletRequest request, HttpServletResponse response, int int1, boolean bol, String str1, Integer[] id, @Arg("int2") Integer int22, @Arg("str2") String str22, Account account1, @Arg("account2.") Account account22) {

        write(request.toString());
        write(response.toString());
        write("int1=" + int1);
        write("str1=" + str1);
        write("int22=" + int22);
        write("str22=" + str22);
        write("account1=" + account1);
        write("account1=" + account1.get("username"));
        write("account22=" + account22);
        write("account22=" + (null == account22 ? "account22 is null" : account22.get("username")));

        write("bol=" + bol);

        for (Integer integer : id) {
            write(integer);
        }
        return view("write:测试成功");
    }

    /**
     * 测试redirect
     */
    @At("test_3.htm")
    public void test3() {
        redirect("http://g.cn");
    }

    /**
     * 测试AbstractAction
     */
    @At("test_abs_action.htm")
    public void testAbstractAction() {
        write(getRequest().toString());
        write(getResponse().toString());
        write("int1=" + getParameter("int1"));
        write("str1=" + getParameter("str1"));
        write("int22=" + getParameter("int2"));
        write("str22=" + getParameter("str2"));
        write("account1=" + get(Account.class, "account1."));
        write("account1=" + get(Account.class, "account1.").get("username"));
        write("account22=" + get(Account.class, "account2."));
        write("account22=" + (null == get(Account.class, "account2.") ? "account22 is null" : get(Account.class, "account2.").get("username")));

        for (Integer integer : getArray(int.class, "id")) {
            write(integer);
        }
        write("测试成功");
    }

    /**
     * 各种不同的视图方法
     */
    @At("test_all.htm")
    public String testAll(HttpServletResponse response) throws Exception {
        response.getWriter().print("Response write 测试成功");
        super.write("AbstractAction write 测试成功");
        Context.write("Ctx write 测试成功");
        Context.write("Ctx write 第二次 测试成功");

        Context.view("write:" + "Ctx view 测试成功");
        super.view("write:" + "AbstractAction view 测试成功");

        return "write:" + "return text 测试成功";
    }

    /**
     * action方法参数适配
     */
    @At("test_get_param.htm")
    public String testGetParam(Integer num1, @Arg("num2") Integer num2) {
        return "";
    }

    /**
     * action方法默认路径
     */
    @At
    public void test_action_path_default_value() {
        write(getRequest().getRequestURI());
    }

    @At(value = { "1.do", "2.do" }, method = { GET, POST })
    public void test_action_many_annotation() {
        write(getRequest().getServletPath() + "\n" + getRequest().getMethod());
    }
}