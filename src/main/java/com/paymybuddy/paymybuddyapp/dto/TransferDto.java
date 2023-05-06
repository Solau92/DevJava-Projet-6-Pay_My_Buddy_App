package com.paymybuddy.paymybuddyapp.dto;

import java.time.LocalDate;

public class TransferDto {

	private Integer id;
	private LocalDate date;
	private double amount;
	private String reason;
	private Integer creditor;
	private String creditorEmail;
	private Integer debtor;

	public TransferDto() {
	}

	public TransferDto(Integer id, LocalDate date, double amount, String reason, Integer creditor, Integer debtor) {
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.reason = reason;
		this.creditor = creditor;
		this.debtor = debtor;
	}

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

	public String getCreditorEmail() {
		return creditorEmail;
	}

	public void setCreditorEmail(String creditorEmail) {
		this.creditorEmail = creditorEmail;
	}

}
