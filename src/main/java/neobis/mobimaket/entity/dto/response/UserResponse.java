package neobis.mobimaket.entity.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.Image;
import neobis.mobimaket.entity.UserInfo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String username;
    String email;
    UserInfo userInfo;
    String phone;
    Image profilePhoto;
}
