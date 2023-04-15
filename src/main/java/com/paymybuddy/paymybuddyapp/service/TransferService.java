package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;

import java.util.List;

public interface TransferService {

	void saveTransfer(TransferDto transferDto);

	List<TransferDto> findAllUsersTransfers(String email);

}
