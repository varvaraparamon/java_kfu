package EX4.src.repositories;

import EX4.src.models.Cart;


import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CartRepositoryFileBasedImpl implements CartRepository {

    private String fileName;
    private Long currentId = 1L;

    public CartRepositoryFileBasedImpl(String fileName){
        this.fileName = fileName;
        initCurrentId();
    }

    private void initCurrentId() {


        Long maxId = 0L;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String lineFromFile = reader.readLine();
            while (lineFromFile != null) {

                String parsedLine[] = lineFromFile.split("\\|");
                Long id = Long.parseLong(parsedLine[0]);
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

        for (int i = 0; i < cart.getCurrentSize(); i++) {
            line.append(cart.getCartProductsId()[i]).append(",");
        }

        line.append("|")
                .append(cart.getCurrentSum())
                .append("|");

        return line.toString();
    };


    private Mapper<String, Cart> stringLineToCartMapper = line -> {
        String[] parts = line.split("\\|");

        Cart cart = new Cart();

        cart.setId(Long.parseLong(parts[0]));
        cart.setUserId(Long.parseLong(parts[1]));


        String productPart = parts[2]; 

        if (!productPart.isEmpty()) {
            String[] ids = productPart.split(",");
            for (String idStr : ids) {
                if (idStr.isEmpty()) 
                    continue;
                cart.addProduct(Long.parseLong(idStr));
            }
        }

        cart.setCurrentSum(Double.parseDouble(parts[3]));

        return cart;
    };


    @Override
    public void save(Cart cart) {
        File original = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(original, true))) {

            if (cart.getId() == null) {
                cart.setId(currentId++);
            }

            writer.write(cartToStringLineMapper.map(cart));
            writer.newLine();

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void update(Cart cart) {
        File original = new File(fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(original))) {

            List<String> lines = reader.lines()
                    .map(value -> stringLineToCartMapper.map(value))
                    .map(foundCart -> {
                        if (!Objects.equals(foundCart.getId(), cart.getId())) {
                            return foundCart;
                        }
                        return cart; 
                    })
                    .map(objectValue -> cartToStringLineMapper.map(objectValue))
                    .collect(Collectors.toList());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(original))) {
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public Cart findById(Long id) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String lineFromFile = reader.readLine();

            while (lineFromFile != null) {
                Cart cart = stringLineToCartMapper.map(lineFromFile);
                if (cart.getId().equals(id)) {
                    return cart;
                }
                lineFromFile = reader.readLine();
            }
            
            return null;

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } 
        
    }


}
