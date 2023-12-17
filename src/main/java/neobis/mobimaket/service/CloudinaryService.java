package neobis.mobimaket.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map delete(String publicId);
    Map upload(MultipartFile multipartFile, String folder);
}
