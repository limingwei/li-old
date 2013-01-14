package li.test;

import java.lang.reflect.Field;

import li.annotation.Inject;
import li.ioc.Ioc;
import li.util.Reflect;
import li.util.Verify;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class JUnit4Li extends BlockJUnit4ClassRunner {
    private Class<?> type;

    public JUnit4Li(Class<?> type) throws InitializationError {
        super(type);
        this.type = type;
    }

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
}