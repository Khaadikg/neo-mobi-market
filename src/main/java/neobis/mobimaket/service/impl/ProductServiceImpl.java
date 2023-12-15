package neobis.mobimaket.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.Product;
import neobis.mobimaket.entity.User;
import neobis.mobimaket.entity.dto.request.ProductRequest;
import neobis.mobimaket.entity.dto.response.ProductResponse;
import neobis.mobimaket.entity.dto.response.ProductShortResponse;
import neobis.mobimaket.entity.mapper.ProductMapper;
import neobis.mobimaket.exception.NotFoundException;
import neobis.mobimaket.repository.ProductPagingRepository;
import neobis.mobimaket.repository.ProductRepository;
import neobis.mobimaket.repository.UserRepository;
import neobis.mobimaket.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProductPagingRepository pagingRepository;
    UserRepository userRepository;
    @Override
    public String addProduct(ProductRequest request) {
        Product product = ProductMapper.mapProductRequestToProduct(request);
        product.setOwner(getAuthUser());
        productRepository.save(product);
        return "success";
    }

    @Override
    public String updateProduct(Long id, ProductRequest request) {
        Product product = getById(id);
        product.setName(request.getName());
        product.setShortDescription(request.getShortDescription());
        product.setFullDescription(request.getFullDescription());
        product.setPrice(request.getPrice());
        return "Updated successfully";
    }

    @Override
    public List<ProductShortResponse> getAllProduct(Pageable pageable) { // here need to be pagination
        return pagingRepository.findAll(pageable).stream().map(ProductMapper::mapProductToProductShortResponse).toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return ProductMapper.mapProductToProductResponse(getById(id));
    }

    @Override
    public String deleteProductById(Long id) {
        Product product = productRepository.findByIdAndIdOfUser(id, getAuthUser().getId()).orElseThrow(
                () -> new NotFoundException("Product not found by id = " + id)
        );
        productRepository.delete(product);
        return "Product deleted!";
    }

    private Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product not found by id = " + id)
        );
    }
    private User getAuthUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
    }
}
