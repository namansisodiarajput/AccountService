package com.allica.backend.controllers.models;

import com.allica.backend.enums.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GetAccountResponse {

    private String accountHolderName;

    private String accountNumber;

    private String ifscCode;

    private AccountType accountType;

    private BigDecimal balance;
}
