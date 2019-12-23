package njp.raf.cloud.user.service;

import lombok.RequiredArgsConstructor;
import njp.raf.cloud.exception.user.InvalidUserInfoException;
import njp.raf.cloud.exception.user.UserNotFoundException;
import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
class UserPreparationService {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[ayb]\\$.{56}$");

    private final UserRepository userRepository;

    public Long prepareUserForDelete(Long id) {
        if (userNotFound(id))
            throw new UserNotFoundException(String.format("User with id: %d not found!", id));

        return id;
    }

    private boolean userNotFound(Long id) {
        return userRepository.findById(id).isEmpty();
    }

    public User prepareUserForSave(User userRequest) {
        if (usernameExists(userRequest.getUsername()))
            throw new InvalidUserInfoException(
                    String.format("Username: %s, already exists!", userRequest.getUsername())
            );

        userRequest.setPassword(hashPassword(userRequest.getPassword()));

        return userRequest;
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User prepareUserForUpdate(Long existingId, User userRequest) {
        if (userNotFound(existingId))
            throw new UserNotFoundException(String.format("User with id: %d not found!", existingId));

        if (usernameChanged(existingId, userRequest.getUsername()) && usernameExists(userRequest.getUsername()))
            throw new InvalidUserInfoException(
                    String.format("Username: %s, already exists!", userRequest.getUsername())
            );

        if (isPasswordPlain(userRequest.getPassword()))
            userRequest.setPassword(hashPassword(userRequest.getPassword()));

        userRequest.setId(existingId);

        return userRequest;
    }

    private boolean usernameChanged(Long existingId, String newUsername) {
        return !userRepository.findById(existingId)
                .orElseThrow(IllegalStateException::new)
                .getUsername().equals(newUsername);
    }

    private boolean isPasswordPlain(String password) {
        return !BCRYPT_PATTERN.matcher(password).matches();
    }

    private String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

}
