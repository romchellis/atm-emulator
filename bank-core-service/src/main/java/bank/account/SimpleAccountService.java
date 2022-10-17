package bank.account;

import static bank.jooq.generated.model.tables.Account.ACCOUNT;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import bank.exception.account.InsufficientFundsException;
import bank.client.ClientService;
import bank.jooq.generated.model.tables.records.AccountRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import request.DepositRequest;
import request.WithdrawRequest;
import result.DepositResult;
import result.TotalAmountResult;
import result.WithdrawResult;

@Service
@Transactional
@RequiredArgsConstructor
public class SimpleAccountService implements AccountService {

    private final DSLContext dslContext;
    private final ClientService clientService;

    @Override
    public TotalAmountResult amount() {
        final var clientId = clientService.getCurrentClientId();
        final var amount = dslContext.select(ACCOUNT.AMOUNT)
                .from(ACCOUNT)
                .where(ACCOUNT.CLIENT_ID.eq(clientId))
                .fetchSingleInto(BigDecimal.class);
        return TotalAmountResult.of(amount);
    }

    @Override
    public WithdrawResult withdraw(@NonNull WithdrawRequest request) {
        final var clientId = clientService.getCurrentClientId();
        final var amountToWithdraw = request.getAmount();
        final var account = getCurrentAccount(clientId);
        final var currentAmount = account.getAmount();
        throwIfInsufficient(amountToWithdraw, currentAmount);

        final var updatedAmount = currentAmount.subtract(amountToWithdraw);
        account.setAmount(updatedAmount);
        account.update();
        return WithdrawResult.of(updatedAmount, amountToWithdraw);
    }

    @Override
    public DepositResult deposit(@NonNull DepositRequest request) {
        final var clientId = clientService.getCurrentClientId();
        final var deposit = request.getAmount();
        final var account = getCurrentAccount(clientId);
        final var amount = account.getAmount();
        final var updatedAmount = deposit.add(amount);
        account.setAmount(updatedAmount);
        account.update();
        return DepositResult.of(updatedAmount);
    }

    private void throwIfInsufficient(BigDecimal amountToWithdraw, BigDecimal currentAmount) {
        if (currentAmount.compareTo(amountToWithdraw) <= 0) {
            throw new InsufficientFundsException(currentAmount, amountToWithdraw);
        }
    }

    private AccountRecord getCurrentAccount(Integer clientId) {
        return dslContext.fetchOne(ACCOUNT, ACCOUNT.CLIENT_ID.eq(clientId));
    }

}
