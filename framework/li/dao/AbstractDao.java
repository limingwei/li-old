package li.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import li.ioc.Ioc;
import li.model.Bean;
import li.model.Field;
import li.util.Log;
import li.util.Reflect;

/**
 * AbstractDao,通常,业务对象的Dao要继承这个基类
 * 
 * @author li (limw@w.cn)
 * @version 0.1.7 (2012-06-26)
 */
public class AbstractDao<T> {
    private static final Log log = Log.init();

    private Class<T> modelType;// 泛型参数的实际类型,即是数据对象类型

    private Bean beanMeta;// 存储数据对象结构

    private QueryBuilder queryBuilder;// QueryBuilder,SQL构造器

    private DataSource dataSource;// 当前Dao的DataSource

    /**
     * 通过泛型参数得到modelType
     * 
     * @see li.util.Reflect#actualType(Class, Integer)
     */
    protected Class<T> getType() {
        if (null == this.modelType) {
            this.modelType = (Class<T>) Reflect.actualType(getClass(), 0); // 通过泛型参数得到modelType
        }
        return this.modelType;
    }

    /**
     * 得到当前Dao所操作的数据对象的结构
     * 
     * @see li.model.Bean#getMeta(DataSource, Class)
     */
    protected Bean getBeanMeta() {
        if (null == this.beanMeta) {
            this.beanMeta = Bean.getMeta(getDataSource(), getType());
        }
        return this.beanMeta;
    }

    /**
     * 得到SQL构造器
     */
    protected QueryBuilder getQueryBuilder() {
        if (null == this.queryBuilder) {
            this.queryBuilder = new QueryBuilder();
        }
        if (null == this.queryBuilder.dataSource) {
            this.queryBuilder.dataSource = getDataSource();
        }
        if (null == this.queryBuilder.beanMeta) {
            this.queryBuilder.beanMeta = getBeanMeta();
        }
        return this.queryBuilder;
    }

    /**
     * 如果还没有注入dataSource,则尝试次从Ioc中搜索DataSource类型的Bean
     */
    protected DataSource getDataSource() {
        if (null == this.dataSource) {
            this.dataSource = Ioc.get(DataSource.class);
            log.warn("DataSource not injected for ?", this);
        }
        return this.dataSource;
    }

    /**
     * 得到一个Connection,从Trans或者从Datasource
     * 
     * @see li.dao.Trans
     * @see li.dao.QueryRunner
     */
    protected Connection getConnection() {
        try {
            if (null == Trans.CONNECTION_MAP.get()) {// 如果未进入事务
                return this.getDataSource().getConnection();// 则简单获取一个connection
            } else { // 如果已经进入事务
                Connection connection = Trans.CONNECTION_MAP.get().get(getClass()); // 从connectionMap中得到为这个Dao类缓存的connection
                if (null == connection || connection.isClosed()) { // 没有缓存这个Dao的connection或已被关闭
                    connection = this.getDataSource().getConnection(); // 获取一个新的connection
                    connection.setAutoCommit(false); // 设置为不自动提交
                    Trans.CONNECTION_MAP.get().put(getClass(), connection); // 缓存connection
                }
                return connection; // 返回这个connection
            }
        } catch (Exception e) {
            throw new RuntimeException("error to connect to the database", e);
        }
    }

    /**
     * 查询对象对应的表的总记录数
     * 
     * @see li.dao.AbstractDao#count(String, Object...)
     */
    public Integer count() {
        return count(getQueryBuilder().countAll());
    }

    /**
     * 根据SQL条件查询记录数
     * 
     * @param sql 传入的sql语句,可以包含'?'占位符和具名占位符
     * @param args 替换sql中占位符的值,或者对应具名占位符的Map
     */
    public Integer count(String sql, Object... args) {
        QueryRunner queryRunner = new QueryRunner(getConnection());
        ModelBuilder modelBuilder = new ModelBuilder(queryRunner, queryRunner.executeQuery(getQueryBuilder().countBySql(sql, args)));
        return Integer.valueOf(modelBuilder.value(1, true, true));
    }

    /**
     * 删除ID等于传入参数的一条记录,如果存在的话
     * 
     * @see li.dao.AbstractDao#delete(String, Object...)
     */
    public Boolean delete(Number id) {
        return 0 < delete(getQueryBuilder().deleteById(id));
    }

    /**
     * 根据SQL条件删除若干条数据
     * 
     * @param sql 传入的sql语句,可以包含'?'占位符和具名占位符
     * @param args 替换sql中占位符的值,或者对应具名占位符的Map
     * @return 受影响的行数
     * 
     * @see li.dao.AbstractDao#update(String, Object...)
     */
    public Integer delete(String sql, Object... args) {
        return new QueryRunner(getConnection()).executeUpdate(getQueryBuilder().deleteBySql(sql, args));
    }

