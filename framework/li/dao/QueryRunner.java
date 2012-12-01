package li.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import li.util.Log;

/**
 * Dao的辅助类,用于构建PreparedStatement,执行SQL查询
 * 
 * @author li (limw@w.cn)
 * @version 0.1.6 (2012-05-08)
 */
public class QueryRunner {
    private static final Log log = Log.init();

    private Connection connection;// 当前QueryRunner实例的connection

    private PreparedStatement preparedStatement;// 当前QueryRunner实例的preparedStatement

    /**
     * 实例变量,保存最后一条被插入记录被设置的自增ID
     */
    protected Integer LAST_INSERT_ID;

    /**
     * 初始化一个QueryRunner
     */
    public QueryRunner(Connection connection) {
        this.connection = connection;
    }

    /**
     * 执行查询类SQL,返回ResultSet结果集
     */
    public ResultSet executeQuery(String sql) {
        ResultSet resultSet = null;
        if (null == Trans.CONNECTION_MAP.get() || null == Trans.EXCEPTION.get()) {
            try { // 如果未进入事务或事务中未出现异常,则执行后面的语句
                log.info(sql + "-> " + connection.getClass().getName() + "@" + Integer.toHexString(connection.hashCode()));

                preparedStatement = connection.prepareStatement(sql);
                resultSet = preparedStatement.executeQuery();
            } catch (Exception e) {
                Trans.EXCEPTION.set(e);// 出现异常,记录起来
                log.error(e);
                e.printStackTrace();
            }
        }
        return resultSet;// 查询类SQL,在ModelBuilder中关闭
    }

    /**
     * 执行更新类SQL,返回Integer类型的,受影响的行数
     */
    public Integer executeUpdate(String sql) {
        Integer count = -1;
        if (null == Trans.CONNECTION_MAP.get() || null == Trans.EXCEPTION.get()) {
            try { // 如果未进入事务或事务中未出现异常,则执行后面的语句
                log.info(sql + " -> " + connection.getClass().getName() + "@" + Integer.toHexString(connection.hashCode()));
                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);// 构建要返回GeneratedKeys的Statement
                count = preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();// 获得主键结果集
                this.LAST_INSERT_ID = null != generatedKeys && generatedKeys.next() ? generatedKeys.getInt(1) : -1;// 设置最后更新的主键的值
                generatedKeys.close();// 关闭主键结果集
            } catch (Exception e) {
                Trans.EXCEPTION.set(e); // 出现异常,记录起来
                log.error(e);
                e.printStackTrace();
            }
        }
        this.close();// 更新类SQL,在这里关闭
        return count;
    }

    /**
     * 关闭QueryRunner：关闭PreparedStatement;关闭Connection,如果未进入事务的话
     */
    public void close() {
        try {
            if (null != preparedStatement) {
                preparedStatement.close();
                log.trace("Closing PreparedStatement " + preparedStatement);
            }
            if (null != connection && null == Trans.CONNECTION_MAP.get()) {
                connection.close();// Trans.CONNECTION_MAP.get()为空表示未进入事务,若已进入事务,则由事务关闭连接
                log.debug("Closing Connection " + connection);
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception at li.dao.QueryRunner.close()", e);
        }
    }
}