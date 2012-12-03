package li.dao;

import java.util.Arrays;
import java.util.Collections;

import li.util.Reflect;
import li.util.Verify;

import com.alibaba.druid.wall.WallConfig;

public class WallFilter extends com.alibaba.druid.wall.WallFilter {
    private WallConfig wallConfig;

    public WallFilter() {
        wallConfig = new WallConfig();
        this.setConfig(wallConfig);
    }

    /**
     * WallFilter中英文逗号分隔的设置为true的属性列表
     */
    public void setWallFilterTrue(String attrs) {
        String[] strs = Verify.isEmpty(attrs) ? new String[] {} : attrs.split("\\,");
        for (String attr : strs) {
            Reflect.set(this, attr, true);
        }
    }

    /**
     * WallFilter中英文逗号分隔的设置为true的属性列表
     */
    public void setWallFilterFalse(String attrs) {
        String[] strs = Verify.isEmpty(attrs) ? new String[] {} : attrs.split("\\,");
        for (String attr : strs) {
            Reflect.set(this, attr, false);
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

    /**
     * PermitVariants，英文逗号分隔的列表
     */
    public void setPermitVariants(String attrs) {
        wallConfig.getPermitVariants().addAll(Verify.isEmpty(attrs) ? Collections.EMPTY_LIST : Arrays.asList(attrs.split("\\,")));
    }
}