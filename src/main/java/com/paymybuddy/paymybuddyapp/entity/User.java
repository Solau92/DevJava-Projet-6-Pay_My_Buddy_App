package com.paymybuddy.paymybuddyapp.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@DynamicUpdate
public class User {

	@OneToMany(
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.EAGER,
			mappedBy = "creditor" // rajouté
	)
//	@JoinColumn(name = "id")
	List<Transfer> transfersReceived = new ArrayList<>();
	@OneToMany(
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.EAGER,
			mappedBy = "debtor" // rajouté
	)
//	@JoinColumn(name = "id")
	List<Transfer> transfersDone = new ArrayList<>();
	@ManyToMany(
			fetch = FetchType.LAZY
	)
	@JoinTable(
			name = "contact",
			joinColumns = @JoinColumn(name = "user"),
			inverseJoinColumns = @JoinColumn(name = "friend")
			// unique = true
	)
	List<User> contacts = new ArrayList<>(); // Set
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
	@Column
	private double accountBalance;

	public User() {
	}

	public User(Integer id, String email, String password, String firstname, String lastname, double accountBalance, List<Transfer> transfersReceived, List<Transfer> transfersDone, List<User> contacts) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.accountBalance = accountBalance;
		this.transfersReceived = transfersReceived;
		this.transfersDone = transfersDone;
		this.contacts = contacts;
	}

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

	public List<Transfer> getTransfersReceived() {
		return transfersReceived;
	}

	public void setTransfersReceived(List<Transfer> transfersReceived) {
		this.transfersReceived = transfersReceived;
	}

	public List<Transfer> getTransfersDone() {
		return transfersDone;
	}

	public void setTransfersDone(List<Transfer> transfersDone) {
		this.transfersDone = transfersDone;
	}

	public List<User> getContacts() {
		return contacts;
	}

	public void setContacts(List<User> contacts) {
		this.contacts = contacts;
	}

	public void printContact() {
		for (User u : this.getContacts()
		) {
			System.out.print(u.getEmail());
		}
	}

	public void printTransfersDone() {
		int compteur = 0;
		for (Transfer t : this.getTransfersDone()) {
			System.out.println(compteur + ") amount : " + t.getAmount() + " reason : " + t.getReason() + " - ");
			compteur++;
		}
	}

}
