package com.allica.backend.controllers.models;

import com.allica.backend.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class GetTransactionResponse {
    private UUID accountId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
}
