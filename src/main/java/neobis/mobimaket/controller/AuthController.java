package neobis.mobimaket.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import neobis.mobimaket.entity.dto.request.LoginRequest;
import neobis.mobimaket.entity.dto.request.RegistrationRequest;
import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.response.LoginResponse;
import neobis.mobimaket.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = {})
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public String registration(@RequestBody @Valid RegistrationRequest request) {
        return authService.registration(request);
    }

    @PutMapping("/registration-confirm")
    public String ensureRegistration(@RequestParam(name = "code") @Positive Integer token,
                                     @RequestParam(name = "username") @NotBlank String username) {
        return authService.registrationConfirm(token, username);
    }

    @PutMapping("/send-code")
    public String resendMessage(@RequestBody SendCodeRequest request){
        return authService.sendMessage(request);
    }

    @PostMapping("/sign-in")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/refresh-token")
    public String getRefreshToken(@RequestParam(name = "token") @NotBlank String token,
                                  @RequestParam(name = "username") @NotBlank String username) {
        return  authService.refreshToken(token, username);
    }
}

