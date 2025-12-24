// package com.example.services;

// import org.junit.jupiter.api.*;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.MethodSource;
// import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
// import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
// import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

// import com.example.models.User;
// import com.example.repositories.UserRepositoryJdbcTemplateImpl;

// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Stream;

// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.Matchers.*;
// import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;

// @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
// class UserServiceImplTest {

//     private EmbeddedDatabase embeddedDatabase;

//     private UserService userService;

//     @BeforeEach
//     void setUp() {
//         embeddedDatabase = new EmbeddedDatabaseBuilder()
//                 .setType(EmbeddedDatabaseType.H2)
//                 .addScripts("sql/schema.sql", "sql/data.sql")
//                 .build();

//         UserRepositoryJdbcTemplateImpl userRepository =
//                 new UserRepositoryJdbcTemplateImpl(embeddedDatabase);

//         userService = new UserServiceImpl(userRepository);
//     }

//     @AfterEach
//     void tearDown() {
//         embeddedDatabase.shutdown();
//     }

// }