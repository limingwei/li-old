package li.dao.test;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;
import li.dao.Trans;
import li.util.Log;

@Bean
@Table("t_account")
public class _User extends Record<_User, Integer> {
    private static final long serialVersionUID = -2274465783698819130L;

    private static final Log log = Log.init();

    @li.annotation.Trans
    public void testMultipleTrans2() {
        new Trans() {
            public void run() {
                log.debug("calling testMultipleTrans3()");
                testMultipleTrans3();
            }
        };
    }

    @li.annotation.Trans
    public void testMultipleTrans3() {
        new Trans() {
            public void run() {
                new Trans() {
                    public void run() {
                        updateIgnoreNull(new _User().set("id", 2).set("username", "u-5" + System.currentTimeMillis()).set("password", "p-1").set("email", "e-1").set("status", 1));
                    }
                };
            }
        };
    }
}