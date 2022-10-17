package bank.account;

import static bank.jooq.generated.model.tables.Account.ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import bank.client.ClientService;
import bank.exception.account.InsufficientFundsException;
import bank.jooq.generated.model.tables.Account;
import bank.jooq.generated.model.tables.records.AccountRecord;
import request.DepositRequest;
import request.WithdrawRequest;
import result.DepositResult;
import result.TotalAmountResult;
import result.WithdrawResult;

public class SimpleAccountServiceTest {

    private final Integer currentClientID = 1;
    private final BigDecimal amount = BigDecimal.valueOf(1000);

    private AccountService accountService;
    private ClientService clientService;
    private DSLContext ctx;

    @BeforeEach
    void setUp() {
        ctx = mock(DSLContext.class, Mockito.RETURNS_DEEP_STUBS);
        clientService = mock(ClientService.class);
        accountService = new SimpleAccountService(ctx, clientService);
    }

    @Test
    void amount_should_return_current_account_sum() {
        given(clientService.getCurrentClientId()).willReturn(currentClientID);
        given(selectClientAmount()).willReturn(amount);
        var expectedAmount = TotalAmountResult.of(amount);

        var actualAmount = accountService.amount();

        assertThat(actualAmount).isEqualTo(expectedAmount);
    }

    @Test
    void should_throw_insufficient_funds_exception() {
        var account = new AccountRecord(1, amount, currentClientID);
        given(clientService.getCurrentClientId()).willReturn(currentClientID);
        given(selectClientAccount()).willReturn(account);
        var sumToWithdraw = BigDecimal.valueOf(100000000);
        var request = new WithdrawRequest(sumToWithdraw);

        assertThatExceptionOfType(InsufficientFundsException.class)
                .isThrownBy(() -> accountService.withdraw(request));
    }

    @Test
    void should_withdraw__from_current_amount() {
        var account = spy(new AccountRecord(1, amount, currentClientID));
        given(clientService.getCurrentClientId()).willReturn(currentClientID);
        given(selectClientAccount()).willReturn(account);
        doReturn(1).when(account).update();
        var sumToWithdraw = BigDecimal.valueOf(500);
        var request = new WithdrawRequest(sumToWithdraw);
        var updatedAmount = amount.subtract(sumToWithdraw);
        var expected = WithdrawResult.of(updatedAmount, sumToWithdraw);

        var actual = accountService.withdraw(request);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_add_to_current_amount_on_deposit() {
        var sumToDeposit = BigDecimal.valueOf(1000);
        var account = spy(new AccountRecord(1, amount, currentClientID));
        var request = new DepositRequest(sumToDeposit);
        var expectedAmount = sumToDeposit.add(amount);
        var expectedResult = DepositResult.of(expectedAmount);
        given(clientService.getCurrentClientId()).willReturn(currentClientID);
        given(selectClientAccount()).willReturn(account);
        doReturn(1).when(account).update();

        DepositResult depositResult = accountService.deposit(request);

        assertThat(depositResult).isEqualTo(expectedResult);
    }

    private Record selectClientAccount() {
        return ctx.fetchOne(ACCOUNT, ACCOUNT.CLIENT_ID.eq(currentClientID));
    }

    private BigDecimal selectClientAmount() {
        return ctx.select(Account.ACCOUNT.AMOUNT)
                .from(Account.ACCOUNT)
                .where(Account.ACCOUNT.CLIENT_ID.eq(currentClientID))
                .fetchSingleInto(BigDecimal.class);
    }
}