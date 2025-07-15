package service;

import db.DatabaseConnection;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {


    //متد ثبت نام کاربر

    public boolean registerUser(User user)  {

        String username = user.getUserName();
        String password = user.getPassword();

        //بررسی خالی نبودن نام کاربری

        if (username == null || username.trim().isEmpty()) {
            System.out.println("Registration failed : Username can not be empty.");
            return false;
        }

        //ررسی طول مناسب رمز عبور

        if (password == null || password.length() < 6) {
            System.out.println("Registration failed : Password must be at least 6 characters.");
            return false;
        }

        //بررسی تکراری نبودن نام کاربری

        try {
            if (getUserByUsername(username) != null) {
                System.out.println("Registration failed : username " + username + " is already taken.");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //اجرای ثبت نام

        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, user.getRole());

            int rowsInserted  = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Error while registering user: " + e.getMessage());
            return false;
        }
    }

    //متد خواندن کاربر فقط برای مدیر سیستم(Admin)

    public List<User> getAllUsers(String username, String password) {

        List<User> users = new ArrayList<>();

        //بررسی اینکه ادمین هست یا کاربر

        if (!isAdmin(username, password)){
            System.out.println("Access denied : Only Admin can view all users");
            return users;
        }

        String query = "SELECT username, password, role FROM users";


        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            //خواندن کاربران از دیتابیس و ساخت لیست

            while (resultSet.next()) {
                String user = resultSet.getString("username");
                String pass = resultSet.getString("password");
                String role = resultSet.getString(("role"));
                users.add(new User(user, pass, role));
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching users: " + e.getMessage());
        }
        return users;
    }

    //متد تشخیص role

    public boolean isAdmin(String userName, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    return "admin".equalsIgnoreCase(role);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while checking admin: " + e.getMessage());
        }
        return false;
    }

    //حذف کاربر از دیتابیس فقط برای ادمین مجاز است

    public boolean deleteUser(String adminUsername, String adminPassword, String targetUsername) {

        //بررسی اینکه آیااین درخواست حذف از سمت یک ادمین هست یا نه

        if (isAdmin(adminUsername, adminPassword)){
            System.out.println("Access deniedL : Only admin can delete users.");
            return false;
        }

        String query = "DELETE FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, targetUsername);
            int rowsDeleted  = preparedStatement.executeUpdate();
            return rowsDeleted  > 0;
        } catch (SQLException e) {
            System.out.println("Enter while deleting user: " + e.getMessage());
            return false;
        }
    }

    //بروز رسانی اطلاعات کاربر - فقط خودش یا ادمین میتواند این کار را انجام دهد

    public boolean updateUser(String requesterUsername, String requesterPassword, String targetUsername,
                              String newPassword, String newRole) {

        //اگر کاربر ادمین نیست و میخواهد کسی را آپدیت کند -> اجازه ندارد

        if (!requesterUsername.equals(targetUsername) && !isAdmin(requesterUsername, requesterPassword)){
            System.out.println("Access denied : You can only update your own profile unless you are an admin.");
            return false;
        }

        String query = "UPDATE users SET password = ?, role = ? WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, newRole);
            preparedStatement.setString(3, targetUsername);

            int rowAffected = preparedStatement.executeUpdate();
            return rowAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error while updating user : " + e.getMessage());

            return false;
        }
    }

    //تغییر رمز عبور -> فقط توسط خود کاربر مجاز است

    public boolean changePassword(String username, String password, String newPassword) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            //بررسی وجود کاربر

            User user = getUserByUsername(username);
            if (user == null) {
                System.out.println("user not found .");
                return false;
            }

            //بررسی اینکه رمز فعلی درست هست یا نه

            if (!user.getPassword().equals(password)){
                System.out.println("Incorrect current password.");
                return false;
            }


            //به روز رسانی رمز عبور

            String query = "UPDATE users SET password = ? WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, username);

                int roeUpdate = preparedStatement.executeUpdate();
                return roeUpdate > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error while changing password: " + e.getMessage());
            return false;
        }
    }

    //نمایش کاربر با استفاده از نام کاربری
    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    return new User(
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role"));
                } else {
                    System.out.println("User not found with username: " + username);
                    return null;
                }
            }
        }
    }

    //متد ورود کاربر

    public User loginUser(String username, String password){
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    //اگر یوزر پیدا شد، اطلاعاتش به صورت user برمیگرده
                    return new User(
                            resultSet.getString("username"),
                            null,
                            resultSet.getString("role"));
                }
            }
        }catch (SQLException e){
            System.out.println("Error while logging in : " + e.getMessage());
        }
        return null; // در صورت نامعتبر بودن اطلاعات
    }

    //بازنشانی رمز عبور برای کاربری که رمز عبورش را فراموش کرده

    public boolean forgetPassword(String username, String newPassword){
        try(Connection connection = DatabaseConnection.getConnection()){

            //بررسی وجود کاربر

            User user = getUserByUsername(username);
            if (user != null){
                System.out.println("User not hound with username : " + user.getUserName());
                return false;
            }

            //بروز رسانی رمز عبور

            String query = "UPDATE  users SET password = ? WHERE username = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, username);


                int rowUpdate = preparedStatement.executeUpdate();
                return rowUpdate > 0;
            }
        }catch (SQLException e){
            System.out.println("Error while resetting password : " + e.getMessage());
            return false;
        }
    }
}
