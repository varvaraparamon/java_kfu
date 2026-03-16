package com.example.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.example.models.PromoCode;
import com.example.models.PromoType;
import com.example.models.PromoUsageType;
import com.example.repositories.PromoCodeRepositoryJdbcTemplateImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.github.npathai.hamcrestopt.OptionalMatchers.*;


@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("PromoCodeService Tests")
class PromoCodeServiceImplTest {

    private EmbeddedDatabase embeddedDatabase;
    private PromoCodeService promoCodeService;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("sql/schema.sql", "sql/data.sql")
                .build();

        PromoCodeRepositoryJdbcTemplateImpl promoCodeRepository =
                new PromoCodeRepositoryJdbcTemplateImpl(embeddedDatabase);

        promoCodeService = new PromoCodeServiceImpl(promoCodeRepository);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Nested
    @DisplayName("Create Operations")
    class CreateOperations {

        @Test
        void should_create_promo_code_and_set_id() {
            PromoCode promo = PromoCode.builder()
                    .code("SUMMER2024")
                    .type(PromoType.PERCENT)
                    .value(15.0)
                    .usageType(PromoUsageType.MULTI_USE)
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusDays(30))
                    .build();

            promoCodeService.createPromoCode(promo);

            assertThat(promo.getId(), notNullValue());
            assertThat(promo.getId(), greaterThan(0L));
        }

        @ParameterizedTest(name = "should create {0} promo with {1}% discount")
        @CsvSource({
                "PERCENT, SPRING10,  10.0, SINGLE_USE",
                "PERCENT, SUMMER20,  20.0, MULTI_USE",
                "PERCENT, AUTUMN15,  15.0, SINGLE_USE",
                "FIXED,   FLAT50,    50.0, MULTI_USE",
                "FIXED,   FLAT100,  100.0, SINGLE_USE"
        })
        void should_create_different_types_of_promo_codes(
                PromoType type, String code, Double value, PromoUsageType usageType) {

            PromoCode promo = PromoCode.builder()
                    .code(code)
                    .type(type)
                    .value(value)
                    .usageType(usageType)
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusDays(30))
                    .build();

            promoCodeService.createPromoCode(promo);

