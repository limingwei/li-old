package li;

import li.aop.AopTest;
import li.dao.AbstractDaoTest;
import li.dao.ModelBuilderTest;
import li.dao.PageTest;
import li.dao.QueryBuilderTest;
import li.dao.QueryRunnerTest;
import li.dao.RecordTest;
import li.dao.StringIdRecordTest;
import li.dao.TransTest;
import li.demo.action.AccountActionTest;
import li.demo.action.DemoActionTest;
import li.demo.record.AccountTest;
import li.demo.record.ForumTest;
import li.hibernate.HibernateTest;
import li.hibernate.UserDaoTest;
import li.ioc.IocTest;
import li.model.BeanTest;
import li.model.FieldTest;
import li.mvc.ContextTest;
import li.service.AbstractServiceTest;
import li.util.ConvertTest;
import li.util.FilesTest;
import li.util.LogTest;
import li.util.ReflectTest;
import li.util.VerifyTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ContextTest.class,// MVC
        AbstractServiceTest.class, // Service
        IocTest.class,// Ioc
        AopTest.class, // Aop
        BeanTest.class, FieldTest.class, // Model
        AbstractDaoTest.class, QueryRunnerTest.class, RecordTest.class, PageTest.class, //
        QueryBuilderTest.class, ModelBuilderTest.class, TransTest.class, StringIdRecordTest.class,// Dao
        ConvertTest.class, ReflectTest.class, FilesTest.class, LogTest.class, VerifyTest.class, // Util
        AccountTest.class, ForumTest.class, // Record
        DemoActionTest.class, AccountActionTest.class, // Action
        HibernateTest.class, UserDaoTest.class })
public class AllTest {}