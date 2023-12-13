package neobis.mobimaket.entity.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;
    String shortDescription;
    String fullDescription;
    Double price;
}
