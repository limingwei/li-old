package li.dao;

import java.sql.SQLException;
import java.util.Arrays;
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
        if (filters == null || filters.length() == 0) {
            return;
        }
        String[] filterArray = filters.split("\\,");

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

        // 禁止使用的func列表
        String[] PERMIT_FUNCTIONS = properties.getProperty("DRUID.PERMIT_FUNCTIONS").split(",");
        // 禁止访问的数据库列表
        String[] PERMIT_SCHEMAS = properties.getProperty("DRUID.PERMIT_SCHEMAS").split(",");
        // 禁止访问的table列表
        String[] PERMIT_TABLES = properties.getProperty("DRUID.PERMIT_TABLES").split(",");
        // 只读table列表
        String[] READONLY_TABLES = properties.getProperty("DRUID.READONLY_TABLES").split(",");
        // 禁止访问的系统对象列表
        String[] PERMIT_OBJECTS = properties.getProperty("DRUID.PERMIT_OBJECTS").split(",");
        //
        String[] PERMIT_VARIANTS = properties.getProperty("DRUID.PERMIT_VARIANTS").split(",");

        WallConfig wallConfig = new WallConfig();

        wallConfig.getPermitFunctions().addAll(Arrays.asList(PERMIT_FUNCTIONS));
        wallConfig.getPermitObjects().addAll(Arrays.asList(PERMIT_OBJECTS));
        wallConfig.getPermitSchemas().addAll(Arrays.asList(PERMIT_SCHEMAS));
        wallConfig.getPermitTables().addAll(Arrays.asList(PERMIT_TABLES));
        wallConfig.getPermitVariants().addAll(Arrays.asList(PERMIT_VARIANTS));
        wallConfig.getReadOnlyTables().addAll(Arrays.asList(READONLY_TABLES));

        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);

        super.getProxyFilters().add(wallFilter);
    }
}