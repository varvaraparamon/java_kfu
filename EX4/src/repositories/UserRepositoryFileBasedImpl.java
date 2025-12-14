package EX4.src.repositories;

import EX4.src.models.User;


import java.io.*;


public class UserRepositoryFileBasedImpl implements UserRepository {

    private String fileName;
    private Long currentId = 1L;

    public UserRepositoryFileBasedImpl(String fileName){
        this.fileName = fileName;
        initCurrentId();
    }

    private void initCurrentId() {


        Long maxId = 0L;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
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

    private Mapper<User, String> userToStringLineMapper = user -> {
        StringBuilder line = new StringBuilder();
        line.append(user.getId())
                .append("|")
                .append(user.getName())
                .append("|");

        if (user.getSurname() == null) {
            line.append("NULL")
            .append("|");
        } else {
            line.append(user.getSurname())
                    .append("|");
        }

        line.append(user.getPhone())
                .append("|")
                .append(user.getEmail())
                .append("|");

        if (user.getAge() == 0) {
            line.append("NULL")
                    .append("|");
        } else {
            line.append(user.getAge())
                    .append("|");
        }

      return line.toString();
    };

    private Mapper<String, User> stringLineToUserMapper = line -> {
        User user = new User();
        String parsedLine[] = line.split("\\|");
        user.setId(Long.parseLong(parsedLine[0]));
        user.setName(parsedLine[1]);
        if (parsedLine[2].equals("NULL")) {
            user.setSurname(null);
        } else {
            user.setSurname(parsedLine[2]);
        }
        user.setPhone(parsedLine[3]);
        user.setEmail(parsedLine[4]);

        if (parsedLine[5].equals("NULL")) {
            user.setAge(0);
        } else {
            user.setAge(Integer.parseInt(parsedLine[5]));
        }

        return user;
    };



    @Override
    public void save(User user) {
        if (user.getId() == null) {
            user.setId(currentId++);
        }

        String lineToSave = userToStringLineMapper.map(user);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))){
            writer.write(lineToSave + "\n");
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
    @Override
    public User findById(Long id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String lineFromFile = reader.readLine();
            while (lineFromFile != null) {
                User user = stringLineToUserMapper.map(lineFromFile);
                if (user.getId().equals(id)) {
                    return user;
                }
                lineFromFile = reader.readLine();
            }

            return  null;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


}
