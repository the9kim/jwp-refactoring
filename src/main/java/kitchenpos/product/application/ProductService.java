package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final Product savedProduct = productDao.save(request.toEntity());

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> list() {
        return productDao.findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}