package EX4.src.services;

import EX4.src.Logger;
import EX4.src.models.User;
import EX4.src.repositories.UserRepository;

public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private Logger logger;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
        this.logger = Logger.getLogger();
    }

    @Override
    public void create(User user) {
        userRepository.save(user);
        logger.createUser(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id);
    }
    
    

}
