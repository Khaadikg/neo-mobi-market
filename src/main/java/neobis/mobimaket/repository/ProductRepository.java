package neobis.mobimaket.repository;

import neobis.mobimaket.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select * from products p join user_liked_products ulp on p.id = ulp.product_id and p.id = ?1 and ulp.user_id = ?2", nativeQuery = true)
    Product findByIdAndIdOfLikedUser(Long productId, Long userId);
    @Query(value = "select p from Product p where p.id = :productId and p.owner.id = :ownerId")
    Optional<Product> findByIdAndIdOfUser(@Param("productId") Long productId, @Param("ownerId") Long userId);
    @Query("select p from Product p order by p.id desc limit 1")
    Optional<Product> findLastAddedProduct();
}
