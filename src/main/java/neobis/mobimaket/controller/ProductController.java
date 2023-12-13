package neobis.mobimaket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.dto.request.ProductRequest;
import neobis.mobimaket.entity.dto.response.LoginResponse;
import neobis.mobimaket.entity.dto.response.ProductResponse;
import neobis.mobimaket.entity.dto.response.ProductShortResponse;
import neobis.mobimaket.exception.reponse.ExceptionResponse;
import neobis.mobimaket.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("api/product")
public class ProductController {
    ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER_ACTIVE')")
    @Operation(summary = "Save product", description = "For only fully filled user accounts")
    public String saveProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request);
    }

    @GetMapping
    @Operation(summary = "Get product by id", description = "Getting product for all users",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class)),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "User not found exception")
            }
    )
    public ProductResponse getProductById(@RequestParam Long id) {
        return productService.getProductById(id);
    }

    @PutMapping
    @Operation(summary = "Update product", description = "Update product by id, For only fully filled user accounts",
            responses = {
            @ApiResponse(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)),
                    responseCode = "200", description = "Good"),
            @ApiResponse(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)),
                    responseCode = "404", description = "User not found exception")
    }
    )
    public String updateProduct(Long id, ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @GetMapping("/all")
    @Operation(summary = "Gets all products", description = "Get all products that has been created, For only fully filled user accounts")
    public List<ProductShortResponse> getAllProducts(@Parameter(description = "Number of page", required = true)
                                                         @RequestParam Optional<Integer> pageNumber,
                                                     @Parameter(description = "Amount of items", required = true)
                                                     @RequestParam Optional<Integer> pageSize ) {
        int page = pageNumber.filter(p -> p >= 1).map(p -> p - 1).orElse(0);
        int amount = pageSize.orElse(0);
        return productService.getAllProduct(PageRequest.of(page, amount));

    }
}
