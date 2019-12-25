package njp.raf.cloud.user.rest;

import lombok.RequiredArgsConstructor;
import njp.raf.cloud.annotation.AuthorizationRole;
import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.domain.UserRole;
import njp.raf.cloud.user.dto.TokenRequest;
import njp.raf.cloud.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
@RequestMapping("/cloud/user")
public class UserRestController {

    private final UserService userService;

    @DeleteMapping("/delete/existingId={existingId}")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<?> delete(
            @PathVariable Long existingId, @RequestHeader("Authorization") String authorizationHeader
    ) {
        userService.deleteById(existingId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody User userRequest) {
        return new ResponseEntity<>(userService.save(userRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update/existingId={existingId}")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<?> update(
            @PathVariable Long existingId, @Valid @RequestBody User user,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(userService.update(existingId, user), HttpStatus.OK);
    }

    @GetMapping("/id={id}")
    @AuthorizationRole(roles = {UserRole.ADMIN})
    public ResponseEntity<User> findById(
            @PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/username={username}")
    @AuthorizationRole(roles = {UserRole.ADMIN})
    public ResponseEntity<User> findByUsername(
            @PathVariable String username, @RequestHeader("Authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    @AuthorizationRole(roles = {UserRole.ADMIN})
    public ResponseEntity<List<User>> findAll(@RequestHeader("Authorization") String authorizationHeader) {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody TokenRequest tokenRequest) {
        return new ResponseEntity<>(userService.login(tokenRequest), HttpStatus.OK);
    }

}
