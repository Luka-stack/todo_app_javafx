package com.nisshoku.todo.database;

import com.nisshoku.todo.model.Task;
import com.nisshoku.todo.model.User;

import java.sql.*;

public class DatabaseHandler extends Configs {

    Connection dbConnection;

    public Connection getDbConnection() {

        try {
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?serverTimezone=UTC";
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
            return dbConnection;
        }
        catch (SQLException ex) {
            System.err.println("DatabaseHandler : " + ex.getMessage());
            return null;
        }
    }

    // Write
    public int createUser(User user) {

        int result = -1;

        String insert = "INSERT INTO " + Const.USERS_TABLE
                + " (" + Const.USERS_FIRST + ", " + Const.USERS_LAST  + ", " + Const.USERS_USER + ", "
                + Const.USERS_PASSWD + ", " + Const.USERS_GENDER + ")"
                + " VALUES(?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getGender());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }

        return result;
    }

    public int createTask(Task task, int userId) {

        int result = -1;

        String insert = "INSERT INTO " + Const.TASKS_TABLE +
                "(" + Const.USERS_ID +","+ Const.TASKS_DATE +","+ Const.TASKS_DESC
                +","+ Const.TASKS_TASK +")"+ " VALUES (?, NOW(),?,?)";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getTask());
            result = preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return result;
    }

    // Read
    public ResultSet getUser(User user) {

        ResultSet result = null;

        String query = "SELECT * FROM " + Const.USERS_TABLE + " WHERE "
                + Const.USERS_USER + "=?" + " AND "
                + Const.USERS_PASSWD + "=?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            result = preparedStatement.executeQuery();
        }
        catch (SQLException e) { e.getMessage(); }

        return result;
    }

    public ResultSet getTasks(int userId) {

        ResultSet resultSet = null;

        String query = "SELECT " + Const.TASKS_TASK + "," + Const.TASKS_DESC + "," + Const.TASKS_DATE
                + " FROM " + Const.TASKS_TABLE + " WHERE userId =?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
        }
        catch (SQLException e) { e.getMessage(); }

        return  resultSet;
    }

    // Update
    public int updateTask(String task, String newName, String newDesc) {

        int result = -1;

        String update = "UPDATE " + Const.TASKS_TABLE + " SET "
                + Const.TASKS_TASK + "=?" + "," + Const.TASKS_DESC + "=?"
                + "," + Const.TASKS_DATE + "=NOW() WHERE " + Const.TASKS_TASK + "=?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newDesc);
            preparedStatement.setString(3, task);
            result = preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.getStackTrace(); }

        return result;
    }

    // Delete
    public int deleteTask(String task) {

        int result = -1;

        String delete = "DELETE FROM " + Const.TASKS_TABLE + " WHERE " + Const.TASKS_TASK + " =?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);
            preparedStatement.setString(1, task);
            result = preparedStatement.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return result;
    }
}
