package neobis.mobimaket.service;

import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.request.UserRequest;
import neobis.mobimaket.entity.dto.response.*;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserResponse updateProfile(UserRequest request);
    ImageResponse updateProfilePhoto(Map result);
    List<ProductShortResponse> getAllPersonalProducts();
    List<ProductShortResponse> getAllLikedProducts();
    LikeResponse likeProduct(Long id);
    SingleStringResponse numberConfirm(Integer code, SendCodeRequest request);
    String sendMessage(SendCodeRequest request);
    UserResponse getUserProfile();
}
