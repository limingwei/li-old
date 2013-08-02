package li.ioc;

import static org.junit.Assert.assertNotNull;
import li.annotation.Inject;
import li.test.BaseTest;

import org.junit.Test;

public class IocTest extends BaseTest {
    @Inject
    _AAAAA aaaa;

    @Test
    public void getByName() {
        assertNotNull(Ioc.get("beanA"));
    }

    @Test
    public void getByTypeAndName() {
        assertNotNull(Ioc.get(_AAAAA.class, "beanA"));
    }

    @Test
    public void getByType() {
        assertNotNull(Ioc.get(_AAAAA.class));
    }
}