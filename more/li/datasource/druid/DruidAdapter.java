package li.datasource.druid;

import java.sql.Connection;
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
        String[] filterArray = (Verify.isEmpty(filters)) ? new String[0] : filters.split("\\,");
        for (String item : filterArray) {
            if ("wall".equals(item)) {
                WallDruidFilter wallFilter = Ioc.get(WallDruidFilter.class);
                if (null == wallFilter) {
                    wallFilter = new WallDruidFilter();
                }
                super.getProxyFilters().add(wallFilter);
            } else {
                super.setFilters(item);
            }
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return super.getConnection();
    }
}