package njp.raf.cloud.user.service;

import njp.raf.cloud.exception.InvalidUserInfoException;
import njp.raf.cloud.exception.UserNotFoundException;
import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
class UserPreparationService {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[ayb]\\$.{56}$");

    private final UserRepository userRepository;

    UserPreparationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long prepareUserForDelete(Long id) {
        if (userNotFound(id))
            throw new UserNotFoundException(String.format("User with id: %d not found!", id));

        return id;
    }

    private boolean userNotFound(Long id) {
        return userRepository.findById(id).isEmpty();
    }

    public User prepareUserForSave(User user) {
        if (usernameExists(user.getUsername()))
            throw new InvalidUserInfoException(String.format("Username: %s, already exists!", user.getUsername()));

        user.setPassword(hashPassword(user.getPassword()));

        return user;
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User prepareUserForUpdate(Long existingId, User user) {
        if (isUsernameChanged(existingId, user.getUsername()) && usernameExists(user.getUsername()))
            throw new InvalidUserInfoException(String.format("Username: %s, already exists!", user.getUsername()));

        if (isPasswordPlain(user.getPassword()))
            user.setPassword(hashPassword(user.getPassword()));

        user.setId(existingId);

        return user;
    }

    private boolean isUsernameChanged(Long existingId, String newUsername) {
        return !userRepository.findById(existingId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %d not found!", existingId)))
                .getUsername().equals(newUsername);
    }

    private boolean isPasswordPlain(String password) {
        return !BCRYPT_PATTERN.matcher(password).matches();
    }

    private String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

}
