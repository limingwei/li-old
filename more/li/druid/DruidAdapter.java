package li.druid;

import java.sql.Connection;
import java.sql.SQLException;

import li.ioc.Ioc;
import li.util.Log;
import li.util.Verify;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidAdapter extends DruidDataSource {
    private static final long serialVersionUID = 8840084696561752691L;

    private static final Log log = Log.init();

    /**
     * 替换内置的setFilters
     */
    public void setDruidFilters(String filters) throws SQLException {
        String[] filterArray = (Verify.isEmpty(filters)) ? new String[0] : filters.split("\\,");
        for (String item : filterArray) {
            if ("wall".equals(item)) {
                log.info("using li.datasource.druid.WallDruidFilter");
                WallDruidFilter wallFilter = Ioc.get(WallDruidFilter.class);
                super.getProxyFilters().add(null != wallFilter ? wallFilter : new WallDruidFilter());
            } else {
                super.setFilters(item);
            }
        }
    }

    /**
     * 不允许初始化DataSource后使用不同的密码获取Connection
     */
    public Connection getConnection(String username, String password) throws SQLException {
        log.warn("not allowed to get a connection using new username and password after datasource inited !!!");
        return super.getConnection();
    }
}