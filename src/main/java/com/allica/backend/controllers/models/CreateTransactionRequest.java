package com.allica.backend.controllers.models;

import com.allica.backend.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CreateTransactionRequest {

    @NotNull(message = "Account ID is mandatory")
    private UUID accountId;

    @NotNull(message = "Amount is mandatory")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is mandatory")
    private TransactionType transactionType;
}
