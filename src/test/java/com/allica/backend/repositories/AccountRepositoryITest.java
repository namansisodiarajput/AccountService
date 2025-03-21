package com.allica.backend.repositories;

import com.allica.backend.entities.AccountEntity;
import com.allica.backend.enums.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryITest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void saveAccount_Success() {
        // Given
        AccountEntity accountEntity = AccountEntity.builder()
                .accountHolderName("John Doe")
                .accountNumber("123456789012")
                .ifscCode("ABC12345678")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1000))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();  // No manual accountId assignment

        // When
        AccountEntity savedAccount = accountRepository.save(accountEntity);

        // Then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getAccountId()).isNotNull();  // Hibernate will generate this ID
        assertThat(savedAccount.getAccountHolderName()).isEqualTo("John Doe");
        assertThat(savedAccount.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    void findById_ReturnsAccount() {
        // Given
        AccountEntity accountEntity = AccountEntity.builder()
                .accountHolderName("Alice Johnson")
                .accountNumber("987654321098")
                .ifscCode("XYZ87654321")
                .accountType(AccountType.CURRENT)
                .balance(BigDecimal.valueOf(5000))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        AccountEntity savedAccount = accountRepository.save(accountEntity);  // Save and get the auto-generated ID

        // When
        Optional<AccountEntity> fetchedAccount = accountRepository.findById(savedAccount.getAccountId());  // Use generated ID

        // Then
        assertThat(fetchedAccount).isPresent();
        assertThat(fetchedAccount.get().getAccountHolderName()).isEqualTo("Alice Johnson");
        assertThat(fetchedAccount.get().getAccountType()).isEqualTo(AccountType.CURRENT);
    }


    @Test
    void findById_NotFound_ReturnsEmpty() {
        // When
        Optional<AccountEntity> fetchedAccount = accountRepository.findById(UUID.randomUUID());

        // Then
        assertThat(fetchedAccount).isEmpty();
    }

    @Test
    void updateAccount_Success() {
        // Given
        AccountEntity accountEntity = AccountEntity.builder()
                .accountHolderName("Mark Wayne")
                .accountNumber("123456000000")
                .ifscCode("LMN00011122")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(2000))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        AccountEntity savedEntity = accountRepository.save(accountEntity);

        // Fetch the entity from the DB again to avoid stale state issues
        AccountEntity fetchedEntity = accountRepository.findById(savedEntity.getAccountId()).orElseThrow();

        // When - Update the balance and account holder name
        fetchedEntity.setBalance(BigDecimal.valueOf(5000));
        fetchedEntity.setAccountHolderName("Mark Wayne Updated");
        accountRepository.save(fetchedEntity);

        // Fetch the updated account and assert changes
        Optional<AccountEntity> updatedAccount = accountRepository.findById(fetchedEntity.getAccountId());

        // Then
        assertThat(updatedAccount).isPresent();
        assertThat(updatedAccount.get().getAccountHolderName()).isEqualTo("Mark Wayne Updated");
        assertThat(updatedAccount.get().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(5000));
    }

    @Test
    void deleteAccount_Success() {
        // Given
        AccountEntity accountEntity = AccountEntity.builder()
                .accountHolderName("Charlie Brown")
                .accountNumber("112233445566")
                .ifscCode("OPQ33344455")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(3000))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        AccountEntity savedAccount = accountRepository.save(accountEntity);  // Save and get auto-generated ID

        // When
        accountRepository.deleteById(savedAccount.getAccountId());  // Use the generated ID
        Optional<AccountEntity> deletedAccount = accountRepository.findById(savedAccount.getAccountId());

        // Then
        assertThat(deletedAccount).isEmpty();
    }

}
