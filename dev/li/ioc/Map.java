package li.ioc;

import java.util.HashMap;

import li.util.Verify;

/**
 * @author : 明伟 
 * @date : 2013年7月25日 下午2:36:25
 * @version 1.0 
 */
public class Map extends HashMap<Object, Object> {
    private static final long serialVersionUID = 251909297223224764L;

    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public void setEach(String beans) {
        String[] array = (Verify.isEmpty(beans)) ? new String[0] : beans.split("\\,");
        for (String each : array) {
            String[] map = each.split(":");
            super.put(map[0], Ioc.get(map[1]));
        }
    }
}