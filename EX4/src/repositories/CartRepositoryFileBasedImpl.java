package EX4.src.repositories;

import EX4.src.models.Cart;

import java.io.*;

public class CartRepositoryFileBasedImpl implements CartRepository{

    private String fileName;
    private int currentId = 1;

    public CartRepositoryFileBasedImpl(){
        this.fileName = fileName;
        initCurrentId();
    }

    private void initCurrentId() {


        int maxId = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String lineFromFile = reader.readLine();
            while (lineFromFile != null) {

                String parsedLine[] = lineFromFile.split("\\|");
                int id = Integer.parseInt(parsedLine[0]);
                if (id > maxId) {
                    maxId = id;
                }
                lineFromFile = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        currentId = maxId + 1;
}

    private Mapper<Cart, String> cartToStringLineMapper = cart -> {
        StringBuilder line = new StringBuilder();
        line.append(cart.getId())
                .append("|")
                .append(cart.getUserId())
                .append("|");

        Cart.CartPrinter printer = cart.new CartPrinter();

        StringBuilder products = new StringBuilder();

        String printed = printer.print().replace("\n", ",");
            if (!printed.isEmpty()) {
            String[] lines = printed.split("\n");
            for (String l : lines) {
                if (l.isEmpty()) continue;
                products.append(l.replace(" ", "")).append(","); 
        }

        line.append(printed)
            .append("|")
            .append(cart.getCurrentSum())
            .append("|");

      return line.toString();
    };

    private Mapper<String, Cart> stringLineToCarttMapper = line -> {
        Cart cart = new Cart();
        String parsedLine[] = line.split("\\|");
        cart.setId(Integer.parseInt(parsedLine[0]));
        cart.setUserId(Integer.parseInt(parsedLine[1]));
        product.setPrice(Double.parseDouble(parsedLine[2]));

        return product;
    };


    @Override
    public void save(Cart cart) {
        if (cart.getId() == null) {
            cart.setId(currentId++);
        }
        carts.put(cart.getId(), cart);
    }

    @Override
    public Cart findById(Integer id) {
        return carts.get(id);
    }


}
