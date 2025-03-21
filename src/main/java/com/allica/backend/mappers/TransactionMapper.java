package com.allica.backend.mappers;

import com.allica.backend.controllers.models.CreateTransactionRequest;
import com.allica.backend.controllers.models.GetTransactionResponse;
import com.allica.backend.entities.TransactionEntity;
import com.allica.backend.services.models.Transaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionMapper {

    public Transaction toDto(TransactionEntity entity) {
        return Transaction.builder()
                .amount(entity.getAmount())
                .transactionType(entity.getTransactionType())
                .accountId(entity.getAccountEntity().getAccountId())
                .transactionDate(entity.getTransactionDate())
                .build();
    }

    public Transaction toTransactionModel(CreateTransactionRequest request) {
        return Transaction.builder()
                .amount(request.getAmount())
                .transactionType(request.getTransactionType())
                .accountId(request.getAccountId())
                .build();
    }

    public GetTransactionResponse toGetTransactionResponse(Transaction transaction) {
        return GetTransactionResponse.builder()
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .transactionDate(transaction.getTransactionDate())
                .accountId(transaction.getAccountId())
                .build();
    }
}
