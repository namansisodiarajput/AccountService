package com.allica.backend.controllers.models;

import com.allica.backend.enums.AccountType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateAccountRequest {

    @NotBlank(message = "Account holder name is mandatory")
    @Size(min = 3, max = 50, message = "Account holder name must be between 3 and 50 characters")
    private String accountHolderName;

    @NotBlank(message = "Account number is mandatory")
    @Pattern(regexp = "\\d{12}", message = "Account number must be exactly 12 numeric characters")
    private String accountNumber;

    @NotBlank(message = "IFSC code is mandatory")
    @Pattern(regexp = "[a-zA-Z0-9]{11}", message = "IFSC code must be exactly 11 alphanumeric characters")
    private String ifscCode;

    @NotNull(message = "Account type is mandatory")
    private AccountType accountType;

    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be zero or a positive value")
    private BigDecimal balance;
}
