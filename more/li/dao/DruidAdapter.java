package li.dao;

import java.sql.SQLException;

import li.ioc.Ioc;
import li.util.Verify;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidAdapter extends DruidDataSource {
    private static final long serialVersionUID = 8840084696561752691L;

    /**
     * 替换内置的setFilters
     */
    public void setDruidFilters(String filters) throws SQLException {
        System.out.println("setFilters " + filters);

        String[] filterArray = (Verify.isEmpty(filters)) ? new String[] {} : filters.split("\\,");
        for (String item : filterArray) {
            if ("wall".equals(item)) {
                System.out.println("super.getProxyFilters().add(Ioc.get(WallFilter.class)) " + item);
                super.getProxyFilters().add(Ioc.get(WallFilter.class));
            } else {
                System.out.println("super.setFilters(item) " + item);
                super.setFilters(item);
            }
        }
    }
}