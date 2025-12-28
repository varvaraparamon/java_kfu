package com.example.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.models.Product;
import com.example.repositories.ProductRepositoryJdbcTemplateImpl;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static com.github.npathai.hamcrestopt.OptionalMatchers.*;

@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("ProductService Tests")
class ProductServiceImplTest {

    private EmbeddedDatabase embeddedDatabase;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("sql/schema.sql", "sql/data.sql")
                .build();

        ProductRepositoryJdbcTemplateImpl productRepository =
                new ProductRepositoryJdbcTemplateImpl(embeddedDatabase);

        productService = new ProductServiceImpl(productRepository);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Nested
    @DisplayName("Create Operations")
    class CreateOperations {

        @Test
        void should_create_product_and_set_id() {
            Product product = Product.builder()
                    .name("Butter")
                    .description("200g pack")
                    .price(120.0)
                    .build();

            productService.create(product);

            assertThat(product.getId(), notNullValue());
            assertThat(product.getId(), greaterThan(0L));
        }

        @ParameterizedTest(name = "should create: {0} - {1} ({2} руб.)")
        @CsvSource({
                "Apple,   1kg,           85.75",
                "Orange,  500g,          65.50",
                "Banana,  1 bunch,       120.00",
                "Tomato,  1kg,           95.25"
        })
        void should_create_products_with_different_prices(String name, String description, Double price) {
            Product product = Product.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .build();

            productService.create(product);

            assertThat(product.getId(), notNullValue());
            Optional<Product> savedProduct = productService.getById(product.getId());
            assertThat(savedProduct, isPresent());
            assertThat(savedProduct.get(), hasProperty("price", equalTo(price)));
        }
    }

    @Nested
    @DisplayName("Read Operations")
    class ReadOperations {

        @ParameterizedTest(name = "should find product: {0}")
        @MethodSource("com.example.services.ProductServiceImplTest#existingProductsProvider")
        void should_get_existing_products_by_id(Product expectedProduct) {
            Optional<Product> productOpt = productService.getById(expectedProduct.getId());

            assertThat(productOpt, isPresentAndIs(expectedProduct));
            assertThat(productOpt.get(), hasProperty("name", equalTo(expectedProduct.getName())));
            assertThat(productOpt.get(), hasProperty("price", equalTo(expectedProduct.getPrice())));
        }

        @Test
        void should_return_empty_optional_for_non_existing_product() {
            Optional<Product> productOpt = productService.getById(999L);

            assertThat(productOpt, isEmpty());
        }
    }

    private static Stream<Product> existingProductsProvider() {
        return Stream.of(
                Product.builder()
                        .id(1L)
                        .name("Milk")
                        .description("1 liter")
                        .price(90.50)
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("Bread")
                        .description("White bread")
                        .price(45.00)
                        .build(),
                Product.builder()
                        .id(3L)
                        .name("Cheese")
                        .description("Russian cheese 200g")
                        .price(150.00)
                        .build()
        );
    }
}