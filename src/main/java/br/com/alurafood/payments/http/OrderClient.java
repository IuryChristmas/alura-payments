package br.com.alurafood.payments.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("order-ms")
public interface OrderClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/order/{id}/paid")
    void updatePayment(@PathVariable Long id);
}
