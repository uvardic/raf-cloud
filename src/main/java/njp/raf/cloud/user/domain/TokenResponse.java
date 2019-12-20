package njp.raf.cloud.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class TokenResponse {

    @NotNull
    @NotBlank
    private final String token;

}
