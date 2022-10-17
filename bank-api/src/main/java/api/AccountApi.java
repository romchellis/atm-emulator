package api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import config.FeignConfig;
import request.DepositRequest;
import request.WithdrawRequest;
import result.DepositResult;
import result.TotalAmountResult;
import result.WithdrawResult;

@FeignClient(name = "accountClient",
        url = "http://" + "${bank.api.host}" + ":${bank.api.port}",
        path = "/account",
        configuration = FeignConfig.class)
public interface AccountApi {

    @GetMapping("/amount")
    TotalAmountResult amount();

    @PostMapping("/withdraw")
    WithdrawResult withdraw(@RequestBody WithdrawRequest request);

    @PostMapping("/deposit")
    DepositResult deposit(@RequestBody DepositRequest request);

}
