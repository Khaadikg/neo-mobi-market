package neobis.mobimaket.service;

import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.request.UserRequest;
import neobis.mobimaket.entity.dto.response.ProductResponse;

import java.util.List;

public interface UserService {
    String updateProfile(UserRequest request);
    String updateProfilePhoto(String photo);
    List<ProductResponse> getAllMyProducts();
    List<ProductResponse> getAllMyLikedProducts();
    String likeProduct(Long id);
    String numberConfirm(Integer code, SendCodeRequest request);
    String sendMessage(SendCodeRequest request);
}
