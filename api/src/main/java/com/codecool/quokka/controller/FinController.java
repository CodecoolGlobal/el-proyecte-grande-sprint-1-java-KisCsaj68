package com.codecool.quokka.controller;

import com.codecool.quokka.model.OrderDto;
import com.codecool.quokka.model.assets.AssetType;
import com.codecool.quokka.model.order.Orders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@ConfigurationProperties
public class FinController {
    public static final UUID ACCOUNT_ID = UUID.fromString("a1521309-f533-460a-a9fc-3028b0efc79b");
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${quokka.service.oms.address}${quokka.service.oms.endpoint}")
    private String url;

    @PostMapping(path = "stock")
    public ResponseEntity createNewStockOrder(@RequestBody OrderDto data) {
        Orders actualOrders = data.toEntity(ACCOUNT_ID);
        actualOrders.setAssetType(AssetType.STOCK);

        //post request to OMS / marketOrder
        HttpEntity<Orders> request = new HttpEntity<>(actualOrders);
        ResponseEntity orderCreateResponse = restTemplate.postForObject(url, request, ResponseEntity.class);
        return orderCreateResponse;
    }

    @PostMapping(path = "crypto")
    public ResponseEntity createNewCryptoOrder(@RequestBody OrderDto data) {
        Orders actualOrders = data.toEntity(ACCOUNT_ID);
        actualOrders.setAssetType(AssetType.CRYPTO);

        //post request to OMS / marketOrder
        HttpEntity<Orders> request = new HttpEntity<>(actualOrders);
        ResponseEntity orderCreateResponse = restTemplate.postForObject(url, request, ResponseEntity.class);
        return orderCreateResponse;
    }
}
