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
import neobis.mobimaket.exception.reponse.ExceptionResponse;
import neobis.mobimaket.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PutMapping("/phone-confirm")
    @Operation(summary = "Phone confirmation", description = "Add number for user account by 4 digit code sent by SMS, if token EXPIRED deletes old token",
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
    public String registrationConfirm(@Parameter(description = "Code for user ensure number sent by SMS", required = true)
                                      @RequestParam(name = "code") @Positive Integer code,
                                      @RequestBody SendCodeRequest request) {
        return userService.numberConfirm(code, request);
    }

    @PutMapping("/send-code")
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

}
