package li.dao_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import li.dao.Trans;
import li.util.Log;

/**
 * Dao的辅助类,用于构建PreparedStatement,执行SQL查询
 * 
 * @author li (limingwei@mail.com)
 * @version 0.1.6 (2012-05-08)
 */
public class QueryRunner {
    private static final Log log = Log.init();

    /**
     * 当前QueryRunner实例的connection
     */
    private Connection connection;

    /**
     * 当前QueryRunner实例的preparedStatement
     */
    private PreparedStatement preparedStatement;

    /**
     * 实例变量,保存最后一条被插入记录被设置的自增ID
     */
    private Integer lastInsertId = -2;

    /**
     * 初始化一个QueryRunner
     */
    public QueryRunner(Connection connection) {
        this.connection = connection;
    }

    /**
     * 返回最后插入的自增Id的值
     */
    public Integer getLastInsertId() {
        return this.lastInsertId;
    }

    /**
     * 执行查询类SQL,返回ResultSet结果集
     * 
     * @param args
     */
    public ResultSet executeQuery(String sql, Object[] args) {
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement = this.setParameters(preparedStatement, args);
            resultSet = preparedStatement.executeQuery();
            log.debug("? -> ?", sql, connection);
        } catch (Exception e) {
            error(e, sql);
        }
        return resultSet;// 查询类SQL,在ModelBuilder中关闭
    }

    public ResultSet executeQuery(String sql) {
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            log.debug("? -> ?", sql, connection);
        } catch (Exception e) {
            error(e, sql);
        }
        return resultSet;// 查询类SQL,在ModelBuilder中关闭
    }

    private PreparedStatement setParameters(PreparedStatement _preparedStatement, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            _preparedStatement.setObject(i + 1, args[i]);
        }
        return _preparedStatement;
    }

    /**
     * 执行更新类SQL,返回Integer类型的,受影响的行数
     */
    public Integer executeUpdate(String sql, Boolean returnGeneratedKeys) {
        Integer count = -1;
        try {
            preparedStatement = returnGeneratedKeys ? connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : connection.prepareStatement(sql);
            count = preparedStatement.executeUpdate();
            if (returnGeneratedKeys) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();// 主键结果集
                this.lastInsertId = null != generatedKeys && generatedKeys.next() ? generatedKeys.getInt(1) : -1;// 最后更新的主键的值
                generatedKeys.close();// 关闭主键结果集
            }
            log.debug("? -> [? row] ?", sql, count, connection);
        } catch (Exception e) {
            error(e, sql);
        }
        this.close();// 更新类SQL,在这里关闭
        return count;
    }

    /**
     * 关闭QueryRunner：关闭PreparedStatement;关闭Connection,如果未进入事务的话
     */
    public void close() {
        try {
            if (null != preparedStatement && !preparedStatement.isClosed()) {
                preparedStatement.close();
                log.trace("Closing PreparedStatement ?", preparedStatement);
            }
            if (null != connection && null == Trans.current()) {
                connection.close();// 若已进入事务,则由事务关闭连接
                log.trace("Closing Connection ?", connection);
            }
        } catch (Exception e) {
            throw new RuntimeException(e + " ", e);
        }
    }

    /**
     * 运行SQL语句时出现异常的处理
     */
    private void error(Exception e, String sql) {
        log.error("? ?", sql, e);
        throw new RuntimeException(e + " ", e);
    }
}