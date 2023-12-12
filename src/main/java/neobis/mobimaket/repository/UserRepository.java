package neobis.mobimaket.repository;

import neobis.mobimaket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query("""
            select u from User u where lower(u.username) like lower(:username)
            or lower(u.email) like lower(:email)""")
    Optional<User> findByUniqConstraint(String username, String email);
}
