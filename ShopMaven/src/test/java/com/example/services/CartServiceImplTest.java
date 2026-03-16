package com.example.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.models.Cart;
import com.example.models.CartProduct;
import com.example.repositories.CartRepositoryJdbcTemplateImpl;
import com.example.repositories.CartProductRepositoryJdbcTemplateImpl;
import com.example.repositories.ProductRepositoryJdbcTemplateImpl;
import com.example.repositories.PromoCodeRepositoryJdbcTemplateImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("CartService Tests")
class CartServiceImplTest {

    private EmbeddedDatabase embeddedDatabase;
    private CartService cartService;
    private CartProductRepositoryJdbcTemplateImpl cartProductRepository;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("sql/schema.sql", "sql/data.sql")
                .build();

        CartRepositoryJdbcTemplateImpl cartRepository =
                new CartRepositoryJdbcTemplateImpl(embeddedDatabase);
        cartProductRepository = new CartProductRepositoryJdbcTemplateImpl(embeddedDatabase);
        ProductRepositoryJdbcTemplateImpl productRepository =
                new ProductRepositoryJdbcTemplateImpl(embeddedDatabase);
        PromoCodeRepositoryJdbcTemplateImpl promoCodeRepository =
                new PromoCodeRepositoryJdbcTemplateImpl(embeddedDatabase);
        CartCalculationService calculationService = new CartCalculationService(
                cartRepository, cartProductRepository, productRepository, promoCodeRepository);
        CartPromoService promoService = new CartPromoService(cartRepository, promoCodeRepository);

        cartService = new CartServiceImpl(cartRepository, cartProductRepository, calculationService, promoService);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Nested
    @DisplayName("Create Operations")
    class CreateOperations {

        @Test
        void should_create_cart_for_user() {
            cartService.createCartForUser(3L);

            Optional<Cart> cartOpt = cartService.getCartByUserId(3L);

            assertThat(cartOpt, isPresent());
            assertThat(cartOpt.get(), hasProperty("userId", equalTo(3L)));
        }

        @ParameterizedTest(name = "should create cart for user with id={0}")
        @CsvSource({
                "1",
                "2",
                "3"
        })
        void should_create_carts_for_different_users(Long userId) {
            cartService.createCartForUser(userId);

            Optional<Cart> cartOpt = cartService.getCartByUserId(userId);
            assertThat(cartOpt, isPresent());
            assertThat(cartOpt.get(), hasProperty("userId", equalTo(userId)));
        }
    }

    @Nested
    @DisplayName("Read Operations")
    class ReadOperations {

        @Test
        void should_get_existing_cart_by_id() {
            Optional<Cart> cartOpt = cartService.getCartByUserId(1L);

            assertThat(cartOpt, isPresent());
            assertThat(cartOpt.get(), hasProperty("userId", equalTo(1L)));
        }

        @Test
        void should_return_empty_optional_for_non_existing_cart() {
            Optional<Cart> cartOpt = cartService.getCartByUserId(999L);

            assertThat(cartOpt, isEmpty());
        }
    }

    @Nested
    @DisplayName("Add Product Operations")
    class AddProductOperations {

        @ParameterizedTest(name = "should add product {0} with quantity {1}")
        @CsvSource({
                "1, 2",
                "2, 5",
                "3, 10"
        })
        void should_add_different_products_to_cart(Long productId, Integer quantity) {
            Long cartId = 2L;
            int initialSize = cartProductRepository.findByCartId(cartId).size();

            cartService.addProductToCart(cartId, productId, quantity);

            List<CartProduct> cartProducts = cartProductRepository.findByCartId(cartId);
            assertThat(cartProducts, hasSize(initialSize + 1));
            assertThat(cartProducts, hasItem(
                    allOf(
                            hasProperty("productId", equalTo(productId)),
                            hasProperty("count", equalTo(quantity))
                    )
            ));
        }

        @Test
        void should_add_multiple_products_to_same_cart() {
            Long cartId = 2L;

            cartService.addProductToCart(cartId, 1L, 2);
            cartService.addProductToCart(cartId, 2L, 3);
            cartService.addProductToCart(cartId, 3L, 1);

            List<CartProduct> cartProducts = cartProductRepository.findByCartId(cartId);
            assertThat(cartProducts, hasSize(3));
        }

        @Test
        void should_throw_exception_when_adding_product_to_non_existing_cart() {
            assertThrows(IllegalArgumentException.class,
                    () -> cartService.addProductToCart(999L, 1L, 1));
        }
    }
}
