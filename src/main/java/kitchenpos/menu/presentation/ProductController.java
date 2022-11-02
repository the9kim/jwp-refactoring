package kitchenpos.menu.presentation;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.presentation.dto.request.ProductRequest;
import kitchenpos.menu.presentation.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {

        final Product created = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + created.getId());
        final ProductResponse productResponse = ProductResponse.from(created);

        return ResponseEntity.created(uri)
                .body(productResponse);
    }

    @GetMapping("/api/products")
    @ResponseStatus(OK)
    public List<ProductResponse> list() {

        final List<Product> products = productService.list();

        return products.stream()
                .map(ProductResponse::from)
                .collect(toList());
    }
}