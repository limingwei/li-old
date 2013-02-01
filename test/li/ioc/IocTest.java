package li.ioc;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class IocTest {
    @Test
    public void getByName() {
        assertNotNull(Ioc.get("beanA"));
    }

    @Test
    public void getByTypeAndName() {
        assertNotNull(Ioc.get(_A.class, "beanA"));
    }

    @Test
    public void getByType() {
        assertNotNull(Ioc.get(_A.class));
    }
}