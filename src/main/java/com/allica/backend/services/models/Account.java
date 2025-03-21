package com.allica.backend.services.models;

import com.allica.backend.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Account {

    @NotBlank(message = "Account holder name is mandatory")
    private String accountHolderName;

    @NotBlank(message = "Account number is mandatory")
    private String accountNumber;

    @NotBlank(message = "IFSC code is mandatory")
    private String ifscCode;

    @NotNull(message = "Account type is mandatory")
    private AccountType accountType;

    @NotNull(message = "Balance cannot be null")
    private BigDecimal balance;
}
