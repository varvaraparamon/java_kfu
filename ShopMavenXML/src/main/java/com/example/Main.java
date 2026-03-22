package com.example;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.models.Cart;
import com.example.models.CartProduct;
import com.example.models.Product;
import com.example.models.PromoCode;
import com.example.models.PromoType;
import com.example.models.PromoUsageType;
import com.example.models.User;
import com.example.services.CartCalculationService;
import com.example.services.CartPromoService;
import com.example.services.CartService;
import com.example.services.ProductService;
import com.example.services.PromoCodeService;
import com.example.services.UserService;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        String suffix = String.valueOf(System.currentTimeMillis() % 1_000_000);

        UserService userService = context.getBean(UserService.class);
        ProductService productService = context.getBean(ProductService.class);
        CartService cartService = context.getBean(CartService.class);
        PromoCodeService promoCodeService = context.getBean(PromoCodeService.class);
        CartPromoService cartPromoService = context.getBean(CartPromoService.class);
        CartCalculationService cartCalculationService = context.getBean(CartCalculationService.class);

        User user = User.builder()
                .name("Ivan")
                .surname("Ivanov")
                .phone("8999" + suffix)
                .email("ivan" + suffix + "@mail.com")
                .age(30)
                .build();
        userService.createUser(user);
        System.out.println("Создан пользователь, id = " + user.getId());

        user.setAge(31);
        userService.updateUser(user.getId(), user);
        System.out.println("Пользователь обновлён, возраст = 31");

        userService.getUserById(user.getId()).ifPresent(u ->
                System.out.println("Пользователь по id, email = " + u.getEmail()));
        userService.getUserByEmail(user.getEmail()).ifPresent(u ->
                System.out.println("Пользователь по email, id = " + u.getId()));
        System.out.println("Всего пользователей = " + userService.getAllUsers().size());

        User userToDelete = User.builder()
                .name("Petr")
                .surname("Petrov")
                .phone("8777" + suffix)
                .email("petr" + suffix + "@mail.com")
                .age(25)
                .build();
        userService.createUser(userToDelete);
        userService.deleteUser(userToDelete.getId());
        System.out.println("Пользователь удалён, id = " + userToDelete.getId());

        Product product = Product.builder()
                .name("Milk")
                .description("1 liter")
                .price(90.5)
                .build();
        productService.createProduct(product);
        System.out.println("Создан продукт, id = " + product.getId());

        product.setPrice(95.0);
        productService.updateProduct(product.getId(), product);
        System.out.println("Продукт обновлён, цена = 95.0");

        productService.getProductById(product.getId()).ifPresent(p ->
                System.out.println("Продукт по id, name = " + p.getName()));
        System.out.println("Всего продуктов = " + productService.getAllProducts().size());

        Product searchProduct = Product.builder()
                .name("Milk chocolate")
                .description("100g")
                .price(120.0)
                .build();
        productService.createProduct(searchProduct);
        System.out.println("Найдено продуктов по названию = " +
                productService.searchProductsByName("milk").size());

        productService.deleteProduct(searchProduct.getId());
        System.out.println("Продукт удалён, id = " + searchProduct.getId());

        Cart cart = cartService.createCartForUser(user.getId());
        cart = cartService.getCartByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Корзина не найдена"));
        System.out.println("Создана корзина, id = " + cart.getId());

        cartService.addProductToCart(user.getId(), product.getId(), 3);
        System.out.println("Товар добавлен в корзину");

        cartService.updateProductCount(user.getId(), product.getId(), 5);
        System.out.println("Количество товара обновлено до 5");

        List<CartProduct> cartProducts = cartService.getCartProducts(cart.getId());
        System.out.println("Товаров в корзине = " + cartProducts.size());

        double subtotal = cartCalculationService.calculateSubtotal(cart.getId());
        double total = cartCalculationService.calculateCartTotal(cart.getId());
        int itemsCount = cartCalculationService.getTotalItemsCount(cart.getId());
        System.out.println("Промежуточная сумма = " + subtotal);
        System.out.println("Итого с промокодом = " + total);
        System.out.println("Количество товаров = " + itemsCount);

        cartService.removeProductFromCart(user.getId(), product.getId());
        System.out.println("Товар удалён из корзины");

        PromoCode promo = PromoCode.builder()
                .code("PROMO" + suffix)
                .type(PromoType.PERCENT)
                .value(10.0)
                .usageType(PromoUsageType.SINGLE_USE)
                .active(true)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        promoCodeService.createPromoCode(promo);
        System.out.println("Создан промокод: " + promo.getCode());

        promo.setValue(15.0);
        promoCodeService.updatePromoCode(promo.getId(), promo);
        System.out.println("Промокод обновлён: значение = 15.0");

        promoCodeService.getPromoCodeById(promo.getId()).ifPresent(p ->
                System.out.println("Промокод по id, code = " + p.getCode()));
        promoCodeService.getPromoCodeByCode(promo.getCode()).ifPresent(p ->
                System.out.println("Промокод по code, id = " + p.getId()));
        System.out.println("Активных промокодов = " + promoCodeService.getAllActivePromoCodes().size());
        System.out.println("Промокод валиден = " + promoCodeService.isValidPromoCode(promo.getCode()));

        boolean applied = cartPromoService.applyPromoCode(cart.getId(), promo.getCode());
        System.out.println("Промокод применён: " + applied);
        System.out.println("Промокод валиден для корзины = " +
                cartPromoService.isPromoCodeValidForCart(cart.getId(), promo.getCode()));

        cartPromoService.removePromoCode(cart.getId());
        System.out.println("Промокод удалён из корзины");

        PromoCode promoToDelete = PromoCode.builder()
                .code("DEL" + suffix)
                .type(PromoType.FIXED)
                .value(20.0)
                .usageType(PromoUsageType.MULTI_USE)
                .active(true)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        promoCodeService.createPromoCode(promoToDelete);
        promoCodeService.deletePromoCode(promoToDelete.getId());
        System.out.println("Промокод удалён, id = " + promoToDelete.getId());

        cartService.clearCart(user.getId());
        System.out.println("Корзина очищена");
    }
}
