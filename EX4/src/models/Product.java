package EX4.src.models;



public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;


    public static class ProductBuilder {
        private String name;
        private String description;
        private Double price;

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }
        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }
        public ProductBuilder price(Double price) {
            this.price = price;
            return this;
        }
        public Product build() {
            return new Product(name, description, price);
        }
    }
    public Product(){

    }


    public Product(String name, String description, Double price){
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(Long id, String name, String description, Double price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String productToString(){
        return (this.name + " " + this.description + " " + this.price);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Double getPrice(){
        return this.price;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price){
        this.price = price;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null){
            return false;
        }
        if ( ! (other instanceof Product)){
            return false;
        }

        if (this == other){
            return true;
        }
        Product that = (Product)other;

        if ((this.name.equals(that.getName())) 
            && (this.description.equals(that.getDescription())) 
            && (this.price  == that.getPrice())){
                return true;
            }
        return false;
    }
    
}
