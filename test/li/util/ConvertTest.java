package li.util;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import li.dao.Record;

import org.junit.Test;

public class ConvertTest {
    private static final Log log = Log.init();

    @Test
    public void timeConvert() {
        String[] times = { "2012/12/31 12:12:12", "2012-12-31 12:12:12", "2012/12/31 12:12", "2012-12-31 12:12", "2012/12/31", "2012-12-31", "12:12:12", "12:12" };
        Class<?>[] types = { java.util.Date.class, java.sql.Date.class, Time.class, Timestamp.class };
        for (String time : times) {
            for (Class<?> type : types) {
                log.debug(Convert.toType(type, time));
            }
        }
    }

    @Test
    public void fromJson() {
        List<Record> list = new ArrayList<Record>();
        for (int i = 0; i < 3; i++) {
            Record record = new Record();
            record.set("id", 1);
            record.set("username", "li");
            record.set("password", "wode");
            record.set("email", "limw@w.cn");
            list.add(record);

            log.debug(Convert2.fromJson(Record.class, Convert2.toJson(record)));
        }

        List<List<Record>> list2 = new ArrayList<List<Record>>();
        list2.add(list);
        list2.add(list);
        list2.add(list);

        String json = Convert2.toJson(list);

        List<Record> records = Convert2.fromJson(Record.class, json);

        log.debug(records);

        String json2 = Convert2.toJson(Convert2.fromJson(Record.class, Convert2.toJson(Convert2.fromJson(Record.class, Convert2.toJson(list)))));

        log.debug(json2);

        assertEquals(json, json2);
    }

    @Test
    public void toJson() {
        List<Record> list = new ArrayList<Record>();
        for (int i = 0; i < 3; i++) {
            Record record = new Record();
            record.set("id", 1);
            record.set("username", "li");
            record.set("password", "wode");
            record.set("email", "limw@w.cn");
            list.add(record);
        }
        String json = Convert2.toJson(list);
        log.debug(json);
    }

    @Test
    public void toMap() {
        Convert.toMap(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
    }

    @Test
    public void toType() {
        String str2 = Convert.toType(String.class, "str");
        assertEquals("str", str2);

        java.util.Date date1 = Convert.toType(java.util.Date.class, "2012-02-11");
        java.sql.Date date2 = Convert.toType(java.sql.Date.class, "2012-02-11");
        java.sql.Time time = Convert.toType(java.sql.Time.class, "17:30:00");
        java.sql.Timestamp timestamp = Convert.toType(java.sql.Timestamp.class, "2012-02-11 17:30:00");

        log.debug("?\t?\t?\t?", timestamp, date2, time, date1);
    }
}