package com.paymybuddy.paymybuddyapp.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@DynamicUpdate
public class User {

    public User() {
    }

    public User(Integer id, String email, String password, String firstname, String lastname, double accountBalance, List<Transfer> transfers_received, List<Transfer> transfers_done, List<User> contacts) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.accountBalance = accountBalance;
        this.transfers_received = transfers_received;
        this.transfers_done = transfers_done;
        this.contacts = contacts;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(name="accountBalance")
    private double accountBalance;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id") // ????????
    List<Transfer> transfers_received = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "id")
    List<Transfer> transfers_done = new ArrayList<>();

    @ManyToMany(
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "contact",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "friend")
    )
    List<User> contacts = new ArrayList<>();

    public Integer getId() {
        return id;
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

    public List<Transfer> getTransfers_received() {
        return transfers_received;
    }

    public void setTransfers_received(List<Transfer> transfers_received) {
        this.transfers_received = transfers_received;
    }

    public List<Transfer> getTransfers_done() {
        return transfers_done;
    }

    public void setTransfers_done(List<Transfer> transfers_done) {
        this.transfers_done = transfers_done;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

    public void printContact(){
        for (User u : this.getContacts()
             ) {
            System.out.print(u.getEmail());
        }
    }
}
