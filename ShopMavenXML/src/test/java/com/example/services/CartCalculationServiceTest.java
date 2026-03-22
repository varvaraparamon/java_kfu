package com.example.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.models.PromoCode;
import com.example.models.PromoType;
import com.example.models.PromoUsageType;
import com.example.repositories.CartRepositoryJdbcTemplateImpl;
import com.example.repositories.CartProductRepositoryJdbcTemplateImpl;
import com.example.repositories.ProductRepositoryJdbcTemplateImpl;
import com.example.repositories.PromoCodeRepositoryJdbcTemplateImpl;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("CartCalculationService Tests")
class CartCalculationServiceTest {

    private EmbeddedDatabase embeddedDatabase;
    private CartCalculationService cartCalculationService;
    private CartPromoService cartPromoService;
    private CartRepositoryJdbcTemplateImpl cartRepository;
    private PromoCodeRepositoryJdbcTemplateImpl promoCodeRepository;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("sql/schema.sql", "sql/data.sql")
                .build();

        cartRepository = new CartRepositoryJdbcTemplateImpl(embeddedDatabase);
        CartProductRepositoryJdbcTemplateImpl cartProductRepository =
                new CartProductRepositoryJdbcTemplateImpl(embeddedDatabase);
        ProductRepositoryJdbcTemplateImpl productRepository =
                new ProductRepositoryJdbcTemplateImpl(embeddedDatabase);
        promoCodeRepository = new PromoCodeRepositoryJdbcTemplateImpl(embeddedDatabase);

        cartCalculationService = new CartCalculationService(
                cartRepository,
                cartProductRepository,
                productRepository,
                promoCodeRepository
        );

        cartPromoService = new CartPromoService(cartRepository, promoCodeRepository);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Nested
    @DisplayName("Calculate Without Promo")
    class CalculateWithoutPromo {

        @Test
        void should_calculate_cart_total_without_promo() {
            double total = cartCalculationService.calculateCartTotal(1L);

            assertThat(total, closeTo(226.00, 0.01));
        }

        @Test
        void should_return_zero_for_non_existing_cart() {
            double total = cartCalculationService.calculateCartTotal(999L);

            assertThat(total, equalTo(0.0));
        }

        @Test
        void should_calculate_empty_cart_as_zero() {
            double total = cartCalculationService.calculateCartTotal(2L);

            assertThat(total, equalTo(0.0));
        }
    }

    @Nested
    @DisplayName("Calculate With Percent Promo")
    class CalculateWithPercentPromo {

        @Test
        void should_calculate_cart_total_with_percent_promo() {
            cartPromoService.applyPromoCode(1L, "PROMO10");

            double total = cartCalculationService.calculateCartTotal(1L);

            assertThat(total, closeTo(203.40, 0.01));
        }

        @ParameterizedTest(name = "with {0}% discount, total should be {1}")
        @CsvSource({
                "10.0,  203.40",
                "20.0,  180.80",
                "50.0,  113.00",
                "100.0,   0.00"
        })
        void should_calculate_with_different_percent_discounts(Double discount, Double expectedTotal) {
            PromoCode promo = PromoCode.builder()
                    .code("TEST" + discount.intValue())
                    .type(PromoType.PERCENT)
                    .value(discount)
                    .usageType(PromoUsageType.MULTI_USE)
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .build();
            promoCodeRepository.save(promo);

            cartPromoService.applyPromoCode(1L, promo.getCode());

            double total = cartCalculationService.calculateCartTotal(1L);

            assertThat(total, closeTo(expectedTotal, 0.01));
        }
    }

    @Nested
    @DisplayName("Calculate With Fixed Promo")
    class CalculateWithFixedPromo {

        @Test
        void should_calculate_cart_total_with_fixed_promo() {
            cartPromoService.applyPromoCode(1L, "FIXED50");

            double total = cartCalculationService.calculateCartTotal(1L);

            assertThat(total, closeTo(176.00, 0.01));
        }