            assertThat(promo.getId(), notNullValue());
            Optional<PromoCode> savedPromo = promoCodeService.getPromoCodeByCode(code);
            assertThat(savedPromo, isPresent());
            assertThat(savedPromo.get(), hasProperty("type", equalTo(type)));
            assertThat(savedPromo, isPresent());
            assertThat(savedPromo.get(), hasProperty("value", equalTo(value)));
        }

        @Test
        void should_not_create_promo_code_with_duplicate_code() {
            PromoCode promo = PromoCode.builder()
                    .code("PROMO10")
                    .type(PromoType.PERCENT)
                    .value(5.0)
                    .usageType(PromoUsageType.SINGLE_USE)
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusDays(10))
                    .build();

            assertThrows(Exception.class, () -> promoCodeService.createPromoCode(promo));
        }
    }

    @Nested
    @DisplayName("Read Operations")
    class ReadOperations {

        @ParameterizedTest(name = "should find promo code: {0}")
        @MethodSource("com.example.services.PromoCodeServiceImplTest#existingPromoCodesProvider")
        void should_get_existing_promo_codes_by_id(PromoCode expectedPromo) {
            Optional<PromoCode> promoOpt = promoCodeService.getPromoCodeById(expectedPromo.getId());
            assertThat(promoOpt, isPresent());
            assertThat(promoOpt.get(), hasProperty("code", equalTo(expectedPromo.getCode())));
            assertThat(promoOpt.get(), hasProperty("type", equalTo(expectedPromo.getType())));
            assertThat(promoOpt.get(), hasProperty("value", equalTo(expectedPromo.getValue())));
        }

        @ParameterizedTest(name = "should find by code: {0}")
        @ValueSource(strings = {"PROMO10", "FIXED50", "EXPIRED"})
        void should_get_existing_promo_codes_by_code(String code) {
            Optional<PromoCode> promoOpt = promoCodeService.getPromoCodeByCode(code);

            assertThat(promoOpt, isPresent());
            assertThat(promoOpt.get(), hasProperty("code", equalTo(code)));
        }

        @Test
        void should_return_empty_optional_for_non_existing_promo_code() {
            Optional<PromoCode> promoOpt = promoCodeService.getPromoCodeByCode("NONEXISTENT");

            assertThat(promoOpt, isEmpty());
        }

        @Test
        void should_get_all_promo_codes() {
            List<PromoCode> promoCodes = promoCodeService.getAllActivePromoCodes();

            assertThat(promoCodes, hasSize(greaterThanOrEqualTo(2)));
        }
    }

    @Nested
    @DisplayName("Update Operations")
    class UpdateOperations {

        @Test
        void should_update_promo_code_value() {
            Optional<PromoCode> promoOpt = promoCodeService.getPromoCodeById(1L);
            assertThat(promoOpt, isPresent());

            PromoCode promo = promoOpt.get();
            promo.setValue(20.0);

            promoCodeService.updatePromoCode(promo.getId(), promo);

            Optional<PromoCode> updatedPromo = promoCodeService.getPromoCodeById(1L);
            assertThat(updatedPromo, isPresent());
            assertThat(updatedPromo.get(), hasProperty("value", equalTo(20.0)));
        }

        @Test
        void should_deactivate_promo_code() {
            Optional<PromoCode> promoOpt = promoCodeService.getPromoCodeById(1L);
            assertThat(promoOpt, isPresent());

            PromoCode promo = promoOpt.get();
            promo.setActive(false);

            promoCodeService.updatePromoCode(promo.getId(), promo);

            Optional<PromoCode> updatedPromo = promoCodeService.getPromoCodeById(1L);
            assertThat(updatedPromo, isPresent());
            assertThat(updatedPromo.get(), hasProperty("active", equalTo(false)));
        }

        @ParameterizedTest(name = "should update value to {0}")
        @ValueSource(doubles = {5.0, 15.0, 25.0, 50.0})
        void should_update_promo_code_with_different_values(Double newValue) {
            Optional<PromoCode> promoOpt = promoCodeService.getPromoCodeById(2L);
            PromoCode promo = promoOpt.get();
            promo.setValue(newValue);

            promoCodeService.updatePromoCode(promo.getId(), promo);

            Optional<PromoCode> updatedPromo = promoCodeService.getPromoCodeById(2L);
            assertThat(updatedPromo, isPresent());
            assertThat(updatedPromo.get(), hasProperty("value", equalTo(newValue)));
        }

        @Test
        void should_not_update_promo_code_if_code_belongs_to_another_promo() {
            PromoCode promo = promoCodeService.getPromoCodeById(2L).orElseThrow();

            promo.setCode("PROMO10");

            assertThrows(Exception.class, () -> promoCodeService.updatePromoCode(promo.getId(), promo));
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteOperations {

        @Test
        void should_delete_promo_code() {
            PromoCode promo = PromoCode.builder()
                    .code("TODELETE")
                    .type(PromoType.PERCENT)
                    .value(5.0)
                    .usageType(PromoUsageType.SINGLE_USE)
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusDays(1))
                    .build();

            promoCodeService.createPromoCode(promo);
            Long promoId = promo.getId();

            promoCodeService.deletePromoCode(promoId);

            Optional<PromoCode> deletedPromo = promoCodeService.getPromoCodeById(promoId);
            assertThat(deletedPromo, isEmpty());
        }
    }

    private static Stream<PromoCode> existingPromoCodesProvider() {
        return Stream.of(
                PromoCode.builder()
                        .id(1L)
                        .code("PROMO10")
                        .type(PromoType.PERCENT)
                        .value(10.0)
                        .usageType(PromoUsageType.SINGLE_USE)
                        .active(true)
                        .build(),
                PromoCode.builder()
                        .id(2L)
                        .code("FIXED50")
                        .type(PromoType.FIXED)
                        .value(50.0)
                        .usageType(PromoUsageType.MULTI_USE)
                        .active(true)
                        .build()
        );
    }
}
