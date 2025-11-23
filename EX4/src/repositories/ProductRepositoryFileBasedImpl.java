package EX4.src.repositories;

import EX4.src.models.Product;

import java.io.*;

public class ProductRepositoryFileBasedImpl implements ProductRepository{
    private String fileName;
    private int currentId = 1;

    public ProductRepositoryFileBasedImpl(String fileName){
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

    private Mapper<Product, String> productToStringLineMapper = product -> {
        StringBuilder line = new StringBuilder();
        line.append(product.getId())
                .append("|")
                .append(product.getName())
                .append("|")
                .append(product.getDescription())
                .append("|")
                .append(product.getPrice())
                .append("|");

      return line.toString();
    };

    private Mapper<String, Product> stringLineToProductMapper = line -> {
        Product product = new Product();
        String parsedLine[] = line.split("\\|");
        product.setId(Integer.parseInt(parsedLine[0]));
        product.setName(parsedLine[1]);
        product.setPrice(Double.parseDouble(parsedLine[2]));

        return product;
    };



    @Override
    public void save(Product product) {
        if (product.getId() == null) {
            product.setId(currentId++);
        }

        String lineToSave = productToStringLineMapper.map(product);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(lineToSave + "\n");
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
    @Override
    public Product findById(Integer id) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String lineFromFile = reader.readLine();
            while (lineFromFile != null) {
                Product product = stringLineToProductMapper.map(lineFromFile);
                if (product.getId().equals(id)) {
                    return product;
                }
                lineFromFile = reader.readLine();
            }

            return  null;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
