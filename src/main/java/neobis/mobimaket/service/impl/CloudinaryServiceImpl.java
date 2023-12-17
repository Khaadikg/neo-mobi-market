package neobis.mobimaket.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import neobis.mobimaket.service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryServiceImpl implements CloudinaryService {
    Cloudinary cloudinary;

    public Map upload(MultipartFile multipartFile, String folder) {
        File file = convert(multipartFile);
        Map result = null;
        try {
            result = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", folder));
            if (!Files.deleteIfExists(file.toPath())) {
                throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath()); // add handler
            }
        } catch ( IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map delete(String publicId) {
        Map result = null;
        try {
            result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private File convert(MultipartFile multipartFile) {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(multipartFile.getBytes());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
