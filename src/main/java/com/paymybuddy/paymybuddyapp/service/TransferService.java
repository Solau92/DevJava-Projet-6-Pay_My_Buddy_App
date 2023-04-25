package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;

import java.util.List;

public interface TransferService {

	void saveTransfer(TransferDto transferDto);

	List<TransferDto> findAllUsersTransfers(User user);

	abstract boolean isAccountBalanceSufficient(TransferDto transferDto, double accountBalance) throws InsufficientBalanceException;
}
