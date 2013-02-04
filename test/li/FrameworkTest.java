package li;

import li.aop.AopTest;
import li.dao.AbstractDaoTest;
import li.dao.ModelBuilderTest;
import li.dao.QueryBuilderTest;
import li.dao.QueryRunnerTest;
import li.dao.RecordTest;
import li.dao.TransTest;
import li.h2.H2Test;
import li.ioc.IocTest;
import li.model.BeanTest;
import li.model.FieldTest;
import li.mvc.ContextTest;
import li.service.AbstractServiceTest;
import li.sqlite.SqliteTest;
import li.util.ConvertTest;
import li.util.FilesTest;
import li.util.LogTest;
import li.util.PageTest;
import li.util.ReflectTest;
import li.util.VerifyTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SqliteTest.class, H2Test.class, //
        ContextTest.class,//
        AbstractServiceTest.class, //
        IocTest.class, AopTest.class, //
        BeanTest.class, FieldTest.class, //
        AbstractDaoTest.class, QueryRunnerTest.class, RecordTest.class, PageTest.class,//
        QueryBuilderTest.class, ModelBuilderTest.class, TransTest.class,//
        ConvertTest.class, ReflectTest.class, FilesTest.class, LogTest.class, VerifyTest.class //
})
public class FrameworkTest {

}