package li.dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import li.util.Reflect;
import li.util.Verify;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

public class DruidAdapter extends DruidDataSource {
    private static final long serialVersionUID = 8840084696561752691L;

    private WallConfig wallConfig = new WallConfig();

    private WallFilter wallFilter = new WallFilter();

    /**
     * 重写setFilters方法
     */
    public void setFilters(String filters) throws SQLException {
        String[] filterArray = (filters != null && filters.length() > 0) ? filters.split("\\,") : new String[] {};
        for (String item : filterArray) {
            if ("wall".equals(item)) {
                wallFilter.setConfig(wallConfig);
                super.getProxyFilters().add(wallFilter);
            } else {
                super.setFilters(item);
            }
        }
    }

    /**
     * WallFilter中英文逗号分隔的设置为true的属性列表
     */
    public void setWallFilterTrue(String attrs) {
        String[] strs = Verify.isEmpty(attrs) ? new String[] {} : attrs.split("\\,");
        for (String attr : strs) {
            Reflect.set(wallFilter, attr, true);
        }
    }

    /**
     * WallFilter中英文逗号分隔的设置为true的属性列表
     */
    public void setWallFilterFalse(String attrs) {
        String[] strs = Verify.isEmpty(attrs) ? new String[] {} : attrs.split("\\,");
        for (String attr : strs) {
            Reflect.set(wallFilter, attr, false);
        }
    }

    /**
     * WallConfig中英文逗号分隔的设置为true的属性列表
     */
    public void setWallConfigTrue(String attrs) {
        String[] strs = Verify.isEmpty(attrs) ? new String[] {} : attrs.split("\\,");
        for (String attr : strs) {
            Reflect.set(wallConfig, attr, true);
        }
    }

    /**
     * WallConfig中英文逗号分隔的设置为false的属性列表
     */
    public void setWallConfigFalse(String attrs) {
        String[] strs = Verify.isEmpty(attrs) ? new String[] {} : attrs.split("\\,");
        for (String attr : strs) {
            Reflect.set(wallConfig, attr, false);
        }
    }

    /**
     * 英文逗号分隔的禁止使用的func列表
     */
    public void setPermitFunctions(String attrs) {
        wallConfig.getPermitFunctions().addAll(Verify.isEmpty(attrs) ? Collections.EMPTY_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的禁止访问的系统对象列表
     */
    public void setPermitObjects(String attrs) {
        wallConfig.getPermitObjects().addAll(Verify.isEmpty(attrs) ? Collections.EMPTY_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的禁止访问的数据库列表
     */
    public void setPermitSchemas(String attrs) {
        wallConfig.getPermitSchemas().addAll(Verify.isEmpty(attrs) ? Collections.EMPTY_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的禁止访问的table列表
     */
    public void setPermitTables(String attrs) {
        wallConfig.getPermitTables().addAll(Verify.isEmpty(attrs) ? Collections.EMPTY_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的只读table列表
     */
    public void setReadonlyTables(String attrs) {
        wallConfig.getReadOnlyTables().addAll(Verify.isEmpty(attrs) ? Collections.EMPTY_LIST : Arrays.asList(attrs.split("\\,")));
    }

    public void setPermitVariants(String attrs) {
        wallConfig.getPermitVariants().addAll(Verify.isEmpty(attrs) ? Collections.EMPTY_LIST : Arrays.asList(attrs.split("\\,")));
    }
}