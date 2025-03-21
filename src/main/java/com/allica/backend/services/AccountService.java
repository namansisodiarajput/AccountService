package com.allica.backend.services;

import com.allica.backend.entities.AccountEntity;
import com.allica.backend.mappers.AccountMapper;
import com.allica.backend.repositories.AccountRepository;
import com.allica.backend.services.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public UUID createAccount(Account account) {
        AccountEntity accountEntity = AccountEntity.builder()
                .accountHolderName(account.getAccountHolderName())
                .accountNumber(account.getAccountNumber())
                .ifscCode(account.getIfscCode())
                .accountType(account.getAccountType())
                .balance(account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        return accountRepository.save(accountEntity).getAccountId();
    }

    public Optional<Account> getAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .map(AccountMapper::toDto);
    }
}
