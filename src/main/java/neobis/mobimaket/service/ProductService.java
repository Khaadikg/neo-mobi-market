package neobis.mobimaket.service;

import neobis.mobimaket.entity.dto.request.ProductRequest;
import neobis.mobimaket.entity.dto.response.ProductResponse;
import neobis.mobimaket.entity.dto.response.ProductShortResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    String addProduct(ProductRequest request);
    String updateProduct(Long id, ProductRequest request);
    List<ProductShortResponse> getAllProduct(Pageable pageable);
    ProductResponse getProductById(Long id);
}
