package demo.action;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import li.annotation.Arg;
import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.annotation.Trans;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.mvc.Context;
import demo.record.Account;
import demo.record.Member;

@Bean
public class DemoAction extends AbstractAction {
    @Inject
    Member memberDao;

    @Inject
    Account accountDao;

    /**
     * 可以使用继承AbstractAction和Context中静态方法，两者提供一一对应的方法
     */
    @At("testcaa")
    public void testContextAndAbstractAction() {
        super.write("用li.mvc.AbstractAction.write(String)展示视图");
        Context.write("用li.mvc.Context.write(String)展示视图");
    }

    /**
     * 用正则表达式配置Action路径
     */
    @At({ "thread-([0-9]{1})-([0-9]*).htm" })
    public void test1() {
        write("test1\t@At({\"thread-([0-9]{1})-([0-9]*).htm\"})");
        for (String param : pathParams()) {
            write(param);
        }
    }

    /**
     * 用正则表达式配置Action路径
     */
    @At({ "thread-([0-9]*)-([a-z]*).htm" })
    public void test2() {
        write("test2\t@At({\"thread-([0-9]*)-([a-z]*).htm\"})");
        for (String param : pathParams()) {
            write(param);
        }
    }

    /**
     * 用正则表达式配置Action路径
     */
    @At({ "thread-([0-9]*)-([A-Z]*).htm" })
    public void test31() {
        write("test3\t@At({\"thread-([0-9]*)-([A-Z]*).htm\"})");
        for (String param : pathParams()) {
            write(param);
        }
    }

    /**
     * 用正则表达式配置Action路径
     */
    @At({ "thread-([0-9]{2})-([0-9]*).htm" })
    public void test4() {
        write(getRequest().getRequestURI() + "<br/>");
        write(getRequest().getRequestURL().toString() + "<br/>");
        write(getRequest().getServletPath() + "<br/>");
        write("test4\t@At({\"thread-([0-9]{2})-([0-9]*).htm\"})");
        System.out.println(getRequest().getRequestURL());
        for (String param : pathParams()) {
            write(param);
        }
    }

    /**
     * 用正则表达式配置Action路径
     */
    @At({ "thread-(.*).htm" })
    public void test5() {
        write("test5\t@At({\"thread-(.*).htm\"})");
        for (String param : pathParams()) {
            write(param);
        }
    }

    /**
     * 返回状态码
     */
    @At("404")
    public void test404() {
        getResponse().setStatus(404);
    }

