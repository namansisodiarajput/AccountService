package com.allica.backend.controllers;

import com.allica.backend.controllers.models.CreateTransactionRequest;
import com.allica.backend.enums.TransactionType;
import com.allica.backend.services.TransactionService;
import com.allica.backend.services.models.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerApiTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID transactionId;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();
        transactionId = UUID.randomUUID();
        accountId = UUID.randomUUID();
    }

    @Test
    void createTransaction_Success() throws Exception {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(500))
                .transactionType(TransactionType.CREDIT)
                .build();

        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transactionId);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId", is(transactionId.toString())));
    }

    @Test
    void getTransactionsByAccountId_Success() throws Exception {
        Transaction transaction1 = Transaction.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(500))
                .transactionType(TransactionType.CREDIT)
                .build();

        Transaction transaction2 = Transaction.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(200))
                .transactionType(TransactionType.DEBIT)
                .build();

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(transactions);

        mockMvc.perform(get("/api/transactions/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountId", is(accountId.toString())))
                .andExpect(jsonPath("$[0].amount", is(500)))
                .andExpect(jsonPath("$[0].transactionType", is("CREDIT")))
                .andExpect(jsonPath("$[1].amount", is(200)))
                .andExpect(jsonPath("$[1].transactionType", is("DEBIT")));
    }

    @Test
    void getTransactionsByAccountId_EmptyList() throws Exception {
        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