    /**
     * 根据ID查询一条记录
     * 
     * @see li.dao.AbstractDao#find(String, Object...)
     */
    public T find(Number id) {
        return find(getQueryBuilder().findById(id));
    }

    /**
     * 根据SQL条件查询一条记录
     * 
     * @param sql 传入的sql语句,可以包含'?'占位符和具名占位符
     * @param args 替换sql中占位符的值,或者对应具名占位符的Map
     * 
     * @see li.dao.AbstractDao#list(Page, String, Object...)
     */
    public T find(String sql, Object... args) {
        List<T> list = list(null, getQueryBuilder().findBySql(sql, args));
        return null != list && list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 根据分页对象进行分页查询,如果page不为NULL,他会被自动设置总记录数
     * 
     * @see li.dao.AbstractDao#list(Page, String, Object...)
     */
    public List<T> list(Page page) {
        return list(page, getQueryBuilder().list(page));
    }

    /**
     * 根据SQL条件和分页对象进行分页查询
     * 
     * @param sql 传入的sql语句,可以包含'?'占位符和具名占位符
     * @param args 替换sql中占位符的值,或者对应具名占位符的Map
     */
    public List<T> list(Page page, String sql, Object... args) {
        sql = getQueryBuilder().listBySql(page, sql, args);

        QueryRunner queryRunner = new QueryRunner(getConnection());
        ResultSet resultSet = queryRunner.executeQuery(sql);
        ModelBuilder modelBuilder = new ModelBuilder(queryRunner, resultSet);

        if (null != resultSet && null != page && page.count()) {
            page.setRecordCount(count(sql));
        }
        Integer count = null == page ? Page.DEFAULT_SIZE : page.getPageSize();
        return modelBuilder.list(getType(), getBeanMeta().fields, count, true);
    }

    /**
     * 执行SQL查询并将结果集封装成Record或其子类的List
     */
    public List<Record> query(Page page, String sql, Object... args) {
        sql = getQueryBuilder().listBySql(page, sql, args);

        QueryRunner queryRunner = new QueryRunner(getConnection());
        ResultSet resultSet = queryRunner.executeQuery(sql);
        ModelBuilder modelBuilder = new ModelBuilder(queryRunner, resultSet);

        if (null != resultSet && null != page && page.count()) {
            page.setRecordCount(count(sql));
        }
        Integer count = null == page ? Page.DEFAULT_SIZE : page.getPageSize();
        Class<?> type = Record.class.isAssignableFrom(getType()) ? getType() : Record.class;// Record类型或其子类
        return (List<Record>) modelBuilder.list(type, Field.list(resultSet), count, true);
    }

    /**
     * 向数据库中插入一条记录,save方法完成后,对象的ID将会被设值
     */
    public Boolean save(T t) {
        QueryRunner queryRunner = new QueryRunner(getConnection());
        Integer updateCount = queryRunner.executeUpdate(getQueryBuilder().save(t));

        Reflect.set(t, getBeanMeta().getId().name, queryRunner.LAST_INSERT_ID);// 设置对象ID为最后主键值

        return 0 < updateCount;
    }

    /**
     * 向数据库中插入一条记录,忽略为空的属性
     */
    public Boolean saveIgnoreNull(T t) {
        QueryRunner queryRunner = new QueryRunner(getConnection());
        Integer updateCount = queryRunner.executeUpdate(getQueryBuilder().saveIgnoreNull(t));

        Reflect.set(t, getBeanMeta().getId().name, queryRunner.LAST_INSERT_ID);// 设置对象ID为最后主键值

        return 0 < updateCount;
    }

    /**
     * 执行更新类的自定义SQL
     * 
     * @param sql 传入的sql语句,可以包含'?'占位符和具名占位符
     * @param args 替换sql中占位符的值,或者对应具名占位符的Map
     * @return 受影响的行数
     */
    public Integer update(String sql, Object... args) {
        return new QueryRunner(getConnection()).executeUpdate(getQueryBuilder().updateBySql(sql, args));
    }

    /**
     * 更新一个对象,根据ID得到对象,然后更新其他属性值
     * 
     * @see li.dao.AbstractDao#update(String, Object...)
     */
    public Boolean update(T t) {
        return 0 < update(getQueryBuilder().update(t));
    }

    /**
     * 更新一个数据对象,忽略其中值为null的属性
     * 
     * @see li.dao.AbstractDao#update(String, Object...)
     */
    public Boolean updateIgnoreNull(T t) {
        return 0 < update(getQueryBuilder().updateIgnoreNull(t));
    }
}