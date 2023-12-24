package neobis.mobimaket.entity.mapper;

import neobis.mobimaket.entity.User;
import neobis.mobimaket.entity.UserInfo;
import neobis.mobimaket.entity.dto.request.UserRequest;
import neobis.mobimaket.entity.dto.response.UserResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserMapper {
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static UserInfo mapUserRequestToUserInfo(UserRequest request) {
        return UserInfo.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .surname(request.getSurname())
                .birthDate(LocalDate.parse(request.getBirthDate(), dateTimeFormatter))
                .build();
    }

    public static UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder()
                .userInfo(user.getUserInfo())
                .email(user.getEmail())
                .profilePhoto(user.getProfilePhoto())
                .phone(user.getPhone())
                .username(user.getUsername())
                .build();
    }
}
