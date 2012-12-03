package li.dao;

import java.sql.SQLException;
import java.util.Arrays;

import li.util.Reflect;
import li.util.Verify;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

public class DruidAdapter extends DruidDataSource {
    private static final long serialVersionUID = 8840084696561752691L;

    private WallConfig wallConfig = new WallConfig();

    /**
     * 重写setFilters方法
     */
    public void setFilters(String filters) throws SQLException {
        String[] filterArray = (filters != null && filters.length() > 0) ? filters.split("\\,") : new String[] {};
        for (String item : filterArray) {
            if ("wall".equals(item)) {
                WallFilter wallFilter = new WallFilter();
                wallFilter.setConfig(wallConfig);
                super.getProxyFilters().add(wallFilter);
            } else {
                super.setFilters(item);
            }
        }
    }

    /**
     * 英文逗号分隔的设置为true的属性列表
     */
    public void setTrue(String attrs) {
        if (!Verify.isEmpty(attrs)) {
            String[] strs = attrs.split("\\,");
            for (String attr : strs) {
                Reflect.set(wallConfig, attr, true);
            }
        }
    }

    /**
     * 英文逗号分隔的设置为false的属性列表
     */
    public void setFalse(String attrs) {
        if (!Verify.isEmpty(attrs)) {
            String[] strs = attrs.split("\\,");
            for (String attr : strs) {
                Reflect.set(wallConfig, attr, false);
            }
        }
    }

    /**
     * 英文逗号分隔的禁止使用的func列表
     */
    public void setPermitFunctions(String attrs) {
        if (!Verify.isEmpty(attrs)) {
            if (!Verify.isEmpty(attrs)) {
                wallConfig.getPermitFunctions().addAll(Arrays.asList(attrs.split("\\,")));
            }
        }
    }

    /**
     * 英文逗号分隔的禁止访问的系统对象列表
     */
    public void setPermitObjects(String attrs) {
        if (!Verify.isEmpty(attrs)) {
            wallConfig.getPermitObjects().addAll(Arrays.asList(attrs.split("\\,")));
        }
    }

    /**
     * 英文逗号分隔的禁止访问的数据库列表
     */
    public void setPermitSchemas(String attrs) {
        if (!Verify.isEmpty(attrs)) {
            wallConfig.getPermitSchemas().addAll(Arrays.asList(attrs.split("\\,")));
        }
    }

    /**
     * 英文逗号分隔的禁止访问的table列表
     */
    public void setPermitTables(String attrs) {
        if (!Verify.isEmpty(attrs)) {
            wallConfig.getPermitTables().addAll(Arrays.asList(attrs.split("\\,")));
        }
    }

    /**
     * 英文逗号分隔的只读table列表
     */
    public void setReadonlyTables(String attrs) {
        if (!Verify.isEmpty(attrs)) {
            wallConfig.getReadOnlyTables().addAll(Arrays.asList(attrs.split("\\,")));
        }
    }

    public void setPermitVariants(String attrs) {
        if (!Verify.isEmpty(attrs)) {
            wallConfig.getPermitVariants().addAll(Arrays.asList(attrs.split("\\,")));
        }
    }
}