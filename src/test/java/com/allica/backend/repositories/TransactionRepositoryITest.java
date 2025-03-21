package com.allica.backend.repositories;

import com.allica.backend.entities.AccountEntity;
import com.allica.backend.entities.TransactionEntity;
import com.allica.backend.enums.AccountType;
import com.allica.backend.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TransactionRepositoryITest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private AccountEntity accountEntity;

    @BeforeEach
    void setUp() {
        // Create and save an account entity to be linked to transactions
        accountEntity = AccountEntity.builder()
                .accountHolderName("John Doe")
                .accountNumber("123456789012")
                .ifscCode("ABC12345678")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1000))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        accountRepository.save(accountEntity);
    }

    @Test
    void saveTransaction_Success() {
        // Given: Save the account entity and reload it to ensure it's managed by the persistence context
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        accountRepository.flush();  // Ensures the entity is fully persisted before use

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(BigDecimal.valueOf(500))
                .transactionType(TransactionType.CREDIT)
                .transactionDate(LocalDateTime.now())
                .accountEntity(savedAccount)  // Use the saved (managed) account entity
                .build();

        // When
        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        // Then
        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getTransactionId()).isNotNull();
        assertThat(savedTransaction.getTransactionType()).isEqualTo(TransactionType.CREDIT);
        assertThat(savedTransaction.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(500));
    }

    @Test
    void findByAccountEntityId_ReturnsTransactions() {
        // Given: Save and manage the accountEntity before associating it with transactions
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        accountRepository.flush();  // Ensure it's fully persisted

        TransactionEntity transaction1 = TransactionEntity.builder()
                .amount(BigDecimal.valueOf(200))
                .transactionType(TransactionType.DEBIT)
                .transactionDate(LocalDateTime.now())
                .accountEntity(savedAccount)  // Use saved (managed) accountEntity
                .build();

        TransactionEntity transaction2 = TransactionEntity.builder()
                .amount(BigDecimal.valueOf(100))
                .transactionType(TransactionType.CREDIT)
                .transactionDate(LocalDateTime.now())
                .accountEntity(savedAccount)  // Use saved (managed) accountEntity
                .build();

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        // When: Retrieve transactions by the account's ID
        List<TransactionEntity> transactions = transactionRepository.findByAccountEntityId(savedAccount.getAccountId());

        // Then
        assertThat(transactions).hasSize(2);
        assertThat(transactions.get(0).getAccountEntity().getAccountId()).isEqualTo(savedAccount.getAccountId());
        assertThat(transactions.get(1).getAccountEntity().getAccountId()).isEqualTo(savedAccount.getAccountId());
    }

    @Test
    void deleteTransaction_Success() {
        // Given: Save and manage accountEntity first
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        accountRepository.flush();  // Ensure the account is fully persisted

        // Create and save a transaction linked to the saved account
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(BigDecimal.valueOf(500))
                .transactionType(TransactionType.CREDIT)
                .transactionDate(LocalDateTime.now())
                .accountEntity(savedAccount)  // Use the saved and managed account entity
                .build();
        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        // When: Delete the transaction by its ID
        transactionRepository.deleteById(savedTransaction.getTransactionId());

        // Then: Verify that the transaction is deleted from the database
        Optional<TransactionEntity> deletedTransaction = transactionRepository.findById(savedTransaction.getTransactionId());
        assertThat(deletedTransaction).isEmpty();  // Check if the transaction is no longer present
    }

    @Test
    void findById_ReturnsTransaction() {
        // Given: Persist and flush the accountEntity to ensure it's managed
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        accountRepository.flush();

        // Create and save the transaction linked to the saved account
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .amount(BigDecimal.valueOf(300))
                .transactionType(TransactionType.DEBIT)
                .transactionDate(LocalDateTime.now())
                .accountEntity(savedAccount)  // Use the saved and managed account
                .build();
        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        // When: Fetch the transaction by its ID
        Optional<TransactionEntity> fetchedTransaction = transactionRepository.findById(savedTransaction.getTransactionId());

        // Then: Verify the fetched transaction
        assertThat(fetchedTransaction).isPresent();
        assertThat(fetchedTransaction.get().getTransactionType()).isEqualTo(TransactionType.DEBIT);
        assertThat(fetchedTransaction.get().getAmount()).isEqualByComparingTo(BigDecimal.valueOf(300));
    }

}
