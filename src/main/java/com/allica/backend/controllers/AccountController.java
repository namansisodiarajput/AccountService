package com.allica.backend.controllers;

import com.allica.backend.controllers.models.CreateAccountRequest;
import com.allica.backend.controllers.models.CreateAccountResponse;
import com.allica.backend.controllers.models.GetAccountResponse;
import com.allica.backend.mappers.AccountMapper;
import com.allica.backend.services.AccountService;
import com.allica.backend.services.models.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Controller", description = "Endpoints for managing bank accounts")  // Swagger tag
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @Operation(summary = "Create a new account", description = "Creates a new bank account with the provided details.")
    public ResponseEntity<CreateAccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account account = AccountMapper.toAccountModel(request);
        UUID accountId = accountService.createAccount(account);

        CreateAccountResponse response = CreateAccountResponse.builder()
                .accountId(accountId)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by ID", description = "Fetches details of an account using the account ID.")
    public ResponseEntity<GetAccountResponse> getAccountById(@PathVariable UUID accountId) {
        Optional<Account> accountOpt = accountService.getAccountById(accountId);

        return accountOpt
                .map(account -> ResponseEntity.ok(AccountMapper.toGetAccountResponse(account)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
