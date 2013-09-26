package li.generator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import li.generator.entity.Entity;

public class Db {
    public static List<Entity> getEntities(String url, String user, String password) {
        List<Entity> entities = new ArrayList<Entity>();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement("show tables");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Entity entity = new Entity();
                entity.setTableName(resultSet.getString(1));
                entities.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }
}