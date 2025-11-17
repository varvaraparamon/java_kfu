package EX4.src.models;

public class Cart {

    private Integer id;
    private Integer userId;
    private CartProduct[] cartProducts = new CartProduct[50];
    private int currentSize = 0;
    private double currentSum = 0;
    private Logger logger;

    public static class CartBuilder {
        private Integer userId;


        public CartBuilder user(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Cart build() {
            return new Cart(userId);
        }
    }


    public Cart(int userId){
        this.userId = userId;
        
        this.logger = Logger.getLogger();
        this.logger.createCart(this);
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void addProduct(int productId, double price, int amount){
        for (int i = 0; i < currentSize; i++) {
            if (cartProducts[i].productId.equals(productId)) {
                cartProducts[i].count += amount;
                this.currentSum += price * amount;
                this.logger.addProduct(productId);
                return;
            }
        }

        cartProducts[currentSize] = new CartProduct(productId, amount);
        this.currentSize++;
        this.currentSum += price * amount;
        this.logger.addProduct(productId);
    }

    public int getUserId(){
        return this.userId;
    }


    public double getCurrentSum(){
        return this.currentSum;
    }

    private static class CartProduct {
        Integer productId;
        int count;

        CartProduct(int productId, int count) {
            this.productId = productId;
            this.count = count;
        }
    }

    public class CartPrinter {
        public String print() {
            String result = "";
            for (int i = 0; i < currentSize; i++) {
                result += cartProducts[i].productId + ": " + cartProducts[i].count + "\n";
            }
            return result;
        }
    }
    
}
