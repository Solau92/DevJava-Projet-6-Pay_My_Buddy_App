package com.paymybuddy.paymybuddyapp.dto;


import org.springframework.lang.NonNull;

public class UserDto {

    public UserDto() {
    }

    public UserDto(Integer id, String email, String password, String firstname, String lastname, double accountBalance) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.accountBalance = accountBalance;
    }

    private Integer id;

//    @NotEmpty
    private String email;

    //    @NotEmpty
    private String password;

//    @NotEmpty
    private String firstname;

    //    @NotEmpty
    private String lastname;

    private double accountBalance;

    public Integer getId() {return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