    /**
     * 这个跳过,研究文件上传的
     */
    @At(value = "upload", method = POST)
    public void testUpload() {
        DataOutputStream dataOutputStream = null;
        try {
            int len;
            byte[] buf = new byte[4048];
            String lines[] = new String[3];
            for (int i = 0; (len = getRequest().getInputStream().readLine(buf, 0, buf.length)) != -1; i++) {
                if (i < 3) {
                    lines[i] = new String(buf, 0, len - 1);
                } else if (i < 4) {
                    String uploadPath = "D:\\Users\\明伟\\Desktop\\";

                    String uploadFileName = lines[1].substring(lines[1].indexOf("filename") + 10, lines[1].length() - 2);
                    uploadFileName = new String(uploadFileName.getBytes(), "UTF-8");
                    String saveFileName = uploadPath + System.currentTimeMillis() + "_" + getSession().getId() + "_" + uploadFileName.replaceAll("[:]{0,1}\\\\", "_");

                    dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveFileName)));
                } else if (!new String(buf, 0, len - 1).contains(lines[0].substring(2, lines[0].length() - 2))) {
                    dataOutputStream.write(buf, 0, len);
                }
            }
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回freemarker视图
     */
    @At("fm")
    @Trans
    public void testFreemarker() {
        setRequest("str1", "床前明月光 testFreemarker");
        Page page = new Page();
        setSession("page", page);
        setRequest("accounts", memberDao.list(page, "select * from t_account"));
        freemarker("WEB-INF/view_fm/fm.htm");
    }

    /**
     * 返回freemarker视图
     */
    @At("fm2")
    public void testFreemarker2() {
        setRequest("str1", "床前明月光 testFreemarker").setRequest("accounts", memberDao.list(new Page(), "select * from t_account"));
        freemarker("WEB-INF/view_fm/fm.htm");
    }

    /**
     * 返回velocity视图
     */
    @At("vl")
    public void testVelocity() {
        setRequest("str1", "床前明月光 testVelocity");
        Page page = new Page();
        setSession("page", page);
        setRequest("accounts", memberDao.list(page, "select * from t_account"));
        velocity("WEB-INF/view_vl/vl.htm");
    }

    /**
     * 返回velocity视图
     */
    @At("vl2")
    public void testVelocity2() {
        setRequest("str1", "床前明月光 testVelocity").setRequest("accounts", memberDao.list(new Page(), "select * from t_account"));
        velocity("WEB-INF/view_vl/vl.htm");
    }

    /**
     * 返回beetl视图
     */
    @At("bt")
    public void testBeetl() {
        setRequest("str1", "床前明月光 testBeetl").setRequest("accounts", accountDao.list(new Page(), "select * from t_account"));
        beetl("WEB-INF/view_bt/bt.htm");
    }

    /**
     * 返回jsp视图
     */
    @At("testjsp")
    public void testJSP() {
        setRequest("str1", "床前明月光 testJSP").setRequest("accounts", accountDao.list(new Page(), "select * from t_account"));
        forward("WEB-INF/view_jsp/test.jsp");
    }

    /**
     * 返回json
     */
    @At("json")
    public void testJson() {
        write("{ \"firstName\":\"John\" , \"lastName\":\"Doe中文试试看\" }");
    }

    /**
     * 返回xml
     */
    @At("xml")
    public void testXml() {
        write("<note><heading>Reminder</heading><body>中文的内容</body></note>");
    }

    /**
     * 返回文本
     */
    @At("text")
    public void testText() {
        write("床前明月光，ABCDE");
    }

    /**
     * 测试不匹配的视图类型
     */
    @At("testViewType")
    public String testViewType() {
        return "视图类型:视图地址";
    }

    /**
     * 测试HTTP Method
     */
    @At(value = "testPost", method = POST)
    public void testPost() {
        write("POST");
    }

    /**
     * 测试HTTP Method
     */
    @At(value = "testGet", method = GET)
    public void testGet() {
        write("GET");
    }

    /**
     * 测试HTTP Method
     */
    @At(value = "testAny")
    public void testAny() {
        write("ANY");
    }

    /**
     * 测试参数适配
     */
    @At("test_dev_filter")
    public String testAtPar(HttpServletRequest request, HttpServletResponse response, int int1, boolean bol, String str1, Integer[] id, @Arg("int2") Integer int22, @Arg("str2") String str22, Account account1, @Arg("account2.") Account account22) {
        System.out.println(request.toString());
        System.out.println(response.toString());
        System.out.println("int1=" + int1);
        System.out.println("str1=" + str1);
        System.out.println("int22=" + int22);
        System.out.println("str22=" + str22);
        System.out.println("account1=" + account1);
        System.out.println("account1=" + account1.get("username"));
        System.out.println("account22=" + account22);
        System.out.println("account22=" + (null == account22 ? "account22 is null" : account22.get("username")));

        System.out.println("bol=" + bol);

        for (Integer integer : id) {
            System.out.println(integer);
        }
        return view("write:测试成功");
    }

    /**
     * 测试redirect
     */
    @At("test_3")
    public void test3() {
        redirect("http://g.cn");
    }

    /**
     * 测试AbstractAction
     */
    @At("test_abs_action")
    public void testAbstractAction() {
        System.out.println(getRequest().toString());
        System.out.println(getResponse().toString());
        System.out.println("int1=" + getParameter("int1"));
        System.out.println("str1=" + getParameter("str1"));
        System.out.println("int22=" + getParameter("int2"));
        System.out.println("str22=" + getParameter("str2"));
        System.out.println("account1=" + get(Account.class, "account1."));
        System.out.println("account1=" + get(Account.class, "account1.").get("username"));
        System.out.println("account22=" + get(Account.class, "account2."));
        System.out.println("account22=" + (null == get(Account.class, "account2.") ? "account22 is null" : get(Account.class, "account2.").get("username")));

        for (Integer integer : getArray(int.class, "id")) {
            System.out.println(integer);
        }
        write("测试成功");
    }

    /**
     * 测试Context工具类
     */
    @At("test_ctx")
    public void testCtx() {
        Context.write("Ctx 测试成功");
    }

    /**
     * 各种不同的视图方法
     */
    @At("test_all")
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
    @At("test_get_param")
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
}