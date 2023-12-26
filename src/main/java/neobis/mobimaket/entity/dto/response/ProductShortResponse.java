package neobis.mobimaket.entity.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.Image;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductShortResponse {
    Long id;
    String name;
    Double price;
    Integer likes;
    Image image;
}
