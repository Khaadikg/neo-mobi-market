package neobis.mobimaket.repository;

import neobis.mobimaket.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductPagingRepository extends PagingAndSortingRepository<Product, Long> {
}
