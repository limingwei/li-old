package li.dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import li.util.Files;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

public class DruidAdapter extends DruidDataSource {
    private static final long serialVersionUID = 8840084696561752691L;

    /**
     * 重写setFilters方法
     */
    public void setFilters(String filters) throws SQLException {
        String[] filterArray = (filters != null && filters.length() > 0) ? filters.split("\\,") : new String[] {};
        for (String item : filterArray) {
            if ("wall".equals(item)) {
                loadWallFilter();
            } else {
                super.setFilters(item);
            }
        }
    }

    /**
     * 加载WallConfig
     */
    private void loadWallFilter() {
        Properties properties = Files.load("config.properties");
        WallConfig wallConfig = new WallConfig();
        WallFilter wallFilter = new WallFilter();

        wallConfig.getPermitFunctions().addAll(getProps(properties, "DRUID.PERMIT_FUNCTIONS"));// 禁止使用的func列表
        wallConfig.getPermitObjects().addAll(getProps(properties, "DRUID.PERMIT_OBJECTS"));// 禁止访问的系统对象列表
        wallConfig.getPermitSchemas().addAll(getProps(properties, "DRUID.PERMIT_SCHEMAS"));// 禁止访问的数据库列表
        wallConfig.getPermitTables().addAll(getProps(properties, "DRUID.PERMIT_TABLES"));// 禁止访问的table列表
        wallConfig.getReadOnlyTables().addAll(getProps(properties, "DRUID.READONLY_TABLES"));// 只读table列表
        wallConfig.getPermitVariants().addAll(getProps(properties, "DRUID.PERMIT_VARIANTS"));

        wallFilter.setConfig(wallConfig);
        super.getProxyFilters().add(wallFilter);
    }

    /*
     * 从Properties里加载配置
     */
    private List<String> getProps(Properties properties, String key) {
        return Arrays.asList(properties.getProperty(key).split(","));
    }
}