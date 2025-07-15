package model;

public class Admin extends User {


    public Admin(String userName, String password, String role){
        super(userName, password, role);
    }

    //متد مخصوص برای مشاهده کاربران model.Admin
    public void viewUsers(){
        System.out.println("model.Admin can view users.");
    }
}
