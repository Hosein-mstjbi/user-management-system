package model;

public class User {

    private String userName;
    private String password;
    private String role; //نقش کاربر: "user" یا "admin"



    //سازنده
    public User(String userName, String password, String role){
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    //گتر و ستر برای password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //گتر و ستر برای username
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    //گتر و ستر برای role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //متد برای نمایش اطلاعات کاربر(رمز عبور نمایش داده نمیشود)
    public String toString(){
        return "model.User{userName :" + userName + " , " + "role :" + role + "}";
    }
}
