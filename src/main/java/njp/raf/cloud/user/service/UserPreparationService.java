package njp.raf.cloud.user.service;

import njp.raf.cloud.exception.InvalidUserInfoException;
import njp.raf.cloud.exception.UserNotFoundException;
import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserPreparationService {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[ayb]\\$.{56}$");

    private static final String USERNAME_SIZE_ERROR_MESSAGE;

    private static final String FIRST_NAME_SIZE_ERROR_MESSAGE;

    private static final String LAST_NAME_SIZE_ERROR_MESSAGE;

    static {
        USERNAME_SIZE_ERROR_MESSAGE = String.format(
                "Invalid username! Username size must be between %d and %d!",
                User.MIN_USERNAME_SIZE, User.MAX_USERNAME_SIZE
        );

        FIRST_NAME_SIZE_ERROR_MESSAGE = String.format(
                "Invalid first name! First name size must be between %d and %d!",
                User.MIN_FIRST_NAME_SIZE, User.MAX_FIRST_NAME_SIZE
        );

        LAST_NAME_SIZE_ERROR_MESSAGE = String.format(
                "Invalid last name! Last name size must be between %d and %d!",
                User.MIN_LAST_NAME_SIZE, User.MAX_LAST_NAME_SIZE
        );
    }

    private final UserRepository userRepository;

    public UserPreparationService(UserRepository userRepository) {
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

//        validateUserAttributeSize(user);

        user.setPassword(hashPassword(user.getPassword()));

        return user;
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User prepareUserForUpdate(Long existingId, User user) {
//        validateUserAttributeSize(user);

        if (isPasswordPlain(user.getPassword()))
            user.setPassword(hashPassword(user.getPassword()));

        user.setId(existingId);

        return user;
    }

    private void validateUserAttributeSize(User user) {
        if (isUsernameSizeInvalid(user.getUsername()))
            throw new InvalidUserInfoException(USERNAME_SIZE_ERROR_MESSAGE);

        if (isFirstNameSizeInvalid(user.getFirstName()))
            throw new InvalidUserInfoException(FIRST_NAME_SIZE_ERROR_MESSAGE);

        if (isLastNameSizeInvalid(user.getLastName()))
            throw new InvalidUserInfoException(LAST_NAME_SIZE_ERROR_MESSAGE);
    }

    private boolean isUsernameSizeInvalid(String username) {
        return username.length() < User.MIN_USERNAME_SIZE || username.length() > User.MAX_USERNAME_SIZE;
    }

    private boolean isFirstNameSizeInvalid(String firstName) {
        // First name is nullable so we let it slide if its null
        if (firstName == null)
            return false;

        return firstName.length() < User.MIN_FIRST_NAME_SIZE || firstName.length() > User.MAX_FIRST_NAME_SIZE;
    }

    private boolean isLastNameSizeInvalid(String lastName) {
        // Last name is nullable so we let it slide if its null
        if (lastName == null)
            return false;

        return lastName.length() < User.MIN_LAST_NAME_SIZE || lastName.length() > User.MAX_LAST_NAME_SIZE;
    }

    private boolean isPasswordPlain(String password) {
        return !BCRYPT_PATTERN.matcher(password).matches();
    }

    private String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

}
