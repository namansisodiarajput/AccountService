package com.allica.backend.services;

import com.allica.backend.entities.AccountEntity;
import com.allica.backend.entities.TransactionEntity;
import com.allica.backend.enums.TransactionType;
import com.allica.backend.exceptions.AccountException;
import com.allica.backend.exceptions.InsufficientBalanceException;
import com.allica.backend.mappers.TransactionMapper;
import com.allica.backend.repositories.AccountRepository;
import com.allica.backend.repositories.TransactionRepository;
import com.allica.backend.services.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public UUID createTransaction(Transaction transaction) {
        AccountEntity accountEntity = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new AccountException("Account with ID " + transaction.getAccountId() + " not found"));

        BigDecimal updatedBalance = calculateUpdatedBalance(accountEntity.getBalance(), transaction);

        accountEntity.setBalance(updatedBalance);

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .transactionDate(LocalDateTime.now())
                .accountEntity(accountEntity)
                .build();

        accountRepository.save(accountEntity);
        return transactionRepository.save(transactionEntity).getTransactionId();
    }

    private BigDecimal calculateUpdatedBalance(BigDecimal currentBalance, Transaction transaction) {
        switch (transaction.getTransactionType()) {
            case CREDIT:
                return currentBalance.add(transaction.getAmount());
            case DEBIT:
                if (currentBalance.compareTo(transaction.getAmount()) < 0) {
                    throw new InsufficientBalanceException("Insufficient balance for the debit transaction.");
                }
                return currentBalance.subtract(transaction.getAmount());
            default:
                throw new IllegalArgumentException("Invalid transaction type.");
        }
    }

    public List<Transaction> getTransactionsByAccountId(UUID accountId) {
        return transactionRepository.findByAccountEntityId(accountId)
                .stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList());
    }
}
