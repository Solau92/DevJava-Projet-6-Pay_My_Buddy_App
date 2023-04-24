package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.User;

import java.util.List;

public interface TransferService {

	void saveTransfer(TransferDto transferDto);

	List<TransferDto> findAllUsersTransfers(User user);

}
