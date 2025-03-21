package com.allica.backend.controllers;

import com.allica.backend.controllers.models.CreateTransactionRequest;
import com.allica.backend.controllers.models.CreateTransactionResponse;
import com.allica.backend.controllers.models.GetTransactionResponse;
import com.allica.backend.mappers.TransactionMapper;
import com.allica.backend.services.TransactionService;
import com.allica.backend.services.models.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller", description = "Endpoints for handling transactions")  // Swagger tag
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create a transaction", description = "Creates a new transaction and returns its transaction ID.")
    public ResponseEntity<CreateTransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        Transaction transaction = TransactionMapper.toTransactionModel(request);
        UUID transactionId = transactionService.createTransaction(transaction);

        CreateTransactionResponse response = CreateTransactionResponse.builder()
                .transactionId(transactionId)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get transactions by account ID", description = "Retrieves a list of transactions linked to a specific account.")
    public ResponseEntity<List<GetTransactionResponse>> getTransactionsByAccountId(@PathVariable UUID accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);

        List<GetTransactionResponse> response = transactions.stream()
                .map(TransactionMapper::toGetTransactionResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
