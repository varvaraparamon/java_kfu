package EX4.src.repositories;

import EX4.src.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryMapImpl implements UserRepository {

    private Map<Integer, User> users;
    private int currentId = 1;

    public UserRepositoryMapImpl(){
        this.users = new HashMap<>();
    }

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            user.setId(currentId++);
        }
        users.put(user.getId(), user);
    }

    @Override
    public User findById(Integer id) {
        return users.get(id);
    }


}
