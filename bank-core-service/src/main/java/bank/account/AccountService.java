package bank.account;

import request.DepositRequest;
import request.WithdrawRequest;
import result.DepositResult;
import result.TotalAmountResult;
import result.WithdrawResult;

/**
 * Service for basic account operations through the card session
 */
public interface AccountService {

    TotalAmountResult amount();

    WithdrawResult withdraw(WithdrawRequest amount);

    DepositResult deposit(DepositRequest amount);

}