        @ParameterizedTest(name = "with {0} rubles discount, total should be {1}")
        @CsvSource({
                "50.0,  176.00",
                "100.0, 126.00",
                "150.0,  76.00",
                "226.0,   0.00"
        })
        void should_calculate_with_different_fixed_discounts(Double discount, Double expectedTotal) {
            PromoCode promo = PromoCode.builder()
                    .code("TEST_FIXED_" + discount.intValue())
                    .type(PromoType.FIXED)
                    .value(discount)
                    .usageType(PromoUsageType.MULTI_USE)
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .build();
            promoCodeRepository.save(promo);

            cartPromoService.applyPromoCode(1L, promo.getCode());

            double total = cartCalculationService.calculateCartTotal(1L);

            assertThat(total, closeTo(expectedTotal, 0.01));
        }

        @Test
        void should_not_allow_negative_total() {
            PromoCode largeDiscount = PromoCode.builder()
                    .code("LARGE500")
                    .type(PromoType.FIXED)
                    .value(500.0)
                    .usageType(PromoUsageType.SINGLE_USE)
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .build();
            promoCodeRepository.save(largeDiscount);

            cartPromoService.applyPromoCode(1L, "LARGE500");

            double total = cartCalculationService.calculateCartTotal(1L);

            assertThat(total, greaterThanOrEqualTo(0.0));
            assertThat(total, equalTo(0.0));
        }
    }

    @Nested
    @DisplayName("Promo Application and Removal")
    class PromoApplicationRemoval {

        @Test
        void should_recalculate_after_promo_removal() {
            cartPromoService.applyPromoCode(1L, "FIXED50");

            double totalWithPromo = cartCalculationService.calculateCartTotal(1L);
            assertThat(totalWithPromo, closeTo(176.00, 0.01));

            cartPromoService.removePromoCode(1L);

            double totalWithoutPromo = cartCalculationService.calculateCartTotal(1L);
            assertThat(totalWithoutPromo, closeTo(226.00, 0.01));
        }

        @Test
        void should_handle_multiple_promo_changes() {
            double total1 = cartCalculationService.calculateCartTotal(1L);
            assertThat(total1, closeTo(226.00, 0.01));

            cartPromoService.applyPromoCode(1L, "PROMO10");
            double total2 = cartCalculationService.calculateCartTotal(1L);
            assertThat(total2, closeTo(203.40, 0.01));

            cartPromoService.removePromoCode(1L);
            double total3 = cartCalculationService.calculateCartTotal(1L);
            assertThat(total3, closeTo(226.00, 0.01));

            cartPromoService.applyPromoCode(1L, "FIXED50");
            double total4 = cartCalculationService.calculateCartTotal(1L);
            assertThat(total4, closeTo(176.00, 0.01));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        void should_handle_expired_promo_code_gracefully() {
            boolean applied = cartPromoService.applyPromoCode(1L, "EXPIRED");

            assertThat(applied, equalTo(false));

            double total = cartCalculationService.calculateCartTotal(1L);
            assertThat(total, closeTo(226.00, 0.01));
        }

        @Test
        void should_calculate_correctly_with_100_percent_discount() {
            PromoCode fullDiscount = PromoCode.builder()
                    .code("FREE100")
                    .type(PromoType.PERCENT)
                    .value(100.0)
                    .usageType(PromoUsageType.SINGLE_USE)
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .build();
            promoCodeRepository.save(fullDiscount);

            cartPromoService.applyPromoCode(1L, "FREE100");

            double total = cartCalculationService.calculateCartTotal(1L);

            assertThat(total, closeTo(0.0, 0.01));
        }

        @ParameterizedTest(name = "cart {0} should calculate correctly")
        @CsvSource({
                "1, 226.00",
                "2, 0.00"
        })
        void should_calculate_different_carts_without_promo(Long cartId, Double expectedTotal) {
            double total = cartCalculationService.calculateCartTotal(cartId);

            assertThat(total, closeTo(expectedTotal, 0.01));
        }
    }
}