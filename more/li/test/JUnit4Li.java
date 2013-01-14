package li.test;

import java.lang.reflect.Field;

import li.annotation.Inject;
import li.dao.Trans;
import li.ioc.Ioc;
import li.util.Reflect;
import li.util.Verify;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * 一个自定义的JUnit4ClassRunner, 提供Ioc注入和Dao回滚功能
 * 
 * @author li (limw@w.cn)
 * @version 0.1.1 (2013-01-14)
 */
public class JUnit4Li extends BlockJUnit4ClassRunner {
    private Class<?> type;

    /**
     * 初始化一个针对type类的TestRunner
     */
    public JUnit4Li(Class<?> type) throws InitializationError {
        super(type);
        this.type = type;
    }

    /**
     * 创建测试类的对象
     */
    protected Object createTest() throws Exception {
        Object target = super.createTest();

        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            Inject inject = field.getAnnotation(Inject.class);
            if (null != inject) {
                if (Verify.basicType(field.getType())) {
                    Reflect.set(target, field.getName(), inject.value());
                } else {
                    Reflect.set(target, field.getName(), Ioc.get(field.getType(), inject.value()));
                }
            }
        }
        return target;
    }

    /**
     * 执行一个测试方法
     */
    protected Statement methodInvoker(final FrameworkMethod method, final Object target) {
        if (null == getRollback(type)) {
            return super.methodInvoker(method, target);
        } else {
            return new Statement() {
                public void evaluate() throws Throwable {
                    new Trans() {
                        public void run() {
                            try {
                                method.invokeExplosively(target);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            Trans.EXCEPTION.set(new RuntimeException("rollback when test"));
                        }// public void run()
                    };// new Trans()
                }// public void evaluate()
            };// new Statement()
        }// else
    }

    /**
     * 检测一个类或其超类上是否有@Rollback注解
     */
    private Rollback getRollback(Class<?> type) {
        Rollback rollback = type.getAnnotation(Rollback.class);
        return null != rollback || Object.class.equals(type.getSuperclass()) ? rollback : getRollback(type.getSuperclass());
    }
}