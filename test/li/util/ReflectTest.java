package li.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ReflectTest {
    _Model2 attr = new _Model2();

    _Model1 model1 = new _Model1();

    _Model2 model2 = new _Model2();

    @Before
    public void before() {
        attr.attr1 = "Model2.attr.attr1";
        model1.attr1 = "123123";
        model1.attr2 = false;
        model1.model2 = attr;
    }

    @Test
    public void getTest1() {
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 100000; i++) {// 5434
            Object result = Reflect.get(map, "none");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void getTest2() {
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 100000; i++) {// 87
            // Object result = Reflect.get2(map, "none");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void setTest1() {
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 100000; i++) {// 8453
            Reflect.set(map, "f", "v");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void setTest2() {
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 100000; i++) {// 89
            // Reflect.set2(map, "f", "v");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void fieldType1() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {// 843
            Class<?> type = Reflect.fieldType(li.dao.AbstractDao.class, "beanMeta");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void fieldType2() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {// 154
            // Class<?> type = Reflect.fieldType2(li.dao.AbstractDao.class, "beanMeta");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void copy() {
        Reflect.copy(model1, model2);

        assertEquals("123123", model2.attr1);
        assertEquals(false, model2.attr2);
        assertEquals(attr, model2.model2);
    }

    @Test
    public void testActualType() {
        assertNull(Reflect.actualType(ReflectTest.class, 9));
    }
}