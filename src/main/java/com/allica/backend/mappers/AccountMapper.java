package com.allica.backend.mappers;

import com.allica.backend.controllers.models.CreateAccountRequest;
import com.allica.backend.controllers.models.GetAccountResponse;
import com.allica.backend.entities.AccountEntity;
import com.allica.backend.services.models.Account;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {

    public Account toDto(AccountEntity entity) {
        return Account.builder()
                .accountHolderName(entity.getAccountHolderName())
                .accountNumber(entity.getAccountNumber())
                .ifscCode(entity.getIfscCode())
                .accountType(entity.getAccountType())
                .balance(entity.getBalance())
                .build();
    }

    public Account toAccountModel(CreateAccountRequest request) {
        return Account.builder()
                .accountHolderName(request.getAccountHolderName())
                .accountNumber(request.getAccountNumber())
                .ifscCode(request.getIfscCode())
                .accountType(request.getAccountType())
                .balance(request.getBalance())
                .build();
    }

    public GetAccountResponse toGetAccountResponse(Account account) {
        return GetAccountResponse.builder()
                .accountHolderName(account.getAccountHolderName())
                .accountNumber(account.getAccountNumber())
                .ifscCode(account.getIfscCode())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }
}
