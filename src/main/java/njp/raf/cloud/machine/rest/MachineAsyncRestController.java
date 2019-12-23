package njp.raf.cloud.machine.rest;

import lombok.RequiredArgsConstructor;
import njp.raf.cloud.annotation.AuthorizationRole;
import njp.raf.cloud.machine.service.MachineOperationService;
import njp.raf.cloud.user.domain.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
@RequestMapping("cloud/machine/async")
public class MachineAsyncRestController {

    private final MachineOperationService machineOperationService;

    @PutMapping("/start/existingId={existingId}")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<?> start(
            @PathVariable Long existingId,
            @RequestHeader("authorization") String authorizationHeader
    ) {
        machineOperationService.start(existingId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/stop/existingId={existingId}")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<?> stop(
            @PathVariable Long existingId,
            @RequestHeader("authorization") String authorizationHeader
    ) {
        machineOperationService.stop(existingId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/restart/existingId={existingId}")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<?> restart(
            @PathVariable Long existingId,
            @RequestHeader("authorization") String authorizationHeader
    ) {
        machineOperationService.restart(existingId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
