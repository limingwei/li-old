package li.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import li.test.BaseTest;

import org.junit.Test;

public class VerifyTest extends BaseTest {
    @Test
    public void basicType() {
        assertTrue(Verify.basicType(int.class));
        assertTrue(Verify.basicType(Integer.class));
        assertFalse(Verify.basicType(this.getClass()));
    }

    @Test
    public void contain() {
        assertTrue(Verify.contain("12345", "123"));
        assertTrue(Verify.contain("ABCDE", "bCd"));
    }

    @Test
    public void endWith() {
        assertTrue(Verify.endWith("abcdefg", "efg"));
    }

    @Test
    public void isEmpty() {
        assertTrue(Verify.isEmpty("   "));
    }

    @Test
    public void startWith() {
        assertTrue(Verify.startWith("abcdefg", "abc"));
    }

    /**
     * {@link li.dao.QueryBuilder#countBySql(String, Object[])}
     */
    @Test
    public void regex1() {
        String REGEX = "COUNT\\(.*\\)";
        String[] tests = { "SELECT COUNT(*) FROM user", "select count(id) from user",//
                "select * from user_count", "SELECT COUNT(1) FROM USER" };
        Boolean[] results = { true, true, false, true };
        for (int i = 0; i < tests.length; i++) {
            assertEquals(results[i], Verify.regex(tests[i].toUpperCase(), REGEX));
        }
    }

    /**
     * {@link li.mvc.ActionContext#getAction(String, String)}
     */
    @Test
    public void regex2() {
        System.out.println("test regex ActionContext#getAction");
    }

    /**
     * {@link li.util.Convert#toType(Class, Object)}
     */
    @Test
    public void regex3() {
        System.out.println("test regex Convert#toType");
    }

    /**
     * {@link li.mvc.Context#write(String)}
     */
    @Test
    public void regex4() {
        System.out.println("test regex Context#write");
    }

    /**
     * {@li.quartz.Quartz}
     */
    @Test
    public void regex5() {
        String QUARTZ_CONFIG_REGEX = "^.*[(config)|(task)]\\.xml$";
        String[] tests = { "config.xml", "task.xml", "test-config.xml", /* "test.xml", */"task.yml" };
        Boolean[] results = { true, true, true, /* false, */false };
        for (int i = 0; i < tests.length; i++) {
            assertEquals(results[i], Verify.regex(tests[i], QUARTZ_CONFIG_REGEX));
        }
    }

    /**
     * {@link li.ioc.XmlIocLoader#getBeans()}
     */
    @Test
    public void regex6() {
        String IOC_CONFIG_REGEX = "^.*[(config)|(ioc)]\\.xml$";
        String[] tests = { "config.xml", "ioc.xml", "test-config.xml", "test.xml", "iloveyou.xml", "ioc.yml" };
        Boolean[] results = { true, true, true, false, false, false };
        for (int i = 0; i < tests.length; i++) {
            assertEquals(results[i], Verify.regex(tests[i], IOC_CONFIG_REGEX));
        }
    }

    /**
     * {@link li.ioc.AnnotationIocLoader#getBeans()}
     */
    @Test
    public void regex7() {
        String CLASS_REGEX = "^.*\\.class$";
        String[] tests = { "User.class" };
        Boolean[] results = { true };
        for (int i = 0; i < tests.length; i++) {
            assertEquals(results[i], Verify.regex(tests[i], CLASS_REGEX));
        }
    }

    /**
     * {@link li.util.Files#load(String)}
     */
    @Test
    public void regex8() {
        String PROPERTIES_REGEX = "^.*\\.properties$";
        String[] tests = { "config.properties" };
        Boolean[] results = { true };
        for (int i = 0; i < tests.length; i++) {
            assertEquals(results[i], Verify.regex(tests[i], PROPERTIES_REGEX));
        }
    }
}