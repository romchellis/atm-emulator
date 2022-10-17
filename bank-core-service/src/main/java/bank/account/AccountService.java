package bank.account;

import request.DepositRequest;
import request.WithdrawRequest;
import result.DepositResult;
import result.TotalAmountResult;
import result.WithdrawResult;

public interface AccountService {

    TotalAmountResult amount();

    WithdrawResult withdraw(WithdrawRequest amount);

    DepositResult deposit(DepositRequest amount);

}
