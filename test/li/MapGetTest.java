package li;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

public class MapGetTest {
    private static final Integer ITEM_NUM = 10000;// 元素个数
    private static final Integer GET_NUM = 10000;// 取值次数

    @Test
    public void testMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < ITEM_NUM; i++) {
            map.put("key_" + i, "value_" + i);
        }

        Long start = System.currentTimeMillis();
        for (int i = 0; i < GET_NUM; i++) {
            String key = "key_" + new Random().nextInt(ITEM_NUM);
            System.out.println(map.get(key));
        }
        System.out.println("耗时 毫秒 " + (System.currentTimeMillis() - start));
    }

    @Test
    public void testList() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < ITEM_NUM; i++) {
            list.add("key_" + i);
        }

        Long start = System.currentTimeMillis();
        for (int i = 0; i < GET_NUM; i++) {
            String key = "key_" + new Random().nextInt(ITEM_NUM);
            System.out.println(get(list, key));
        }
        System.out.println("耗时 毫秒 " + (System.currentTimeMillis() - start));
    }

    private String get(List<String> list, String key) {
        for (String each : list) {
            if (key.equals(each)) {
                return each;
            }
        }
        return "";
    }
}