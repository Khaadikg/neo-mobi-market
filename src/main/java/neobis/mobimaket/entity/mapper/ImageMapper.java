package neobis.mobimaket.entity.mapper;

import neobis.mobimaket.entity.Image;
import neobis.mobimaket.entity.dto.response.ImageResponse;

public class ImageMapper {
    public static ImageResponse mapImageTOImageResponse(Image image) {
        return ImageResponse.builder()
                .imageId(image.getImageId())
                .name(image.getName())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
