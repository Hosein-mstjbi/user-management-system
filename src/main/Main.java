package main;


import model.User;
import service.UserService;

import java.util.Scanner;

public class Main {


    public static void showAdminMenu(Scanner scanner, UserService userService) {

        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. List Users");
            System.out.println("2. Delete User");
            System.out.println("3. Update User");
            System.out.println("4. Logout");


            System.out.println("Choose an option : ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {

                case 1:

                    System.out.println("Enter your username : ");
                    String adminUsername = scanner.nextLine();

                    System.out.println("Enter tour password : ");
                    String adminPassword = scanner.nextLine();

                    userService.getAllUsers(adminUsername, adminPassword).forEach(System.out::println);
                    break;

                case 2:
                    System.out.println("Enter your admin username : ");
                    String username = scanner.nextLine();

                    System.out.println("inter your admin password : ");
                    String password = scanner.nextLine();

                    System.out.println("Enter username to delete : ");
                    String toDelete = scanner.nextLine();

                    if (userService.deleteUser(username, password, toDelete)) {
                        System.out.println("user deleted successfully!");
                    } else {
                        System.out.println("Error deleting user.");
                    }
                    break;

                case 3:

                    System.out.println("Enter your user name : ");
                    String requestUsername = scanner.nextLine();

                    System.out.println("Enter your password : ");
                    String pass = scanner.nextLine();

                    System.out.println("Enter target username to update : ");
                    String target = scanner.nextLine();

                    System.out.println("Enter new password : ");
                    String newPass = scanner.nextLine();

                    System.out.println("Enter new role : ");
                    String newRole = scanner.nextLine();

                    if (userService.updateUser(requestUsername, pass, target, newPass, newRole)) {
                        System.out.println("user update successfully!");
                    } else {
                        System.out.println("Error updating user.");
                    }
                    break;

                case 4:
                    System.out.println("Logging out ...");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static void showUserMenu(Scanner scanner, UserService userService, String username) {

        System.out.println("\n === User Menu === ");

        System.out.println("1. Change password");
        System.out.println("2. Logout");

        System.out.println("choose an option");

        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {

            case 1:

                System.out.println("Enter your current password : ");
                String currentPassword = scanner.nextLine();

                System.out.println("Enter your new password : ");
                String newPass = scanner.nextLine();

                if (userService.changePassword(username, currentPassword, newPass)) {
                    System.out.println("Password changed successfully!");
                } else {
                    System.out.println("Error changing password.");
                }
                break;

            case 2:

                System.out.println("Logging out ...");
                return;
            default:

                System.out.println("Invalid option.");
        }
    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();

        while (true) {

            System.out.println("\n=== User Management System ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("4. Exite");
            System.out.println("choose an option : ");


            int option = scanner.nextInt();
            scanner.nextLine();


            switch (option) {

                case 1:
                    System.out.println("Enter your user name : ");
                    String username = scanner.nextLine();

                    System.out.println("Enter your password : ");
                    String password = scanner.nextLine();

                    System.out.println("Enter your role(user / admin)");
                    String role = scanner.nextLine();

                    User user = new User(username, password, role);
                    if (userService.registerUser(user)) {
                        System.out.println("User registered successfully! ");
                    } else {
                        System.out.println("Error registering user.");
                    }
                    break;

                case 2:

                    System.out.println("Enter username : ");
                    String loginUsername = scanner.nextLine();

                    System.out.println("Enter password : ");
                    String loginPassword = scanner.nextLine();


                    User logedInUser = userService.loginUser(loginUsername, loginPassword);
                    if (logedInUser != null) {
                        System.out.println("Welcome , " + logedInUser.getRole() + " " +
                                logedInUser.getUserName() + "!");
                        if (logedInUser.getRole().equalsIgnoreCase("admin")) {
                            showAdminMenu(scanner, userService);
                        } else {
                            showUserMenu(scanner, userService, logedInUser.getUserName());
                        }
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                    break;

                case 3:
                    System.out.println("Enter your username: ");
                    String forgetUsername = scanner.nextLine();

                    System.out.println("Enter your new password: ");
                    String resetPassword = scanner.nextLine();

                    if (userService.forgetPassword(forgetUsername, resetPassword)) {
                        System.out.println("Password reset successfully!");
                    } else {
                        System.out.println("Password reset failed.");
                    }
                    break;

                case 4:
                    System.out.println("Exiting ...");
                    return;
                default:
                    System.out.println("Invalid option. pleas try again.");
            }
        }
    }
}