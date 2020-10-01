package com.example.minesweep.function;

import com.example.minesweep.domain.AccountEntity;
import com.example.minesweep.rest.response.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountConverter {

    public Account toAccount(AccountEntity accountEntity) {
        return Account.builder().id(accountEntity.getId())
                .email(accountEntity.getEmail())
                .name(accountEntity.getName())
                .build();
    }
}
