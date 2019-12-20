package njp.raf.cloud.user.service;

import njp.raf.cloud.exception.UserNotFoundException;
import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserPreparationService userPreparationService;

    public UserService(UserRepository userRepository, UserPreparationService userPreparationService) {
        this.userRepository = userRepository;
        this.userPreparationService = userPreparationService;
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(userPreparationService.prepareUserForDelete(id));
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(userPreparationService.prepareUserForSave(user));
    }

    @Transactional
    public User update(Long existingId, User user) {
        return userRepository.save(userPreparationService.prepareUserForUpdate(existingId, user));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id: %d was't found!", id)
                ));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with username: %s wasn't found!", username)
                ));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
