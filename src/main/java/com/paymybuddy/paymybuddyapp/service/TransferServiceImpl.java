package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.AmountZeroException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

	private TransferRepository transferRepository;

	private UserService userService;

	public TransferServiceImpl(TransferRepository transferRepository, UserService userService){
		this.transferRepository = transferRepository;
		this.userService = userService;
	}

	@Override
	public void saveTransfer(TransferDto transferDto) throws AmountZeroException {

		if(transferDto.getAmount() == 0) {
			throw new AmountZeroException();
		}
		Transfer transfer = new Transfer();
		transfer.setDate(transferDto.getDate());
		transfer.setAmount(transferDto.getAmount());
		transfer.setDebtor(transferDto.getDebtor());
		transfer.setCreditor(transferDto.getCreditor());
		transfer.setDate(transferDto.getDate());
		transfer.setReason(transferDto.getReason());
		transfer.setDate(LocalDate.now());
		transferRepository.save(transfer);
	}

	@Override
	public List<TransferDto> findAllUsersTransfers(User user) {
		List<Transfer> transfers = user.getTransfersDone();
		List<TransferDto> transfersDto = new ArrayList<>();

		for (Transfer t : transfers) {
			TransferDto transferDto = new TransferDto();
			transferDto.setDate(t.getDate());
			transferDto.setAmount(t.getAmount());
			transferDto.setDebtor(t.getDebtor());
			transferDto.setCreditor(t.getCreditor());
			transferDto.setCreditorEmail(userService.findUserEmailById(t.getCreditor()));
			transferDto.setDate(t.getDate());
			transferDto.setReason(t.getReason());
			transferDto.setDate(LocalDate.now());
			transfersDto.add(transferDto);
		}
/*		return transfers.stream()
				.map((transfer) -> mapToTransferDto(transfer))
				.collect(Collectors.toList());*/
		return transfersDto;
	}

	private TransferDto mapToTransferDto(Transfer transfer) {
		TransferDto transferDto = new TransferDto();
		transfer.setDate(transferDto.getDate());
		transfer.setAmount(transferDto.getAmount());
		transfer.setCreditor(transferDto.getCreditor());
		transfer.setDate(transferDto.getDate());
		transfer.setReason(transferDto.getReason());
		return transferDto;
	}

	@Override
	public boolean isAccountBalanceSufficient(TransferDto transferDto, double accountBalance) throws InsufficientBalanceException {

		if(transferDto.getAmount()*1.05 > accountBalance) {
			throw new InsufficientBalanceException();
		}
			return true;
	}
}
