package li.dao;

import li.annotation.Inject;
import li.dao.test.StringIdRecord;
import li.test.BaseTest;

import org.junit.Test;

public class StringIdRecordTest extends BaseTest {
    @Inject
    StringIdRecord stringIdRecord;

    @Test
    public void findById() {
        System.out.println(stringIdRecord.find("1"));
    }

    @Test
    public void findBySql() {
        System.out.println(stringIdRecord.find("WHERE TRUE", new Object[0]));
    }
}