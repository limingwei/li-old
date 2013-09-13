package li.dao;

import li.annotation.Inject;
import li.dao.test.StringIdRecord;
import li.test.BaseTest;
import li.util.Log;

import org.junit.Test;

/**
 * @author 明伟
 */
public class StringIdRecordTest extends BaseTest {
    static Log log = Log.init();

    @Inject
    StringIdRecord stringIdRecord;

    @Test
    public void findById() {
        log.info("li.dao.StringIdRecordTest.findById() " + stringIdRecord.find("1"));
    }

    @Test
    public void findBySql() {
        log.info("li.dao.StringIdRecordTest.findBySql() " + stringIdRecord.find("WHERE TRUE", new Object[0]));
    }
}