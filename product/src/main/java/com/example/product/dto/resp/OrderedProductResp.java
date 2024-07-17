package com.example.product.dto.resp;

import java.util.List;

public record OrderedProductResp(
        List<ProductResp> products
) {

}
