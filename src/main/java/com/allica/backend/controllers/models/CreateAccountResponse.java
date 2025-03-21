package com.allica.backend.controllers.models;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateAccountResponse {

    private UUID accountId;
}
