package li.demo;

import li.FrameworkTest;
import li.demo.action.AccountActionTest;
import li.demo.action.DemoActionTest;
import li.demo.action.ForumActionTest;
import li.demo.action.MemberActionTest;
import li.demo.action.PostActionTest;
import li.demo.action.ThreadActionTest;
import li.demo.record.AccountTest;
import li.demo.record.ForumTest;
import li.demo.record.MemberTest;
import li.demo.record.PostTest;
import li.demo.record.ThreadTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FrameworkTest.class, //
        AccountTest.class, ForumTest.class, MemberTest.class, PostTest.class, ThreadTest.class,//
        DemoActionTest.class, AccountActionTest.class, MemberActionTest.class, ForumActionTest.class,//
        PostActionTest.class, ThreadActionTest.class })
public class AllTest {

}