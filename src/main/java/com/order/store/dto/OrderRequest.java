package com.order.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderRequest {
    private String customerId;
    private String itemsJson;
}