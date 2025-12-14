package EX4.src.models;


public class Cart {

    private Long id;
    private Long userId;
    private Long[] cartProductsId = new Long[50]; //лист айдишников, отдельная сущность
    private Integer currentSize = 0;
    private Double currentSum = 0.0;

    public static class CartBuilder {
        private Long userId;


        public CartBuilder user(Long userId) {
            this.userId = userId;
            return this;
        }

        public Cart build() {
            return new Cart(userId);
        }
    }

    public Cart(){

    }


    public Cart(Long id, Long userId, Integer currentSize, Double currentSum){
        this.id = id;
        this.userId = userId;
        this.currentSize = currentSize;
        this.currentSum = currentSum;
    }

    public Cart(Long userId){
        this.userId = userId;
        
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void addProduct(Long cartProductId) {
        cartProductsId[currentSize++] = cartProductId;
    }

    public Long getUserId(){
        return this.userId;
    }

    public Integer getCurrentSize(){
        return this.currentSize;
    }


    public Double getCurrentSum(){
        return this.currentSum;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setCurrentSize(Integer currentSize){
        this.currentSize = currentSize;
    }


    public void setCurrentSum(Double currentSum){
        this.currentSum = currentSum;
    }

    public Long[] getCartProductsId(){
        return this.cartProductsId;
    }

    // public class CartPrinter {
    //     public String print() {
    //         String result = "";
    //         for (int i = 0; i < currentSize; i++) {
    //             result += cartProductsId[i].productId + ": " + cartProductsId[i].count + "\n";
    //         }
    //         return result;
    //     }
    // }
    
}
