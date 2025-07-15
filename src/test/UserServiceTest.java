package test;

import model.User;
import service.UserService;

import java.util.List;

public class UserServiceTest {
    public static void main(String[] args) {

        UserServiceTest test = new UserServiceTest();

        System.out.println("+++++     Test Register     +++++\n");
        test.testRegisterUser();
        System.out.println("\n=============================\n");
        System.out.println("+++++     Test delete     +++++\n");
        test.testDeleteUser();
        System.out.println("\n=============================\n");
        System.out.println("+++++     Test Update     +++++\n");
        test.testUpdateUser();
        System.out.println("\n=============================\n");
        System.out.println("+++++     Test Change Password     +++++\n");
        test.testChangePassword();
        System.out.println("\n=============================\n");
        System.out.println("+++++     Test Login     +++++\n");
        test.testLoginUser();
        System.out.println("\n=============================\n");
        System.out.println("+++++     Test isAdmin     +++++\n");
        test.testIsAdmin();
        System.out.println("\n=============================\n");
        System.out.println("+++++     Test getAllUsers     +++++\n");
        test.testGetAllUsers();
        System.out.println("\n=============================\n");
        System.out.println("+++++     Test getUserByUsername     +++++\n");
        test.testGetUserByUsername();
    }



    //تست متد ثبت نام کاربر

    public void testRegisterUser() {
        UserService userService = new UserService();

        //سناریو 1: ثبت موفق

        User validUser = new User("hossein123", "securePass", "user");

        boolean result1 = userService.registerUser(validUser);
        System.out.println(result1 ? "Test Passed: valid user registered successfully." :
                "Test Failed: valid user should be registered.");


        //سناریو 2: نام کاربری خالی

        User emptyUsername = new User(" ", "somePass", "user");
        boolean result2 = userService.registerUser(emptyUsername);
        System.out.println(!result2 ? "Test Passed: Empty username rejected." :
                "Test Failed: Empty username should not be accepted.");

        //سناریو 3: رمز عبور کوتاه

        User shortPassword = new User("userShortPass", "123", "user");
        boolean result3 = userService.registerUser(shortPassword);
        System.out.println(!result3 ? "Test Passed: Short password rejected.":
                "Test Failed: Short password should npt be accepted.");


        //سناریو 4: نام کاربری تکراری

        User duplicateuser = new User("hossein123", "anotherPass", "user");
        boolean result4 = userService.registerUser(duplicateuser);
        System.out.println(!result4 ? "Test Passed: Duplicate username rejected." :
                "Test Failed: Duplicate username should not be accepted.");
    }

    //تست متد حذف کاربر

    public void testDeleteUser(){
        UserService userService = new UserService();

        //ثبت یک مدیر برای حذف کردن کاربر

        User admin = new User("adminTest", "adminPass", "admin");
        userService.registerUser(admin);

        //اضافه کردن کاربر نمونه برای حذف

        User user = new User("testUser", "testPassword", "user");
        userService.registerUser(user);


        //حذف کاربر و بررسی نتیجه

        boolean isDeleted = userService.deleteUser("adminTest", "adminPass",
                "testUser");
        if (isDeleted){
            System.out.println("Delete Your Test Passed!");
        }else{
            System.out.println("Delete Your Test  Failed!");
        }
    }


    //تست متد بروزرسانی کاربر

    public void testUpdateUser(){
        UserService userService = new UserService();


        //اضافه کردن کاربر نمونه برای تست

        User user = new User("updateMe", "pass123", "user");
        userService.registerUser(user);

        //بروز رسانی کاربر و بررسی نتیجه

        boolean isUpdate = userService.updateUser("updateMe", "pass123",
                "updateMe", "newPass456", "admin");
        if (isUpdate){
            System.out.println("Update User Test Passed!");
        }else {
            System.out.println("Update User Test Failed!");
        }
    }

    // تست متد تغییر رمز عبور

    public void testChangePassword() {
        UserService userService = new UserService();

        // ثبت کاربر

        User user = new User("changePassUser", "oldPass", "user");
        userService.registerUser(user);

        //سناریو 1: رمز درست وارد شده

        boolean result1 = userService.changePassword("changePassUser", "oldPass",
                "newPass123");
        if (result1) {
            System.out.println("Change Password Test Passed (valid user)!");
        } else {
            System.out.println("Change Password Test Failed (valid user)!");
        }

        // سناریو 2: رمز اشتباه

        boolean result2 = userService.changePassword("changePassUser",
                                                    "wrongOldPass",
                                                    "anotherPass");
        if (!result2) {
            System.out.println("Change Password Test Passed (invalid password)!");
        } else {
            System.out.println("Change Password Test Failed (invalid password)!");
        }
    }

