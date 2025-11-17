package EX4.src.models;

public class Logger {
    private static Logger instance;

    static {
        instance = new Logger();
    }

    private Logger() {

    }

    public static Logger getLogger() {
        return instance;
    }

    public void addProduct(int productId) {
        System.out.println("Добавлен новый товар: " + productId + " Id");
    }

    public void createUser(User user) {
        System.out.println("Создан новый юзер: " + user.userToString());
    }

    public void createCart(Cart cart) {
        System.out.println("Для пользователя " + cart.getUserId() + " создана корзина");
    }

    public void createProduct(Product product) {
        System.out.println("Создан новый товар: " + product.productToString());
    }
}
