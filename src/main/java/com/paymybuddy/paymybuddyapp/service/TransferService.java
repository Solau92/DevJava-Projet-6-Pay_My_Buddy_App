package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.AmountZeroException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface TransferService {

	/**
	 * Saves the transfer in database if the transfer attributes are valid.
	 * @param transferDto
	 * @throws AmountZeroException
	 */
	@Transactional
	void saveTransfer(User loggedUser, TransferDto transferDto) throws Exception;

	/**
	 * Returns the list of TransferDto done by the User given in parameter.
	 * @param user
	 * @return list of TransferDto
	 */
	List<TransferDto> findAllUsersTransfers(User user);

}
