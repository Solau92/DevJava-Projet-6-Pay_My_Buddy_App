package com.paymybuddy.paymybuddyapp.repository;

import com.paymybuddy.paymybuddyapp.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {

/*    Transfer findByDebtor(String email);

    Transfer findByCreditor(String email);*/

}
