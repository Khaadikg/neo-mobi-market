package neobis.mobimaket.service;

import neobis.mobimaket.entity.dto.request.ProductRequest;
import neobis.mobimaket.entity.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    String addProduct(ProductRequest request);
    String updateProduct(Long id, ProductRequest request);
    String deleteProduct(Long id);
    List<ProductResponse> getAllProduct();
    ProductResponse getProductById(Long id);
}
