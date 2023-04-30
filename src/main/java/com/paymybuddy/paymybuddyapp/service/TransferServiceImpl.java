package com.paymybuddy.paymybuddyapp.service;

import com.paymybuddy.paymybuddyapp.dto.TransferDto;
import com.paymybuddy.paymybuddyapp.entity.Transfer;
import com.paymybuddy.paymybuddyapp.entity.User;
import com.paymybuddy.paymybuddyapp.exception.AmountZeroException;
import com.paymybuddy.paymybuddyapp.exception.InsufficientBalanceException;
import com.paymybuddy.paymybuddyapp.repository.TransferRepository;
import jakarta.transaction.Transactional;
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

	/**
	 * Saves the transfer in database if the transfer attributes are valid.
	 * @param transferDto
	 * @throws AmountZeroException
	 */
	@Transactional
	@Override
	public void saveTransfer(User loggedUser, TransferDto transferDto) throws InsufficientBalanceException, Exception {

		Integer idLoggedUser = loggedUser.getId();
		transferDto.setDebtor(idLoggedUser);

		transferDto.setCreditor(userService.findUserByEmail(transferDto.getCreditorEmail()).getId());

		isAccountBalanceSufficient(transferDto, loggedUser.getAccountBalance());

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
		userService.addTransfer(transferDto);
	}

	/**
	 * Returns the list of TransferDto done by the User given in parameter.
	 * @param user
	 * @return list of TransferDto
	 */
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
		return transfersDto;
	}

	/** TODO : repasser en privé
	 * Returns true if the account balance is sufficient to make the transfer, throws an Exception if not
	 * @param transferDto
	 * @param accountBalance
	 * @return true if the account balance is sufficient to make the transfer
	 * @throws InsufficientBalanceException
	 */
	private boolean isAccountBalanceSufficient(TransferDto transferDto, double accountBalance) throws InsufficientBalanceException {

		if(transferDto.getAmount()*1.05 > accountBalance) {
			throw new InsufficientBalanceException();
		}
			return true;
	}

}
