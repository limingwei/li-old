package li.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ReflectTest {
    DestModel attr = new DestModel();

    SrcModel srcModel = new SrcModel();

    DestModel destModel = new DestModel();

    @Before
    public void before() {
        attr.attr1 = "Model2.attr.attr1";
        srcModel.attr1 = "123123";
        srcModel.attr2 = false;
        srcModel.destModel = attr;
    }

    @Test
    public void copy() {
        Reflect.copy(srcModel, destModel);

        assertEquals("123123", destModel.attr1);
        assertEquals(false, destModel.attr2);
        assertEquals(attr, destModel.destModel);
    }

    @Test
    public void getTest1() {
        int num = 100;
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < num; i++) {
            Object result = Reflect.get(map, "none");
        }
        System.out.println("新版本的 get 执行 " + num + " 次 耗时 " + (System.currentTimeMillis() - start));
    }

    @Test
    public void getTest2() {
        int num = 100;
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < num; i++) {
            Object result = get2(map, "none");
        }
        System.out.println("老版本的 get 执行 " + num + " 次 耗时 " + (System.currentTimeMillis() - start));
    }

    /**
     * 老版本的方法,做对比
     */
    public static Object get2(Object target, String fieldName) {
        try {
            String method = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getter = Reflect.getMethod(target.getClass(), method);
            return getter.invoke(target);// 使用Getter方法
        } catch (Exception e) {// 没有匹配的Getter方法
            try {
                return Reflect.getField(target.getClass(), fieldName).get(target);// 通过属性访问
            } catch (Exception ex) {
                if (target instanceof Map) {
                    return ((Map) target).get(fieldName);// 通过Record.get()方法
                } else {
                    throw new RuntimeException("Reflect.get() target=" + target + ",fieldName=" + fieldName);
                }
            }
        }
    }

    @Test
    public void setTest1() {
        int num = 100;
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < num; i++) {
            Reflect.set(map, "f", "v");
        }
        System.out.println("新版本的 set 执行 " + num + " 次 耗时 " + (System.currentTimeMillis() - start));
    }

    @Test
    public void setTest2() {
        int num = 100;
        long start = System.currentTimeMillis();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < num; i++) {
            set2(map, "f", "v");
        }
        System.out.println("老版本的 set 执行 " + num + " 次 耗时 " + (System.currentTimeMillis() - start));
    }

    /**
     * 老版本方法,供对比
     */
    public static void set2(Object target, String fieldName, Object value) {
        try {
            String method = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method setter = Reflect.getMethod(target.getClass(), method, Reflect.fieldType(target.getClass(), fieldName));
            setter.invoke(target, value);// 使用Setter方法,这里没做类型转换
        } catch (Exception e) {// 没有匹配的Setter方法
            try {
                Field field = Reflect.getField(target.getClass(), fieldName);
                field.set(target, Convert.toType(field.getType(), value));// 通过属性访问,这里有做类型转换
            } catch (Exception ex) {
                if (target instanceof Map) {
                    ((Map) target).put(fieldName, value);// 通过Record.set()方法,这里也没做类型转换
                } else {
                    throw new RuntimeException("Reflect.set() target=" + target + ",fieldName=" + fieldName + ",value=" + value);
                }
            }
        }
    }

    @Test
    public void fieldType1() {
        int num = 100;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            Class<?> type = Reflect.fieldType(li.dao.AbstractDao.class, "beanMeta");
        }
        System.out.println("新版本的 fieldType 执行 " + num + " 次 耗时 " + (System.currentTimeMillis() - start));
    }

    @Test
    public void fieldType2() {
        int num = 100;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            Class<?> type = fieldType2(li.dao.AbstractDao.class, "beanMeta");
        }
        System.out.println("老版本的 fieldType 执行 " + num + " 次 耗时 " + (System.currentTimeMillis() - start));
    }

    /**
     * 老版本,供性能对比
     */
    public static Class<?> fieldType2(Class<?> targetType, String fieldName) {
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method[] methods = targetType.getMethods();
        for (Method method : methods) {
            if (1 == method.getParameterTypes().length && method.getName().equals(setterName)) {
                return method.getParameterTypes()[0];// 从setter探测
            }
        }
        Field field = Reflect.getField(targetType, fieldName);
        if (null != field) { // 从field探测
            return field.getType();
        }
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method getter = Reflect.getMethod(targetType, getterName);
        if (null != getter) {// 从getter探测
            return getter.getReturnType();
        }
        return null;
    }

    @Test
    public void testActualType() {
        assertNull(Reflect.actualType(ReflectTest.class, 9));
    }
}