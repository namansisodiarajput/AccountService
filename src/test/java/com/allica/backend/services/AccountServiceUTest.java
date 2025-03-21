package com.allica.backend.services;

import com.allica.backend.entities.AccountEntity;
import com.allica.backend.enums.AccountType;
import com.allica.backend.repositories.AccountRepository;
import com.allica.backend.services.AccountService;
import com.allica.backend.services.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceUTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private UUID accountId;
    private AccountEntity accountEntity;
    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        accountId = UUID.randomUUID();
        accountEntity = AccountEntity.builder()
                .accountId(accountId)
                .accountHolderName("John Doe")
                .accountNumber("123456789012")
                .ifscCode("IFSC1234567")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1000))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        account = Account.builder()
                .accountHolderName("John Doe")
                .accountNumber("123456789012")
                .ifscCode("IFSC1234567")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1000))
                .build();
    }

    @Test
    void createAccount_success() {
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        UUID createdAccountId = accountService.createAccount(account);

        assertNotNull(createdAccountId);
        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }

    @Test
    void getAccountById_success() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));

        Optional<Account> result = accountService.getAccountById(accountId);

        assertTrue(result.isPresent());
        assertEquals(accountEntity.getAccountHolderName(), result.get().getAccountHolderName());
        assertEquals(accountEntity.getAccountNumber(), result.get().getAccountNumber());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAccountById_notFound() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Optional<Account> result = accountService.getAccountById(accountId);

        assertFalse(result.isPresent());
        verify(accountRepository, times(1)).findById(accountId);
    }
}
