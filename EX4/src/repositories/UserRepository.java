package EX4.src.repositories;

import EX4.src.models.User;

public interface UserRepository {

    void save(User user);

    User findById(Long id);

}
