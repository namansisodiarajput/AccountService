package com.allica.backend.controllers;

import com.allica.backend.controllers.models.CreateAccountRequest;
import com.allica.backend.controllers.models.CreateAccountResponse;
import com.allica.backend.controllers.models.GetAccountResponse;
import com.allica.backend.enums.AccountType;
import com.allica.backend.mappers.AccountMapper;
import com.allica.backend.services.AccountService;
import com.allica.backend.services.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

class AccountControllerApiTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        objectMapper = new ObjectMapper();
        accountId = UUID.randomUUID();
    }

    @Test
    void createAccount_success() throws Exception {
        CreateAccountRequest request = CreateAccountRequest.builder()
                .accountHolderName("John Doe")
                .accountNumber("123456789012")
                .ifscCode("IFSC1234567")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1000))
                .build();

        CreateAccountResponse response = CreateAccountResponse.builder()
                .accountId(accountId)
                .build();

        when(accountService.createAccount(any(Account.class))).thenReturn(accountId);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId", is(accountId.toString())));
    }

    @Test
    void getAccountById_success() throws Exception {
        Account account = Account.builder()
                .accountHolderName("John Doe")
                .accountNumber("123456789012")
                .ifscCode("IFSC1234567")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1000))
                .build();

        when(accountService.getAccountById(accountId)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/accounts/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountHolderName", is("John Doe")))
                .andExpect(jsonPath("$.accountNumber", is("123456789012")))
                .andExpect(jsonPath("$.ifscCode", is("IFSC1234567")))
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.balance", is(1000)));
    }

    @Test
    void getAccountById_notFound() throws Exception {
        when(accountService.getAccountById(accountId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/" + accountId))
                .andExpect(status().isNotFound());
    }
}
