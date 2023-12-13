package neobis.mobimaket.service;

import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.request.UserRequest;
import neobis.mobimaket.entity.dto.response.ProductResponse;
import neobis.mobimaket.entity.dto.response.ProductShortResponse;

import java.util.List;

public interface UserService {
    String updateProfile(UserRequest request);
    String updateProfilePhoto(String photo);
    List<ProductShortResponse> getAllMyProducts();
    List<ProductShortResponse> getAllMyLikedProducts();
    ProductResponse getProductById(Long id);
    String likeProduct(Long id);
    String numberConfirm(Integer code, SendCodeRequest request);
    String sendMessage(SendCodeRequest request);
}
