package bank.controller;

import org.springframework.web.bind.annotation.RestController;

import api.AccountApi;
import bank.account.AccountService;
import lombok.RequiredArgsConstructor;
import request.DepositRequest;
import request.WithdrawRequest;
import result.DepositResult;
import result.TotalAmountResult;
import result.WithdrawResult;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @Override
    public TotalAmountResult amount() {
        return accountService.amount();
    }

    @Override
    public WithdrawResult withdraw(WithdrawRequest request) {
        return accountService.withdraw(request);
    }

    @Override
    public DepositResult deposit(DepositRequest request) {
        return accountService.deposit(request);
    }


}
