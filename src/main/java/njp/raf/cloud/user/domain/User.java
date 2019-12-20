package njp.raf.cloud.user.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true)
    @Size(min = MIN_USERNAME_SIZE, max = MAX_USERNAME_SIZE)
    private String username;

    @NotNull
    private String password;

    @Size(min = MIN_FIRST_NAME_SIZE, max = MAX_FIRST_NAME_SIZE)
    private String firstName;

    @Size(min = MIN_LAST_NAME_SIZE, max = MAX_LAST_NAME_SIZE)
    private String lastName;

    public static final int MIN_USERNAME_SIZE = 5;

    public static final int MAX_USERNAME_SIZE = 100;

    public static final int MIN_FIRST_NAME_SIZE = 2;

    public static final int MAX_FIRST_NAME_SIZE = 100;

    public static final int MIN_LAST_NAME_SIZE = 2;

    public static final int MAX_LAST_NAME_SIZE = 100;

}
