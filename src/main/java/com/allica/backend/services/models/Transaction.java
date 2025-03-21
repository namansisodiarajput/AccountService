package com.allica.backend.services.models;

import com.allica.backend.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Transaction {

    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is mandatory")
    private TransactionType transactionType;

    @NotNull(message = "Account ID is mandatory")
    private UUID accountId;

    private LocalDateTime transactionDate;
}
