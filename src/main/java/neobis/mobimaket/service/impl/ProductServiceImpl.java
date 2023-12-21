package neobis.mobimaket.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import neobis.mobimaket.entity.Image;
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
import neobis.mobimaket.service.CloudinaryService;
import neobis.mobimaket.service.ImageService;
import neobis.mobimaket.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProductPagingRepository pagingRepository;
    UserRepository userRepository;
    ImageService imageService;
    CloudinaryService cloudinaryService;

    @Override
    public String addProduct(ProductRequest request, MultipartFile[] multipartFiles) {
        log.info("Starting to save images in Database!");
        List<Map> results = new ArrayList<>();
        Arrays.stream(multipartFiles).forEach(file -> results.add(cloudinaryService.upload(file,  "product")));
        log.info("Images saved into cloudinary!");
        log.info("Starting to save images in Database!");
        Product product = ProductMapper.mapProductRequestToProduct(request);
        List<Image> images = results.stream().map(x -> Image.builder()
                .name((String) x.get("original_filename"))
                .imageUrl((String) x.get("url"))
                .imageId(String.valueOf(x.get("public_id")))
                .product(product).build()).toList();
        product.setPhoto(images);
        product.setOwner(getAuthUser());
        productRepository.save(product);
        imageService.saveAllImages(images);
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
    @Transactional
    public String deleteProductById(Long id) {
        Product product = productRepository.findByIdAndIdOfUser(id, getAuthUser().getId()).orElseThrow(
                () -> new NotFoundException("Product not found by id = " + id)
        );
        imageService.deleteAllImagesByProductId(product.getId());
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
