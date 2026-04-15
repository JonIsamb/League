package com.jisamb.league.api.repository;

import com.jisamb.league.entity.User;
import com.jisamb.league.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Testcontainers
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_Save_ReturnsSavedUser() {
        User user = new User("John Doe", "john.doe@test.test", "1234");

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
        Assertions.assertThat(savedUser.getName()).isEqualTo("John Doe");
        Assertions.assertThat(savedUser.getEmail()).isEqualTo("john.doe@test.test");
        Assertions.assertThat(savedUser.getPassword()).isEqualTo("1234");
        Assertions.assertThat(savedUser.getTimezone()).isEqualTo("Europe/Paris");
        Assertions.assertThat(savedUser.getAreNotificationsAllowed()).isEqualTo(false);
    }

    @Test
    public void UserRepository_FindAll_ReturnsFoundUsers() {
        User user1 = new User("John Doe", "john.doe@test.test", "1234");
        User user2 = new User("Max Doe", "max.doe@test.test", "5678");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> usersFound = userRepository.findAll();

        Assertions.assertThat(usersFound).isNotNull();
        Assertions.assertThat(usersFound.size()).isEqualTo(2);

        Assertions.assertThat(usersFound.getFirst()).isNotNull();
        Assertions.assertThat(usersFound.getFirst().getId()).isGreaterThan(0);
        Assertions.assertThat(usersFound.getFirst().getName()).isEqualTo("John Doe");
        Assertions.assertThat(usersFound.getFirst().getEmail()).isEqualTo("john.doe@test.test");
        Assertions.assertThat(usersFound.getFirst().getPassword()).isEqualTo("1234");
        Assertions.assertThat(usersFound.getFirst().getTimezone()).isEqualTo("Europe/Paris");
        Assertions.assertThat(usersFound.getFirst().getAreNotificationsAllowed()).isEqualTo(false);

        Assertions.assertThat(usersFound.getLast()).isNotNull();
        Assertions.assertThat(usersFound.getLast().getId()).isGreaterThan(0);
        Assertions.assertThat(usersFound.getLast().getName()).isEqualTo("Max Doe");
        Assertions.assertThat(usersFound.getLast().getEmail()).isEqualTo("max.doe@test.test");
        Assertions.assertThat(usersFound.getLast().getPassword()).isEqualTo("5678");
        Assertions.assertThat(usersFound.getLast().getTimezone()).isEqualTo("Europe/Paris");
        Assertions.assertThat(usersFound.getLast().getAreNotificationsAllowed()).isEqualTo(false);
    }

    @Test
    public void UserRepository_FindById_ReturnsUser() {
        User user = new User("John Doe", "john.doe@test.test", "1234");
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getName()).isEqualTo("John Doe");
    }

    @Test
    public void UserRepository_FindById_ReturnsEmptyWhenNotFound() {
        Optional<User> foundUser = userRepository.findById(999);

        Assertions.assertThat(foundUser).isEmpty();
    }

    @Test
    public void UserRepository_Delete_SoftDeletesUser() {
        User user = new User("John Doe", "john.doe@test.test", "1234");
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        Assertions.assertThat(foundUser).isEmpty();
        Assertions.assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    public void UserRepository_Save_ThrowsOnDuplicateEmail() {
        User user1 = new User("John Doe", "same@test.test", "1234");
        User user2 = new User("Max Doe", "same@test.test", "5678");

        userRepository.save(user1);

        Assertions.assertThatThrownBy(() -> userRepository.save(user2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
