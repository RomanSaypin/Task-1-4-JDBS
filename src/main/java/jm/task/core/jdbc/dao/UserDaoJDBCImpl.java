package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//https://github.com/RomanSaypin/Task-1-4-JDBS.git
public class UserDaoJDBCImpl implements UserDao {
    private static final String REQUEST_SQL_CREATE_USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS user(id INT PRIMARY KEY AUTO_INCREMENT,
            name_user VARCHAR(40),
            lastName_user VARCHAR(40),
            age INT)
            """;
    private static final String REQUEST_SQL_DROP_USERS_TABLE = """
            DROP TABLE IF EXISTS user
            """;
    private static final String REQUEST_SQL_SAVE_USER = """
            INSERT INTO user(name_user, lastName_user, age) 
            VALUES(?,?,?)
            """;
    private static final String REQUEST_SQL_REMOVE_USER_BY_ID = """
            DELETE FROM user WHERE id = ?
            """;
    private static final String REQUEST_SQL_GET_ALL_USERS = """
            SELECT * FROM user
            """;
    private static final String REQUEST_SQL_CLEAN_USERS_TABLE = """
            DELETE FROM user
            """;


    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection(); var statement = connection.prepareStatement(REQUEST_SQL_CREATE_USERS_TABLE)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection(); var statement = connection.prepareStatement(REQUEST_SQL_DROP_USERS_TABLE)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {

        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(REQUEST_SQL_SAVE_USER)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REQUEST_SQL_REMOVE_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        String requestSQLGetAllUsers = "";
        List<User> list = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             var statement = connection.prepareStatement(REQUEST_SQL_GET_ALL_USERS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name_user"));
                user.setLastName(resultSet.getString("lastName_user"));
                user.setAge(resultSet.getByte("age"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(list);
        return list;

    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             var statement = connection.prepareStatement(REQUEST_SQL_CLEAN_USERS_TABLE)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
