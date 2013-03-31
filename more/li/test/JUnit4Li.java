package li.test;

import java.lang.reflect.Field;
import java.util.List;

import li.annotation.Inject;
import li.dao.Trans;
import li.ioc.Ioc;
import li.util.Log;
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
    private static final Log log = Log.init();

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

        List<Field> fields = Reflect.getFields(type);
        for (Field field : fields) {
            Inject inject = field.getAnnotation(Inject.class);
            if (null != inject) {
                if (Verify.basicType(field.getType())) {
                    Reflect.set(target, field.getName(), inject.value());
                } else {
                    Reflect.set(target, field.getName(), Ioc.get(field.getType(), inject.value()));
                }
                log.trace("Set Field: ?.? = ?", type, field.getName(), inject.value());
            }
        }
        return target;
    }

    /**
     * 执行一个测试方法
     */
    protected Statement methodInvoker(final FrameworkMethod method, final Object target) {
        if (null == type.getAnnotation(Rollback.class)) {
            return super.methodInvoker(method, target);
        } else {
            return new Statement() {
                public void evaluate() throws Throwable {
                    new Trans() {
                        public void run() {
                            try {
                                method.invokeExplosively(target);
                            } catch (Throwable e) {
                                throw new RuntimeException(e);
                            }
                            Trans.EXCEPTION.set(new RuntimeException("rollback when test"));
                        }// public void run()
                    };
                }// public void evaluate()
            };
        }// else
    }
}