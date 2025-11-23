package EX4.src.services;

import EX4.src.models.User;
import EX4.src.repositories.UserRepository;

public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        userRepository.save(user);
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id);
    }
    
    

}
