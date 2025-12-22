package com.example;

import java.time.LocalDateTime;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.models.*;
import com.example.repositories.*;
import com.example.services.*;

public class Main {

    public static void main(String[] args) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5442/app_db");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");

        UserRepository userRepository =
                new UserRepositoryJdbcTemplateImpl(dataSource);
        ProductRepository productRepository =
                new ProductRepositoryJdbcTemplateImpl(dataSource);
        CartRepository cartRepository =
                new CartRepositoryJdbcTemplateImpl(dataSource);
        CartProductRepository cartProductRepository =
                new CartProductRepositoryJdbcTemplateImpl(dataSource);
        PromoCodeRepository promoCodeRepository = 
                new PromoCodeRepositoryJdbcTemplateImpl(dataSource);


        UserService userService =
                new UserServiceImpl(userRepository);
        ProductService productService =
                new ProductServiceImpl(productRepository);
        CartService cartService =
                new CartServiceImpl(cartRepository, cartProductRepository);
        PromoCodeService promoCodeService =
                new PromoCodeServiceImpl(promoCodeRepository);
        CartPromoService cartPromoService = 
                new CartPromoService(cartRepository, promoCodeRepository);
        CartCalculationService cartCalculationService = 
                new CartCalculationService(
                        cartRepository,
                        cartProductRepository,
                        productRepository,
                        promoCodeRepository
                );

        User user = User.builder()
                .name("Ivan")
                .surname("Ivanov")
                .phone("89990001122")
                .email("ivan@mail.com")
                .age(30)
                .build();

        userService.create(user);
        System.out.println("Создан пользователь, id = " + user.getId());

        Product product = Product.builder()
                .name("Milk")
                .description("1 liter")
                .price(90.5)
                .build();

        productService.create(product);
        System.out.println("Создан продукт, id = " + product.getId());

        cartService.create(user.getId());
        Cart cart = cartService.getById(1L)
                .orElseThrow(() -> new RuntimeException("Корзина не найдена"));

        System.out.println("Создана корзина, id = " + cart.getId());

        cartService.addProduct(cart.getId(), product.getId(), 3);

        System.out.println("Товар добавлен в корзину");

        PromoCode promo = PromoCode.builder()
                .code("PROMO10")
                .type(PromoType.PERCENT)        
                .value(10.0)                    
                .usageType(PromoUsageType.SINGLE_USE)
                .active(true)
                .expiresAt(LocalDateTime.now().plusDays(7)) 
                .build();

        promoCodeService.create(promo);
        System.out.println("Создан промокод: " + promo.getCode());

        boolean applied = cartPromoService.applyPromoCode(cart.getId(), "PROMO10");
        System.out.println("Промокод применён: " + applied);

        double total = cartCalculationService.calculateCartTotal(cart.getId());
        System.out.println("Итоговая стоимость корзины с промокодом: " + total);

        cartPromoService.removePromoCode(cart.getId());
        System.out.println("Промокод отменён");

        double totalWithoutPromo = cartCalculationService.calculateCartTotal(cart.getId());
        System.out.println("Стоимость корзины без промокода: " + totalWithoutPromo);
    }
}