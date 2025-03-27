package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.*;

public class UserDaoJDBCImpl implements UserDao {

    @Override
    public void createUsersTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS users (Id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255), lastName VARCHAR(255), age INT NOT NULL)";
        try (Connection connection = Util.getConnection();
             PreparedStatement ps = connection.prepareStatement(createTable)) {
            ps.executeUpdate();
            System.out.println("Database has been created!");
        } catch (SQLException e) {
            System.out.println("Connection failed..." + e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             PreparedStatement ps = connection.prepareStatement("DROP TABLE IF EXISTS users")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Connection failed..." + e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setInt(3, age);
            ps.executeUpdate();
            System.out.println("User " + name + " " + lastName + " " + age + " has been saved!");
        } catch (SQLException e) {
            System.out.println("Connection failed..." + e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE Id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
            System.out.println("User " + id + " has been removed!");
        } catch (SQLException e) {
            System.out.println("Connection failed..." + e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
                System.out.println("User " + user.getName() + " " + user.getLastName() + " " + user.getAge());
            }
        } catch (SQLException e) {
            System.out.println("Connection failed..." + e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM users")) {
            ps.executeUpdate();
            System.out.println("Users has been cleaned!");
        } catch (SQLException e) {
            System.out.println("Connection failed..." + e);
        }
    }
}
