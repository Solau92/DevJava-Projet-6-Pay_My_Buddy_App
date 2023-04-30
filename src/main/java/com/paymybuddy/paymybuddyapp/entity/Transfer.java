package com.paymybuddy.paymybuddyapp.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="transfer")
public class Transfer {

    public Transfer() {
    }

    public Transfer(Integer id, LocalDate date, double amount, String reason, Integer creditor, Integer debtor) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.reason = reason;
        this.creditor = creditor;
        this.debtor = debtor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private LocalDate date;

    @Column
    private double amount;

    @Column
    private String reason;

    @Column(nullable = false)
    private Integer creditor;

    @Column(nullable = false)
    private Integer debtor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getCreditor() {
        return creditor;
    }

    public void setCreditor(Integer creditor) {
        this.creditor = creditor;
    }

    public Integer getDebtor() {
        return debtor;
    }

    public void setDebtor(Integer debtor) {
        this.debtor = debtor;
    }

}