    //تست متد لاگین کاربر

    public void testLoginUser(){

        UserService userService = new UserService();

        //ثبت کاربر برای تست لاگین

        User testUser = new User("loginUser", "loginPass", "user");
        userService.registerUser(testUser);

        //سناریو 1: لاگین موفق

        User loggedInUser1 = userService.loginUser("loginUser", "loginPass");
        System.out.println(loggedInUser1 != null ? "Login Test Passed (valid credentials) " :
                "Login the Failed (valid credentials) ");




        //سناریو 2: لاگین بارمز اشتباه

        User loggedInUser2 = userService.loginUser("loginUser", "wrongPass");
        System.out.println(loggedInUser2 == null ? "Login Test Passed (wrong password)" :
                "Login Test Failed (wrong password)");


        //لاگین با نام کاربری اشتباه

        User loggedInUser3 = userService.loginUser("wrongUser", "loginPass");
        System.out.println(loggedInUser3 == null ? "Login Test Passed (wrong username)":
                "Login Test Failed (wrong username)");
    }


    public void testIsAdmin(){
        UserService userService = new UserService();


        //ثبت کاربر ادمین

        User admin = new User("adminCheck", "admin123", "admin");
        userService.registerUser(admin);

        //ثبت کاربر معمولی

        User user = new User("normalUser", "user123", "user");
        userService.registerUser(user);

        //سناریو 1: کاربر ادمین

        boolean resul1 = userService.isAdmin("adminCheck", "admin123");
        System.out.println(resul1 ? "Test Passed: Admin user detected correctly." :
                "Test Failed: Admin user not detected. ");


        //سناریو 2: کاربر عادی

        boolean result2 = userService.isAdmin("normalUser", "user123");
        System.out.println(!result2 ? "Test Passed: Non-admin user correctly rejected." :
                "Test Failed: Non-admin user incorrectly accepted as admin.");


        //سناریو 3: اطلاعات اشتباه

        boolean result3 = userService.isAdmin("unKnowUser", "wrongPass");
        System.out.println(!result3 ? "Test Passed: Invalid user correctly reject." :
                "Test Failed: Invalid user incorrectly accept as admin.");
    }

    // تست متد getAllUsers

    public void testGetAllUsers(){
        UserService userService = new UserService();


        //ثبت یک کاربر ادمین

        User admin = new User("adminList", "admin123", "admin");
        userService.registerUser(admin);

        //ثبت چند کاربر نمونه

        userService.registerUser(new User("userA", "passA123", "user"));
        userService.registerUser(new User("userB", "passB123", "user"));

        //تست گرفتن لیست با اطلاعات صحیح ادمین

        List<User> users = userService.getAllUsers("adminList", "admin123");

        if(users.size() >= 3){
            System.out.println("Test Passed: Admin can see all users.");
        }else{
            System.out.println("Test Failed: Admin could not see all users.");
        }


        //ورود با اطلاعات کاربر غیر ادمین

        User normalUser = new User("normalGuy", "userPass", "user");
        userService.registerUser(normalUser);


         List<User> deniedList = userService.getAllUsers("normalGuy", "userPass");
         if (deniedList.isEmpty()){
             System.out.println("Test Passed: Non-admin user denied access.");
         }else {
             System.out.println("Test Failed: Non-admin user should not access the list.");
         }

         //ورود با اطلاعات غلط

        List<User> wrongCredList = userService.getAllUsers("adminList", "wrongPass");
         if (wrongCredList.isEmpty()){
             System.out.println("Test Passed: Access denied for wrong credentials.");
         }else {
             System.out.println("Test Failed: Wrong credentials should not get user list.");
         }
    }

    public void testGetUserByUsername(){
        UserService userService = new UserService();

        //ثبت کاربر
        User user = new User("findMe", "findPass", "user");
        userService.registerUser(user);


        try{
            User foundUser = userService.getUserByUsername("findMe");
            if (foundUser != null &&
            foundUser.getUserName().equalsIgnoreCase("findMe") &&
            foundUser.getPassword().equalsIgnoreCase("findPass") &&
            foundUser.getRole().equalsIgnoreCase("user")){
                System.out.println("Test Passed: user found successfully.");
            }else {
                System.out.println("Test Failed: Could not find existing user.");
            }


            //کاربر غیر موجود

            User nonExistent = userService.getUserByUsername("noSuchUser");
            if (nonExistent == null){
                System.out.println("Test Passed: Non-existing user correctly returned null.");
            }else {
                System.out.println("Test Failed: Should return null for non-existing user.");
            }
        }catch (Exception e){
            System.out.println("Test Failed with exception : " + e.getMessage());
        }
    }
}









