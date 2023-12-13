package neobis.mobimaket.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.Product;
import neobis.mobimaket.entity.User;
import neobis.mobimaket.entity.dto.request.SendCodeRequest;
import neobis.mobimaket.entity.dto.request.UserRequest;
import neobis.mobimaket.entity.dto.response.ProductResponse;
import neobis.mobimaket.entity.dto.response.ProductShortResponse;
import neobis.mobimaket.entity.mapper.ProductMapper;
import neobis.mobimaket.exception.IncorrectCodeException;
import neobis.mobimaket.exception.NotFoundException;
import neobis.mobimaket.exception.TokenExpiredException;
import neobis.mobimaket.repository.ProductRepository;
import neobis.mobimaket.repository.UserRepository;
import neobis.mobimaket.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    ProductRepository productRepository;
    @Override
    public String updateProfile(UserRequest request) {
        return null;
    }

    @Override
    public String updateProfilePhoto(String photo) {
        return null;
    }

    @Override
    public List<ProductShortResponse> getAllMyProducts() {
        return getAuthUser().getMyProducts().stream().map(ProductMapper::mapProductToProductShortResponse).toList();
    }

    @Override
    public List<ProductShortResponse> getAllMyLikedProducts() {
        return getAuthUser().getLikedProducts().stream().map(ProductMapper::mapProductToProductShortResponse).toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return ProductMapper.mapProductToProductResponse(productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product not found by id = " + id)
        ));
    }

    @Override
    public String likeProduct(Long id) {
        User user = getAuthUser();
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product by id = " + id + " not found!")
        );
        if (user.getLikedProducts().contains(product)) {
            product.setLikes(product.getLikes() - 1);
            user.getLikedProducts().remove(product);
            userRepository.save(user);
            return "Remove success";
        }
        product.setLikes(product.getLikes() + 1);
        user.getLikedProducts().add(product);
        userRepository.save(user);
        return "Add success";
    }

    @Override
    public String numberConfirm(Integer code, SendCodeRequest request) {
        User user = getUserByUsername(request.getUsername());
        if (!user.getToken().equals(code)) {
            throw new IncorrectCodeException("Code is not correct");
        }
        user.setToken(0);
        user.setTokenExpiration(null);
        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            userRepository.save(user);
            throw new TokenExpiredException("Your code got expired send new one!");
        }
        user.getUserInfo().setPhone(request.getPhone());
        userRepository.save(user);
        return "Phone number set successfully!";
    }

    @Override
    public String sendMessage(SendCodeRequest request) {
        Random random =  new Random();
        Integer token = random.nextInt(1000, 9999);
        User user = getUserByUsername(request.getUsername());
        user.setToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        return String.valueOf(token);
//        return smsSender.sendSms(request, String.valueOf(token));
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found by username = " + username));
    }

    private User getAuthUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
