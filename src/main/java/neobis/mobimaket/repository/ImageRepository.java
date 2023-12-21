package neobis.mobimaket.repository;

import neobis.mobimaket.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteAllByProductId(Long id);
}
