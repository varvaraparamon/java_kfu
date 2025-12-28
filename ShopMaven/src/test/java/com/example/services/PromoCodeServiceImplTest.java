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

            promoCodeService.create(promo);

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

            promoCodeService.create(promo);

            assertThat(promo.getId(), notNullValue());
            Optional<PromoCode> savedPromo = promoCodeService.getByCode(code);
            assertThat(savedPromo, isPresent());
            assertThat(savedPromo.get(), hasProperty("type", equalTo(type)));
            assertThat(savedPromo, isPresent());
            assertThat(savedPromo.get(), hasProperty("value", equalTo(value)));
        }
    }

    @Nested
    @DisplayName("Read Operations")
    class ReadOperations {

        @ParameterizedTest(name = "should find promo code: {0}")
        @MethodSource("com.example.services.PromoCodeServiceImplTest#existingPromoCodesProvider")
        void should_get_existing_promo_codes_by_id(PromoCode expectedPromo) {
            Optional<PromoCode> promoOpt = promoCodeService.getById(expectedPromo.getId());
            assertThat(promoOpt, isPresent());
            assertThat(promoOpt.get(), hasProperty("code", equalTo(expectedPromo.getCode())));
            assertThat(promoOpt.get(), hasProperty("type", equalTo(expectedPromo.getType())));
            assertThat(promoOpt.get(), hasProperty("value", equalTo(expectedPromo.getValue())));
        }

        @ParameterizedTest(name = "should find by code: {0}")
        @ValueSource(strings = {"PROMO10", "FIXED50", "EXPIRED"})
        void should_get_existing_promo_codes_by_code(String code) {
            Optional<PromoCode> promoOpt = promoCodeService.getByCode(code);

            assertThat(promoOpt, isPresent());
            assertThat(promoOpt.get(), hasProperty("code", equalTo(code)));
        }

        @Test
        void should_return_empty_optional_for_non_existing_promo_code() {
            Optional<PromoCode> promoOpt = promoCodeService.getByCode("NONEXISTENT");

            assertThat(promoOpt, isEmpty());
        }

        @Test
        void should_get_all_promo_codes() {
            List<PromoCode> promoCodes = promoCodeService.getAll();

            assertThat(promoCodes, hasSize(greaterThanOrEqualTo(3)));
        }
    }

    @Nested
    @DisplayName("Update Operations")
    class UpdateOperations {

        @Test
        void should_update_promo_code_value() {
            Optional<PromoCode> promoOpt = promoCodeService.getById(1L);
            assertThat(promoOpt, isPresent());

            PromoCode promo = promoOpt.get();
            promo.setValue(20.0);

            promoCodeService.update(promo);

            Optional<PromoCode> updatedPromo = promoCodeService.getById(1L);
            assertThat(updatedPromo, isPresent());
            assertThat(updatedPromo.get(), hasProperty("value", equalTo(20.0)));
        }

        @Test
        void should_deactivate_promo_code() {
            Optional<PromoCode> promoOpt = promoCodeService.getById(1L);
            assertThat(promoOpt, isPresent());

            PromoCode promo = promoOpt.get();
            promo.setActive(false);

            promoCodeService.update(promo);

            Optional<PromoCode> updatedPromo = promoCodeService.getById(1L);
            assertThat(updatedPromo, isPresent());
            assertThat(updatedPromo.get(), hasProperty("active", equalTo(false)));
        }

        @ParameterizedTest(name = "should update value to {0}")
        @ValueSource(doubles = {5.0, 15.0, 25.0, 50.0})
        void should_update_promo_code_with_different_values(Double newValue) {
            Optional<PromoCode> promoOpt = promoCodeService.getById(2L);
            PromoCode promo = promoOpt.get();
            promo.setValue(newValue);

            promoCodeService.update(promo);

            Optional<PromoCode> updatedPromo = promoCodeService.getById(2L);
            assertThat(updatedPromo, isPresent());
            assertThat(updatedPromo.get(), hasProperty("value", equalTo(newValue)));
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

            promoCodeService.create(promo);
            Long promoId = promo.getId();

            promoCodeService.delete(promoId);

            Optional<PromoCode> deletedPromo = promoCodeService.getById(promoId);
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