package neobis.mobimaket.service;

import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.request.UserRequest;
import neobis.mobimaket.entity.dto.response.ProductShortResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    String updateProfile(UserRequest request);
    String updateProfilePhoto(Map result);
    List<ProductShortResponse> getAllPersonalProducts();
    List<ProductShortResponse> getAllLikedProducts();
    String likeProduct(Long id);
    String numberConfirm(Integer code, SendCodeRequest request);
    String sendMessage(SendCodeRequest request);
}
