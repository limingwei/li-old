package li;

import li.demo.action.AccountActionTest;
import li.demo.action.DemoActionTest;
import li.demo.action.PostActionTest;
import li.demo.record.AccountTest;
import li.demo.record.PostTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FrameworkTest.class, //
        AccountTest.class, PostTest.class, //
        DemoActionTest.class, AccountActionTest.class,//
        PostActionTest.class })
public class AllTest {

}