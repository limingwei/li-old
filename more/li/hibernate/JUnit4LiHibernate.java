package li.hibernate;

import li.ioc.Ioc;
import li.test.JUnit4Li;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class JUnit4LiHibernate extends JUnit4Li {
    private SessionFactory _sessionFactory;

    public JUnit4LiHibernate(Class<?> type) throws InitializationError {
        super(type);
        this._sessionFactory = Ioc.get(SessionFactory.class);
    }

    protected Statement methodInvoker(final FrameworkMethod method, final Object target) {
        return new Statement() {
            public void evaluate() throws Throwable {
                OpenSessionInViewFilter.SESSION_THREADLOCAL.set(_sessionFactory.openSession());
                invoke(method, target);
                OpenSessionInViewFilter.closeSession(OpenSessionInViewFilter.SESSION_THREADLOCAL.get());
            }
        };
    }

    private void invoke(FrameworkMethod method, Object target) {
        try {
            super.methodInvoker(method, target).evaluate();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}