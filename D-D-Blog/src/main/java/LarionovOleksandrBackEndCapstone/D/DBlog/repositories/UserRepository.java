package LarionovOleksandrBackEndCapstone.D.DBlog.repositories;

import LarionovOleksandrBackEndCapstone.D.DBlog.ENUMS.ROLE;
import LarionovOleksandrBackEndCapstone.D.DBlog.entities.User;
import LarionovOleksandrBackEndCapstone.D.DBlog.payloads.user.UserEmailDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.enabled = CASE WHEN u.enabled = TRUE THEN FALSE ELSE TRUE END WHERE u.email = ?1")
    int enableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.locked = CASE WHEN u.locked = TRUE THEN FALSE ELSE TRUE END WHERE u.email = ?1")
    int lockedUser(String email);

}
