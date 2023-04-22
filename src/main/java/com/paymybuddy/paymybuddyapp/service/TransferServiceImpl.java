package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferServiceImpl implements TransferService {

	private TransferRepository transferRepository;

	private UserServiceImplement userServiceImplement;

	public TransferServiceImpl(TransferRepository transferRepository, UserServiceImplement userServiceImplement){
		this.transferRepository = transferRepository;
		this.userServiceImplement = userServiceImplement;
	}

	@Override
	public void saveTransfer(TransferDto transferDto) {
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
	public List<TransferDto> findAllUsersTransfers(String email) {
		List<Transfer> transfers = transferRepository.findAll();
		return transfers.stream()
				.map((transfer) -> mapToTransferDto(transfer))
				.collect(Collectors.toList());
		// TODO
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
}
