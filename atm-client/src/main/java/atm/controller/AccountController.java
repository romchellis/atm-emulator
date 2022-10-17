package atm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.AccountApi;
import lombok.RequiredArgsConstructor;
import request.DepositRequest;
import request.WithdrawRequest;
import result.DepositResult;
import result.TotalAmountResult;
import result.WithdrawResult;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountApi accountApi;

    @GetMapping("/amount")
    public TotalAmountResult amount() {
        return accountApi.amount();
    }

    @PostMapping("/withdraw")
    public WithdrawResult withdraw(@RequestBody WithdrawRequest request) {
        return accountApi.withdraw(request);
    }

    @PostMapping("/deposit")
    public DepositResult deposit(@RequestBody DepositRequest request) {
        return accountApi.deposit(request);
    }

}
