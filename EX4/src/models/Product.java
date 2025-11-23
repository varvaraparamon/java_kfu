package EX4.src.models;

public class Product {
    private Integer id;
    private String name;
    private String description;
    private double price;

    private Logger logger;

    public static class ProductBuilder {
        private String name;
        private String description;
        private double price;

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }
        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }
        public ProductBuilder price(double price) {
            this.price = price;
            return this;
        }
        public Product build() {
            return new Product(name, description, price);
        }
    }
    public Product(){
        this.logger = Logger.getLogger();
        this.logger.createProduct(this);
    }


    public Product(String name, String description, double price){
        this.name = name;
        this.description = description;
        this.price = price;
        this.logger = Logger.getLogger();

        this.logger.createProduct(this);
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId() {
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

    public double getPrice(){
        return this.price;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price){
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
