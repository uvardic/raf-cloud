package njp.raf.cloud.user.rest;

import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.service.UserService;
import njp.raf.cloud.util.RestUtilities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cloud/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/delete/id={id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return RestUtilities.createErrorMap(bindingResult);

        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @PutMapping("/update/existingId={existingId}")
    public ResponseEntity<?> update(
            @PathVariable Long existingId, @Valid @RequestBody User user, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors())
            return RestUtilities.createErrorMap(bindingResult);

        return new ResponseEntity<>(userService.update(existingId, user), HttpStatus.OK);
    }

    @GetMapping("/id={id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/username={username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

}
