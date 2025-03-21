package com.allica.backend.entities;

import com.allica.backend.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    @Id
    @GeneratedValue
    private UUID accountId;

    @NotBlank(message = "Account holder name is mandatory")
    private String accountHolderName;

    @NotBlank(message = "Account number is mandatory")
    private String accountNumber;

    @NotBlank(message = "IFSC code is mandatory")
    private String ifscCode;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Account type is mandatory")
    private AccountType accountType;

    @NotNull(message = "Balance is mandatory")
    private BigDecimal balance;

    @OneToMany(mappedBy = "accountEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactionEntities;

    @NotNull
    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @Version  // Add Optimistic Locking Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDateTime.now();
        this.updatedOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedOn = LocalDateTime.now();
    }

    public static class AccountEntityBuilder {
        private Long version = 0L;  // Default version set to 0 when creating an entity
    }
}
