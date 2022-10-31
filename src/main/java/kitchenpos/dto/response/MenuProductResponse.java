package kitchenpos.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private ProductResponse product;
    private long quantity;

    public MenuProductResponse(MenuProduct menuProduct) {
        seq = menuProduct.getSeq();
        product = new ProductResponse(menuProduct.getProduct());
        quantity = menuProduct.getQuantity();
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

}