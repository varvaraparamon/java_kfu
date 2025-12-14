package EX4.src.models;

public class CartProduct {
    Long id;
    Long cartId;
    Long productId;
    Integer count;

    public CartProduct(Long productId, Integer count) {
        this.productId = productId;
        this.count = count;
    }

    public CartProduct(Long cartId, Long productId, Integer count) {
        this.cartId = cartId;
        this.productId = productId;
        this.count = count;
    }

    public CartProduct(Long id, Long cartId, Long productId, Integer count) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.count = count;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getCartId() {
        return this.cartId;
    }

    public void setCartId(Long cartId){
        this.cartId = cartId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId){
        this.productId = productId;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count){
        this.count = count;
    }
}
