package com.example.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.models.Cart;
import com.example.models.PromoCode;
import com.example.models.PromoType;
import com.example.models.PromoUsageType;
import com.example.repositories.CartRepositoryJdbcTemplateImpl;
import com.example.repositories.PromoCodeRepositoryJdbcTemplateImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("CartPromoService Tests")
class CartPromoServiceTest {

    private EmbeddedDatabase embeddedDatabase;
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
        promoCodeRepository = new PromoCodeRepositoryJdbcTemplateImpl(embeddedDatabase);

        cartPromoService = new CartPromoService(cartRepository, promoCodeRepository);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Nested
    @DisplayName("Apply Promo Code - Success Cases")
    class ApplyPromoCodeSuccess {

        @ParameterizedTest(name = "should apply valid promo code: {0}")
        @ValueSource(strings = {"PROMO10", "FIXED50"})
        void should_apply_valid_promo_codes_to_cart(String promoCode) {
            boolean result = cartPromoService.applyPromoCode(1L, promoCode);

            assertThat(result, equalTo(true));

            Optional<Cart> cartOpt = cartRepository.findById(1L);
            assertThat(cartOpt, isPresent());
            assertThat(cartOpt.get().getAppliedPromoCodeId(), notNullValue());
        }

        @Test
        void should_apply_multi_use_promo_multiple_times() {
            boolean result1 = cartPromoService.applyPromoCode(1L, "FIXED50");
            assertThat(result1, equalTo(true));

            boolean result2 = cartPromoService.applyPromoCode(2L, "FIXED50");
            assertThat(result2, equalTo(true));

            Optional<PromoCode> promoOpt = promoCodeRepository.findByCode("FIXED50");
            assertThat(promoOpt, isPresent());
            assertThat(promoOpt.get(), hasProperty("active", equalTo(true)));
        }
    }

    @Nested
    @DisplayName("Apply Promo Code - Failure Cases")
    class ApplyPromoCodeFailure {

        @Test
        void should_not_apply_non_existing_promo_code() {
            boolean result = cartPromoService.applyPromoCode(1L, "NONEXISTENT");

            assertThat(result, equalTo(false));

            Optional<Cart> cartOpt = cartRepository.findById(1L);
            assertThat(cartOpt, isPresent());
            assertThat(cartOpt.get().getAppliedPromoCodeId(), nullValue());
        }

        @Test
        void should_not_apply_promo_to_non_existing_cart() {
            boolean result = cartPromoService.applyPromoCode(999L, "PROMO10");

            assertThat(result, equalTo(false));
        }

        @Test
        void should_not_apply_expired_promo_code() {
            boolean result = cartPromoService.applyPromoCode(1L, "EXPIRED");

            assertThat(result, equalTo(false));

            Optional<Cart> cartOpt = cartRepository.findById(1L);
            assertThat(cartOpt.get().getAppliedPromoCodeId(), nullValue());
        }

        @Test
        void should_not_apply_inactive_promo_code() {
            PromoCode inactivePromo = PromoCode.builder()
                    .code("INACTIVE")
                    .type(PromoType.PERCENT)
                    .value(25.0)
                    .usageType(PromoUsageType.MULTI_USE)
                    .active(false)
                    .expiresAt(LocalDateTime.now().plusDays(30))
                    .build();
            promoCodeRepository.save(inactivePromo);

            boolean result = cartPromoService.applyPromoCode(1L, "INACTIVE");

            assertThat(result, equalTo(false));
        }
    }

    @Nested
    @DisplayName("Promo Code Usage Types")
    class PromoCodeUsageTypes {

        @Test
        void should_deactivate_single_use_promo_after_applying() {
            boolean result = cartPromoService.applyPromoCode(1L, "PROMO10");

            assertThat(result, equalTo(true));

            Optional<PromoCode> promoOpt = promoCodeRepository.findByCode("PROMO10");
            assertThat(promoOpt, isPresent());
            assertThat(promoOpt.get(), hasProperty("active", equalTo(false)));
        }

        @Test
        void should_not_deactivate_multi_use_promo_after_applying() {
            boolean result = cartPromoService.applyPromoCode(1L, "FIXED50");

            assertThat(result, equalTo(true));

            Optional<PromoCode> promoOpt = promoCodeRepository.findByCode("FIXED50");
            assertThat(promoOpt, isPresent());
            assertThat(promoOpt.get(), hasProperty("active", equalTo(true)));
        }
    }

    @Nested
    @DisplayName("Remove Promo Code")
    class RemovePromoCode {

        @Test
        void should_remove_promo_code_from_cart() {
            cartPromoService.applyPromoCode(1L, "FIXED50");

            Optional<Cart> cartBefore = cartRepository.findById(1L);
            assertThat(cartBefore.get().getAppliedPromoCodeId(), notNullValue());

            cartPromoService.removePromoCode(1L);

            Optional<Cart> cartAfter = cartRepository.findById(1L);
            assertThat(cartAfter.get().getAppliedPromoCodeId(), nullValue());
        }

        @Test
        void should_handle_removing_promo_from_non_existing_cart() {
            assertDoesNotThrow(() -> cartPromoService.removePromoCode(999L));
        }

        @ParameterizedTest(name = "should remove promo from cart {0}")
        @ValueSource(longs = {1L, 2L})
        void should_remove_promo_from_different_carts(Long cartId) {
            cartPromoService.applyPromoCode(cartId, "FIXED50");

            Optional<Cart> cartWithPromo = cartRepository.findById(cartId);
            assertThat(cartWithPromo.get().getAppliedPromoCodeId(), notNullValue());

            cartPromoService.removePromoCode(cartId);

            Optional<Cart> cartWithoutPromo = cartRepository.findById(cartId);
            assertThat(cartWithoutPromo.get().getAppliedPromoCodeId(), nullValue());
        }
    }

    @Nested
    @DisplayName("Complex Scenarios")
    class ComplexScenarios {

        @Test
        void should_apply_and_remove_promo_code_multiple_times() {
            cartPromoService.applyPromoCode(2L, "FIXED50");
            Optional<Cart> cart1 = cartRepository.findById(2L);
            assertThat(cart1.get().getAppliedPromoCodeId(), notNullValue());

            cartPromoService.removePromoCode(2L);
            Optional<Cart> cart2 = cartRepository.findById(2L);
            assertThat(cart2.get().getAppliedPromoCodeId(), nullValue());

            cartPromoService.applyPromoCode(2L, "FIXED50");
            Optional<Cart> cart3 = cartRepository.findById(2L);
            assertThat(cart3.get().getAppliedPromoCodeId(), notNullValue());
        }

        @Test
        void should_replace_promo_code_when_applying_new_one() {
            cartPromoService.applyPromoCode(1L, "FIXED50");
            Optional<Cart> cart1 = cartRepository.findById(1L);
            Long firstPromoId = cart1.get().getAppliedPromoCodeId();

            PromoCode newPromo = PromoCode.builder()
                    .code("NEWPROMO")
                    .type(PromoType.PERCENT)
                    .value(15.0)
                    .usageType(PromoUsageType.MULTI_USE)
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusDays(30))
                    .build();
            promoCodeRepository.save(newPromo);

            cartPromoService.applyPromoCode(1L, "NEWPROMO");
            Optional<Cart> cart2 = cartRepository.findById(1L);
            Long secondPromoId = cart2.get().getAppliedPromoCodeId();

            assertThat(secondPromoId, not(equalTo(firstPromoId)));
            assertThat(secondPromoId, equalTo(newPromo.getId()));
        }
    }
}