package neobis.mobimaket.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.entity.Image;
import neobis.mobimaket.entity.dto.response.ImageResponse;
import neobis.mobimaket.entity.mapper.ImageMapper;
import neobis.mobimaket.repository.ImageRepository;
import neobis.mobimaket.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceImpl implements ImageService {
    ImageRepository imageRepository;
    public ImageResponse saveImage(Image image) {
        return ImageMapper.mapImageTOImageResponse(imageRepository.save(image));
    }

    public List<ImageResponse> saveAllImages(List<Image> images) {
        return imageRepository.saveAll(images).stream().map(ImageMapper::mapImageTOImageResponse).toList();
    }
}
