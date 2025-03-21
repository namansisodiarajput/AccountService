package com.allica.backend.services;

import com.allica.backend.entities.AccountEntity;
import com.allica.backend.entities.TransactionEntity;
import com.allica.backend.enums.TransactionType;
import com.allica.backend.exceptions.AccountException;
import com.allica.backend.exceptions.InsufficientBalanceException;
import com.allica.backend.repositories.AccountRepository;
import com.allica.backend.repositories.TransactionRepository;
import com.allica.backend.services.TransactionService;
import com.allica.backend.services.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceUTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    private UUID accountId;
    private AccountEntity accountEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountId = UUID.randomUUID();
        accountEntity = AccountEntity.builder()
                .accountId(accountId)
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    @Test
    void createTransaction_credit_success() {
        Transaction transaction = Transaction.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(200))
                .transactionType(TransactionType.CREDIT)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(TransactionEntity.builder()
                .transactionId(UUID.randomUUID())
                .build());

        UUID transactionId = transactionService.createTransaction(transaction);

        assertNotNull(transactionId);
        assertEquals(new BigDecimal("1200"), accountEntity.getBalance());
    }

    @Test
    void createTransaction_debit_success() {
        Transaction transaction = Transaction.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(500))
                .transactionType(TransactionType.DEBIT)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(TransactionEntity.builder()
                .transactionId(UUID.randomUUID())
                .build());

        UUID transactionId = transactionService.createTransaction(transaction);

        assertNotNull(transactionId);
        assertEquals(new BigDecimal("500"), accountEntity.getBalance());
    }

    @Test
    void createTransaction_throwsInsufficientBalanceException() {
        Transaction transaction = Transaction.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(2000))
                .transactionType(TransactionType.DEBIT)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class,
                () -> transactionService.createTransaction(transaction));
        assertEquals("Insufficient balance for the debit transaction.", exception.getMessage());
    }

    @Test
    void createTransaction_throwsAccountException() {
        UUID invalidAccountId = UUID.randomUUID();
        Transaction transaction = Transaction.builder()
                .accountId(invalidAccountId)
                .amount(BigDecimal.valueOf(100))
                .transactionType(TransactionType.CREDIT)
                .build();

        when(accountRepository.findById(invalidAccountId)).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.createTransaction(transaction));
        assertEquals("Account with ID " + invalidAccountId + " not found", exception.getMessage());
    }
}
