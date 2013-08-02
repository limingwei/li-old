package li.ioc;

import java.util.ArrayList;

import li.util.Verify;

/**
 * @author : 明伟 
 * @date : 2013年7月25日 下午2:30:08
 * @version 1.0 
 */
public class List extends ArrayList<Object> {
    private static final long serialVersionUID = 4245009843840231651L;

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