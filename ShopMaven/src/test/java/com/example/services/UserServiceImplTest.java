package com.example.services;

import com.example.models.User;
import com.example.repositories.UserRepositoryJdbcTemplateImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static com.github.npathai.hamcrestopt.OptionalMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("UserService Integration Tests")
class UserServiceImplTest {

 private EmbeddedDatabase embeddedDatabase;
 private UserService userService;

 @BeforeEach
 void setUp() {
  embeddedDatabase = new EmbeddedDatabaseBuilder()
          .setType(EmbeddedDatabaseType.H2)
          .addScripts("sql/schema.sql", "sql/data.sql")
          .build();

  UserRepositoryJdbcTemplateImpl userRepository =
          new UserRepositoryJdbcTemplateImpl(embeddedDatabase);

  userService = new UserServiceImpl(userRepository);
 }

 @AfterEach
 void tearDown() {
  embeddedDatabase.shutdown();
 }

 @Nested
 @DisplayName("Create Operations")
 class CreateOperations {

  @Test
  void should_save_user_and_populate_id() {
   User newUser = User.builder()
           .name("Leonid")
           .surname("Oganesyan")
           .phone("89001112233")
           .email("leo@example.com")
           .age(25)
           .build();

   userService.create(newUser);

   assertThat(newUser.getId(), allOf(notNullValue(), greaterThan(0L)));
  }
 }

 @Nested
 @DisplayName("Read Operations")
 class ReadOperations {

  @ParameterizedTest(name = "should find user with id={0}")
  @MethodSource("com.example.services.UserServiceImplTest#existingUsersProvider")
  void should_get_existing_users_by_id(User expectedUser) {
   Optional<User> userOpt = userService.getById(expectedUser.getId());

   assertThat(userOpt, isPresentAndIs(expectedUser));
   assertThat(userOpt.get(), hasProperty("email", equalTo(expectedUser.getEmail())));
  }

  @Test
  @DisplayName("should return empty for unknown id")
  void should_return_empty_for_unknown_id() {
   Optional<User> result = userService.getById(999L);

   assertThat(result, isEmpty());
  }
 }


 private static Stream<User> existingUsersProvider() {
  return Stream.of(
          User.builder().id(1L).name("Ivan").surname("Ivanov").phone("89990001122").email("ivan@mail.com").age(30).build(),
          User.builder().id(2L).name("Petr").surname("Petrov").phone("89990001123").email("petr@mail.com").age(25).build(),
          User.builder().id(3L).name("Maria").surname("Sidorova").phone("89990001124").email("maria@mail.com").age(28).build()
  );
 }
}