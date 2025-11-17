package EX4.src.services;

import EX4.src.models.User;

public interface UserService {
    void create(User user);
    User getById(Integer id);
}
