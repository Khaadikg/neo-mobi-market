package neobis.mobimaket.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Embeddable
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfo {
    String name;
    String surname;
    String lastname;
    LocalDate birthDate;
}
