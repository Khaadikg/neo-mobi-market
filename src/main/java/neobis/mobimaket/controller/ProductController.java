package neobis.mobimaket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import neobis.mobimaket.exception.JsonNotValidException;
import neobis.mobimaket.exception.reponse.ExceptionResponse;
import neobis.mobimaket.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("api/product")
public class ProductController {
    ProductService productService;
    ObjectMapper mapper;

    @RequestMapping(method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('USER_ACTIVE')")
    @Operation(summary = "Save product", description = "For only fully filled user accounts",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
            @ApiResponse(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)),
                    responseCode = "404", description = "User not found exception"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "400", description = "Json values string is not valid!")
    })
    public String saveProduct(@Parameter(
                                description = "A profile image to upload", required = true,
                                content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
                              )
            @RequestPart("file") MultipartFile[] multipartFiles,
                              @Parameter(
                                      description = "String of Json values", required = true,
                                      content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
                              )
          @RequestPart("file_json") String request)
    {
        try {
            return productService.addProduct(mapper.readValue(request, ProductRequest.class), multipartFiles);
        } catch (JsonProcessingException e) {
            throw new JsonNotValidException("Json string value is not filled correct!");
        }
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
    @PreAuthorize("hasAuthority('USER_ACTIVE')")
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
    public String updateProduct(@RequestBody ProductRequest request, @RequestParam Long id) {
        return productService.updateProduct(id, request);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('USER_ACTIVE', 'USER')")
    @Operation(summary = "Gets all products", description = "Get all products that has been created, For all users")
    public List<ProductShortResponse> getAllProducts(@Parameter(description = "Number of page", required = true)
                                                         @RequestParam Optional<Integer> pageNumber,
                                                     @Parameter(description = "Amount of items", required = true)
                                                     @RequestParam Optional<Integer> pageSize ) {
        int page = pageNumber.filter(p -> p >= 1).map(p -> p - 1).orElse(0);
        int amount = pageSize.orElse(1);
        return productService.getAllProduct(PageRequest.of(page, amount));

    }

    @DeleteMapping()
    @PreAuthorize("hasAnyAuthority('USER_ACTIVE')")
    @Operation(summary = "Delete product", description = "Update product by id, For only fully filled user accounts",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "User not found exception"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "User not found exception")
            }
    )
    public String deleteProductById(@RequestParam Long id) {
        return productService.deleteProductById(id);
    }
}
