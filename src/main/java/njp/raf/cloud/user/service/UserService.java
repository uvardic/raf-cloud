package njp.raf.cloud.user.service;

import njp.raf.cloud.exception.user.UserAuthenticationException;
import njp.raf.cloud.exception.user.UserNotFoundException;
import njp.raf.cloud.user.domain.TokenRequest;
import njp.raf.cloud.user.domain.TokenResponse;
import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserPreparationService userPreparationService;

    private final TokenService tokenService;

    public UserService(
            UserRepository userRepository, UserPreparationService userPreparationService, TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.userPreparationService = userPreparationService;
        this.tokenService = tokenService;
    }

    @Transactional
    public void deleteById(Long existingId) {
        userRepository.deleteById(userPreparationService.prepareUserForDelete(existingId));
    }

    @Transactional
    public User save(User userRequest) {
        return userRepository.save(userPreparationService.prepareUserForSave(userRequest));
    }

    @Transactional
    public User update(Long existingId, User userRequest) {
        return userRepository.save(userPreparationService.prepareUserForUpdate(existingId, userRequest));
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

    public TokenResponse login(TokenRequest tokenRequest) {
        User foundUser = userRepository.findByUsername(tokenRequest.getUsername())
                .filter(user -> isPasswordMatching(tokenRequest.getPassword(), user.getPassword()))
                .orElseThrow(() -> new UserAuthenticationException("Invalid email or password!"));

        return tokenService.generateTokenFor(foundUser);
    }

    private boolean isPasswordMatching(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
