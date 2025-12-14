package EX4.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import EX4.src.models.*;
import EX4.src.repositories.*;
import EX4.src.services.*;

public class Main {
    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5442/app_db",
                "postgres", "postgres");

           
            UserRepository userRepository = new UserRepositoryJdbcImpl(connection);
            ProductRepository productRepository = new ProductRepositoryJdbcImpl(connection);
            CartRepository cartRepository = new CartRepositoryJdbcImpl(connection);
            CartProductRepository cartProductRepository = new CartProductRepositoryJdbcImpl(connection);

            
            UserService userService = new UserServiceImpl(userRepository);
            ProductService productService = new ProductServiceImpl(productRepository);
            CartService cartService = new CartServiceImpl(cartRepository, cartProductRepository);

            User user = new User.UserBuilder()
                    .name("Ivan")
                    .surname("Ivanov")
                    .phone("89990001122")
                    .email("ivan@mail.com")
                    .age(30)
                    .build();
            userService.create(user);

            System.out.println("Создан юзер с ID: " + user.getId());

            Product product = new Product.ProductBuilder()
                    .name("Milk")
                    .description("1 liter")
                    .price(90.5)
                    .build();
            productService.create(product);

            System.out.println("Создан продукт с ID: " + product.getId());


            cartService.create(user.getId());
            Cart cart = cartService.getById(7L);

            System.out.println("Создана корзина с ID: " + cart.getId());

            cartService.addProduct(cart.getId(), product.getId(), product.getPrice(), 3);

            // Cart.CartPrinter printer = cart.new CartPrinter();
            // System.out.println("Содержимое корзины:\n" + printer.print());

            cart = cartService.getById(cart.getId());
            System.out.println("Общая сумма = " + cart.getCurrentSum());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
