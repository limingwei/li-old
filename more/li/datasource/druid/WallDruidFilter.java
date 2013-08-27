package li.datasource.druid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import li.util.Reflect;
import li.util.Verify;

import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

public class WallDruidFilter extends WallFilter {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final List<String> EMPTY_STRING_LIST = new ArrayList<String>(0);

    private WallConfig wallConfig;

    public WallDruidFilter() {
        wallConfig = new WallConfig();
        this.setConfig(wallConfig);
    }

    /**
     * 英文逗号分隔的列表
     */
    public void setDenyFunctions(String attrs) {
        wallConfig.getDenyFunctions().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的列表
     */
    public void setDenyObjects(String attrs) {
        wallConfig.getDenyObjects().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的列表
     */
    public void setDenySchemas(String attrs) {
        wallConfig.getDenySchemas().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的列表
     */
    public void setDenyTables(String attrs) {
        wallConfig.getDenyTables().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的列表
     */
    public void setDenyVariants(String attrs) {
        wallConfig.getDenyVariants().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的禁止使用的func列表
     */
    public void setPermitFunctions(String attrs) {
        wallConfig.getPermitFunctions().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的禁止访问的数据库列表
     */
    public void setPermitSchemas(String attrs) {
        wallConfig.getPermitSchemas().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的禁止访问的table列表
     */
    public void setPermitTables(String attrs) {
        wallConfig.getPermitTables().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * PermitVariants，英文逗号分隔的列表
     */
    public void setPermitVariants(String attrs) {
        wallConfig.getPermitVariants().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * 英文逗号分隔的只读table列表
     */
    public void setReadonlyTables(String attrs) {
        wallConfig.getReadOnlyTables().addAll(Verify.isEmpty(attrs) ? EMPTY_STRING_LIST : Arrays.asList(attrs.split("\\,")));
    }

    /**
     * WallConfig中英文逗号分隔的设置为false的属性列表
     */
    public void setWallConfigFalse(String attrs) {
        String[] strs = Verify.isEmpty(attrs) ? EMPTY_STRING_ARRAY : attrs.split("\\,");
        for (String attr : strs) {
            Reflect.set(wallConfig, attr, false);
        }
    }

    /**
     * WallConfig中英文逗号分隔的设置为true的属性列表
     */
    public void setWallConfigTrue(String attrs) {
        String[] strs = Verify.isEmpty(attrs) ? EMPTY_STRING_ARRAY : attrs.split("\\,");
        for (String attr : strs) {
            Reflect.set(wallConfig, attr, true);
        }
    }
}