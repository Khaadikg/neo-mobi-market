package neobis.mobimaket.entity.mapper;

import neobis.mobimaket.entity.Image;
import neobis.mobimaket.entity.Product;
import neobis.mobimaket.entity.dto.request.ProductRequest;
import neobis.mobimaket.entity.dto.response.ProductResponse;
import neobis.mobimaket.entity.dto.response.ProductShortResponse;

public class ProductMapper {
    public static ProductShortResponse mapProductToProductShortResponse(Product product) {
        Image image = !product.getPhoto().isEmpty()? product.getPhoto().get(0) : null;
        return ProductShortResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .likes(product.getLikes())
                .price(product.getPrice())
                .image(image)
                .build();
    }

    public static ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .shortDescription(product.getShortDescription())
                .fullDescription(product.getFullDescription())
                .likes(product.getLikes())
                .price(product.getPrice())
                .build();
    }

    public static Product mapProductRequestToProduct(ProductRequest request) {
        return Product.builder()
                .fullDescription(request.getFullDescription())
                .shortDescription(request.getShortDescription())
                .price(request.getPrice())
                .name(request.getName())
                .build();
    }

}
