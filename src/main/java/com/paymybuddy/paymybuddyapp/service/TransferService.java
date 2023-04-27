package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.AmountZeroException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;

import java.util.List;

public interface TransferService {

	void saveTransfer(TransferDto transferDto) throws AmountZeroException;

	List<TransferDto> findAllUsersTransfers(User user);

	boolean isAccountBalanceSufficient(TransferDto transferDto, double accountBalance) throws InsufficientBalanceException;
}
