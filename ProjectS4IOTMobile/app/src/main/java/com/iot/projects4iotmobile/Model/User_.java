package com.iot.projects4iotmobile.Model;


public class User_ {
    private String firstName ;
    private String lastName;
    private String phone;
    private String userName;
    private String password;


    public User_(String firstName,String lastName,String phone,String userName,String password) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.userName=userName;
        this.password=password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
