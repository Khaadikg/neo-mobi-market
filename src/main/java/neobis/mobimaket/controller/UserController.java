package neobis.mobimaket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.request.UserRequest;
import neobis.mobimaket.entity.dto.response.ImageResponse;
import neobis.mobimaket.entity.dto.response.LikeResponse;
import neobis.mobimaket.entity.dto.response.ProductShortResponse;
import neobis.mobimaket.entity.dto.response.UserResponse;
import neobis.mobimaket.exception.reponse.ExceptionResponse;
import neobis.mobimaket.service.CloudinaryService;
import neobis.mobimaket.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("api/user")
public class UserController {
    UserService userService;
    CloudinaryService cloudinaryService;

    @PutMapping("/phone-confirm")
    @PreAuthorize("hasAnyAuthority('USER', 'USER_ACTIVE')")
    @Operation(summary = "Phone confirmation", description = "Add number for SAVED user account by 4 digit code sent by SMS, if token EXPIRED deletes old token",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "400", description = "Token expired exception"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "406", description = "Code is not correct")
            }
    )
    public String phoneConfirm(@Parameter(description = "Code for user ensure number sent by SMS", required = true)
                                      @RequestParam(name = "code") @Positive Integer code,
                               @RequestBody SendCodeRequest request) {
        return userService.numberConfirm(code, request);
    }

    @PutMapping("/send-code")
    @PreAuthorize("hasAnyAuthority('USER', 'USER_ACTIVE')")
    @Operation(summary = "Registration send code", description = "Sends new code, user must be already saved via sign-up",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "406", description = "Validation exception"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "User not found with such username exception")
            }
    )
    public String resendMessage(@RequestBody SendCodeRequest request){
        return userService.sendMessage(request);
    }

    @GetMapping("/get-personal-products")
    @PreAuthorize("hasAuthority('USER_ACTIVE')")
    @Operation(summary = "Getting all personal products", description = "For only fully filled user accounts")
    public List<ProductShortResponse> getAllPersonalProducts() {
        return userService.getAllPersonalProducts();
    }

    @GetMapping("/get-liked-products")
    @PreAuthorize("hasAuthority('USER_ACTIVE')")
    @Operation(summary = "Getting all liked products", description = "For only fully filled user accounts")
    public List<ProductShortResponse> getAllLikedProducts() {
        return userService.getAllLikedProducts();
    }

    @PutMapping("/like")
    @PreAuthorize("hasAuthority('USER_ACTIVE')")
    @Operation(summary = "Like or Dislike", description = "Make like and dislike via product id",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LikeResponse.class)),
                            responseCode = "200", description = "GOOD"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "Product not found with such id exception")
            }
    )
    public LikeResponse likeProduct(@RequestParam @Positive Long id) {
        return userService.likeProduct(id);
    }

    @PutMapping()
    @PreAuthorize("hasAnyAuthority('USER', 'USER_ACTIVE')")
    @Operation(summary = "Update profile", description = "Updates user profile, user must be already saved via sign-up",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LikeResponse.class)),
                            responseCode = "200", description = "GOOD"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "406", description = "Validation exception"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "User not found with such username exception")
            }
    )
    public UserResponse updateProfile(@RequestBody UserRequest request) {
        return userService.updateProfile(request);
    }

    @PostMapping(path = "/my-photo", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('USER', 'USER_ACTIVE')")
    @Operation(summary = "Update profile", description = "Updates user profile, user must be already saved via sign-up",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "User not found with such username exception")
            }
    )
    public ImageResponse updateProfileImage(@Parameter(
                                                    description = "A profile image to upload", required = true,
                                                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
                                            )
                                        @RequestParam("file") MultipartFile multipartFile) {
        Map result = cloudinaryService.upload(multipartFile, "Haadi");
        return userService.updateProfilePhoto(result);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'USER_ACTIVE')")
    @Operation(summary = "Update profile", description = "Updates user profile, user must be already saved via sign-up",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "User not found with such username exception")
            }
    )
    public UserResponse getProfile() {
        return userService.getUserProfile();
    }
}
