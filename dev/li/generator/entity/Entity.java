package li.generator.entity;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private String tableName;

    public Map toMap() {
        Map map = new HashMap();
        map.put("tableName", tableName);
        map.put("entityName", tableName);
        return map;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}