package EX4.src.models;

public class Cart {

    private Integer id;
    private Integer userId;
    private CartProduct[] cartProducts = new CartProduct[50]; //лист айдишников, отдельная сущность
    private int currentSize = 0;
    private double currentSum = 0;
    private Logger logger; //убрать и в сервис добавить, в продукт и юзер так же, либо импорт из пакета утилс

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

    public Cart(){
        this.logger = Logger.getLogger();
        this.logger.createCart(this);
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

    public int getCurrentSize(){
        return this.currentSize;
    }


    public double getCurrentSum(){
        return this.currentSum;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public void setCurrentSize(int currentSize){
        this.currentSize = currentSize;
    }


    public void getCurrentSum(double currentSum){
        this.currentSum = currentSum;
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
