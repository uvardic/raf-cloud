package njp.raf.cloud.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class TokenRequest {

    @NotNull
    @NotBlank
    private final String username;

    @NotNull
    @NotBlank
    private final String password;

}
