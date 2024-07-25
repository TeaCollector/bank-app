package ru.alex.msapigateway.service;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.alex.msapigateway.config.OpenFeignConfiguration;
import ru.alex.msapigateway.dto.*;


@FeignClient(value = "apiGatewayFeignClient", url = OpenFeignConfiguration.DEAL_FEIGN_URL)
public interface DealFeignService {

    @RequestMapping(method = RequestMethod.POST, value = "calculate/{statementId}", consumes = "application/json")
    void calculate(@RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto, @PathVariable String statementId);

    @RequestMapping(method = RequestMethod.POST, value = "document/{statementId}/send")
    void sendDocument(@PathVariable String statementId);

    @RequestMapping(method = RequestMethod.POST, value = "document/{statementId}/sign")
    void signDocument(@PathVariable String statementId);

    @RequestMapping(method = RequestMethod.POST, value = "document/{statementId}/code")
    void codeSign(@PathVariable String statementId, @RequestBody SesCodeDto sesCode);

    @RequestMapping(method = RequestMethod.GET, value = "admin/statement/{statementId}", consumes = "application/json")
    ResponseEntity<StatementDto> getStatement(@PathVariable String statementId);

    @RequestMapping(method = RequestMethod.GET, value = "admin/statements")
    ResponseEntity<List<StatementDto>> getStatements();

}

