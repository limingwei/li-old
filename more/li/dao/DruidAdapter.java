package li.dao;

import java.sql.SQLException;

import li.ioc.Ioc;
import li.util.Verify;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidAdapter extends DruidDataSource {
    private static final long serialVersionUID = 8840084696561752691L;

    /**
     * 重写setFilters方法
     */
    public void setFilters(String filters) throws SQLException {
        String[] filterArray = (Verify.isEmpty(filters)) ? new String[] {} : filters.split("\\,");
        for (String item : filterArray) {
            if ("wall".equals(item)) {
                super.getProxyFilters().add(Ioc.get(WallFilter.class));
            } else {
                super.setFilters(item);
            }
        }
    }
}