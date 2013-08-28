package li.mvc;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import li.test.ActionTest;

import org.junit.Before;
import org.junit.Test;

public class ContextTest extends ActionTest {
    @Before
    public void before() {
        Context.init(request, response, null);
    }

    @Test
    public void getRequest() {
        assertNotNull(Context.getRequest());
    }

    @Test
    public void getArray() {
        request.setParameter("ids", new String[] { "1", "2", "3" });
        System.out.println(Context.getArray(Integer.class, "ids"));
    }

    @Test
    public void getResponse() {
        assertNotNull(Context.getResponse());
    }

    @Test
    public void httl() {
        Ctx.setRequest("name", "limingwei");
        Ctx.setRequest("date", new Date());
        Ctx.httl("WEB-INF/view_ht/httl.httl");
    }

    @Test
    public void freemarker() {
        Ctx.setRequest("name", "limingwei");
        Ctx.setRequest("date", new Date());
        Ctx.freemarker("WEB-INF/view_fm/fm.htm");
    }

    @Test
    public void velocity() {
        Ctx.setRequest("name", "limingwei");
        Ctx.setRequest("date", new Date());
        Ctx.velocity("WEB-INF/view_vl/vl.htm");
    }

    @Test
    public void beetl() {
        Ctx.setRequest("name", "limingwei");
        Ctx.setRequest("date", new Date());
        Ctx.beetl("WEB-INF/view_bt/bt.htm");
    }
}