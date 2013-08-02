package li.ioc;

import java.util.HashSet;

import li.util.Verify;

/**
 * @author : 明伟 
 * @date : 2013年7月25日 下午2:36:03
 * @version 1.0 
 */
public class Set extends HashSet<Object> {
    private static final long serialVersionUID = 6309348438528997067L;

    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public void setEach(String beans) {
        String[] array = (Verify.isEmpty(beans)) ? new String[0] : beans.split("\\,");
        for (String each : array) {
            super.add(Ioc.get(each));
        }
    }
}