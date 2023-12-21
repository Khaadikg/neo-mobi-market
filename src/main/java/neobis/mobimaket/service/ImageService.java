package neobis.mobimaket.service;

import neobis.mobimaket.entity.Image;
import neobis.mobimaket.entity.dto.response.ImageResponse;

import java.util.List;

public interface ImageService {
    ImageResponse saveImage(Image image);

    void saveAllImages(List<Image> images);
    void deleteAllImagesByProductId(Long id);
}
