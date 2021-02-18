package sample.Databases;

import javafx.scene.control.Alert;
import sample.model.Task;
import sample.model.User;

import java.sql.*;

public class DBHandler extends Config {
    Connection dbConnection;

    public Connection getDBConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?serverTimezone=EST";
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void newAccount(User newUser) throws SQLException, ClassNotFoundException {
        String strQuery = "INSERT INTO " + Const.users_table + "(" + Const.user_firstname + "," +
                            Const.user_lastname + "," + Const.user_username + "," + Const.user_password
                            + "," + Const.user_gender +")" + "VALUES(?,?,?,?,?)";

        PreparedStatement preparedStatement = getDBConnection().prepareStatement(strQuery);

        preparedStatement.setString(1, newUser.getFirstName());
        preparedStatement.setString(2, newUser.getLastName());
        preparedStatement.setString(3, newUser.getUserName());
        preparedStatement.setString(4, newUser.getPassword());
        preparedStatement.setString(5, newUser.getGender());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public ResultSet readUser(int userId){
        ResultSet resultSet = null;
        String strQry = "SELECT * FROM " + Const.users_table + " WHERE " +
                Const.user_id + "=?";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(strQry);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;

    }
    public ResultSet readUser(User user) {
        ResultSet resultSet = null;
        if (!user.getUserName().equals("") || !user.getPassword().equals("")){
            String strQry = "SELECT * FROM " + Const.users_table + " WHERE " +
                    Const.user_username + "=?" + " AND " + Const.user_password +
                    "=?";
            PreparedStatement preparedStatement;
            try {
                preparedStatement = getDBConnection().prepareStatement(strQry);
                preparedStatement.setString(1,user.getUserName());
                preparedStatement.setString(2,user.getPassword());
                resultSet = preparedStatement.executeQuery();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Fields");
            alert.setContentText("Please complete the necessary information.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        return resultSet;
    }
    public void addTask(Task task)  {
        String strQuery = "INSERT INTO " + Const.tasks_table + "(" + Const.user_id + "," + Const.tasks_task + "," +
                Const.tasks_datemodified + "," + Const.tasks_description + "," + Const.tasks_urgency +")" + "VALUES(?,?,?,?,?)";

        PreparedStatement preparedStatement;

        try {

            preparedStatement = getDBConnection().prepareStatement(strQuery);

            preparedStatement.setInt(1,task.getUserId());
            preparedStatement.setString(2, task.getTask());
            preparedStatement.setTimestamp(3, task.getDateModified());
            preparedStatement.setString(4, task.getDescription());
            preparedStatement.setString(5, task.getUrgency());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public int getTasks(int userID) throws SQLException, ClassNotFoundException {
        String strQry = "SELECT COUNT(*) FROM " + Const.tasks_table + " WHERE " +
                Const.user_id + "=?";


        PreparedStatement preparedStatement = getDBConnection().prepareStatement(strQry);
        preparedStatement.setInt(1,userID);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            return resultSet.getInt(1);
        }
        return resultSet.getInt(1);

    }
    public ResultSet getTasksByUser(int userId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;
        String strQry = "SELECT * FROM " + Const.tasks_table + " WHERE " +
                Const.user_id + "=?";

        PreparedStatement preparedStatement = getDBConnection().prepareStatement(strQry);
        preparedStatement.setInt(1, userId);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }
    public void deleteTask(int userid, int taskid) throws SQLException, ClassNotFoundException {
        String qry = "DELETE FROM " + Const.tasks_table + " WHERE " + Const.user_id + "=?"
                    + " AND " + Const.tasks_tasksid + "=?";

        PreparedStatement preparedStatement = getDBConnection().prepareStatement(qry);
        preparedStatement.setInt(1, userid);
        preparedStatement.setInt(2,taskid);
        preparedStatement.execute();
        preparedStatement.close();

    }
    public void updateTask(String task, Timestamp datemodified, String description, String urgency,  int taskId) throws SQLException, ClassNotFoundException {

        String query = "UPDATE tasks SET task=?, datemodified=?, description=?, urgency=? WHERE tasksid=?";


        PreparedStatement preparedStatement = getDBConnection().prepareStatement(query);
        preparedStatement.setString(1, task);
        preparedStatement.setTimestamp(2, datemodified);
        preparedStatement.setString(3, description);
        preparedStatement.setString(4, urgency);
        // preparedStatement.setInt(4, userId);
        preparedStatement.setInt(5, taskId);
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

}
