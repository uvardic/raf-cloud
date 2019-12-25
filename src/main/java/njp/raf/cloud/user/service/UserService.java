package njp.raf.cloud.user.service;

import lombok.RequiredArgsConstructor;
import njp.raf.cloud.exception.user.InvalidUserInfoException;
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
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[ayb]\\$.{56}$");

    private final UserRepository userRepository;

    private final TokenService tokenService;

    @Transactional
    public void deleteById(Long existingId) {
        if (userNotFound(existingId))
            throw new UserNotFoundException(String.format("User with id: %d not found!", existingId));

        userRepository.deleteById(existingId);
    }

    private boolean userNotFound(Long id) {
        return userRepository.findById(id).isEmpty();
    }

    @Transactional
    public User save(User userRequest) {
        if (usernameExists(userRequest.getUsername()))
            throw new InvalidUserInfoException(
                    String.format("Username: %s, already exists!", userRequest.getUsername())
            );

        userRequest.setPassword(hashPassword(userRequest.getPassword()));

        return userRepository.save(userRequest);
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    @Transactional
    public User update(Long existingId, User userRequest) {
        if (userNotFound(existingId))
            throw new UserNotFoundException(String.format("User with id: %d not found!", existingId));

        if (usernameChanged(existingId, userRequest.getUsername()) && usernameExists(userRequest.getUsername()))
            throw new InvalidUserInfoException(
                    String.format("Username: %s, already exists!", userRequest.getUsername())
            );

        if (isPasswordPlain(userRequest.getPassword()))
            userRequest.setPassword(hashPassword(userRequest.getPassword()));

        userRequest.setId(existingId);

        return userRepository.save(userRequest);
    }

    private boolean usernameChanged(Long existingId, String newUsername) {
        return !userRepository.findById(existingId)
                .orElseThrow(IllegalStateException::new)
                .getUsername().equals(newUsername);
    }

    private boolean isPasswordPlain(String password) {
        return !BCRYPT_PATTERN.matcher(password).matches();
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
