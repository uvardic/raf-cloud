package njp.raf.cloud.machine.rest;

import njp.raf.cloud.annotation.AuthorizationRole;
import njp.raf.cloud.machine.domain.Machine;
import njp.raf.cloud.machine.dto.MachineSearchRequest;
import njp.raf.cloud.machine.service.MachineService;
import njp.raf.cloud.user.domain.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@SuppressWarnings("unused")
@RequestMapping("cloud/machine")
public class MachineRestController {

    private final MachineService machineService;

    public MachineRestController(MachineService machineService) {
        this.machineService = machineService;
    }

    @DeleteMapping("/delete/existingId={existingId}")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<?> deleteById(
            @PathVariable Long existingId, @RequestHeader("authorization") String authorizationHeader
    ) {
        machineService.deleteById(existingId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/save/ownerId={ownerId}")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<?> save(
            @Valid @RequestBody Machine machineRequest, @PathVariable Long ownerId,
            @RequestHeader("authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(machineService.save(machineRequest, ownerId), HttpStatus.CREATED);
    }

    @PutMapping("/update/existingId={existingId}")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<?> update(
            @PathVariable Long existingId, @Valid @RequestBody Machine machineRequest,
            @RequestHeader("authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(machineService.update(existingId, machineRequest), HttpStatus.OK);
    }

    @GetMapping("/id={id}")
    @AuthorizationRole(roles = {UserRole.ADMIN})
    public ResponseEntity<Machine> findById(
            @PathVariable Long id, @RequestHeader("authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(machineService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<List<Machine>> findAll(@RequestHeader("authorization") String authorizationHeader) {
        return new ResponseEntity<>(machineService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/allByOwner")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<List<Machine>> findAllByOwner(
            @RequestBody MachineSearchRequest machineSearchRequest,
            @RequestHeader("authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(machineService.findAllByOwner(machineSearchRequest), HttpStatus.OK);
    }

    @GetMapping("/allByOwnerAndName")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<List<Machine>> findAllByOwnerAndName(
            @RequestBody MachineSearchRequest machineSearchRequest,
            @RequestHeader("authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(machineService.findAllByOwnerAndName(machineSearchRequest), HttpStatus.OK);
    }

    @GetMapping("/allByOwnerAndStatusIn")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<List<Machine>> findAllByOwnerAndStatusIn(
            @RequestBody MachineSearchRequest machineSearchRequest,
            @RequestHeader("authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(machineService.findAllByOwnerAndStatusIn(machineSearchRequest), HttpStatus.OK);
    }

    @GetMapping("/allByOwnerAndBetweenDate")
    @AuthorizationRole(roles = {UserRole.ADMIN, UserRole.USER})
    public ResponseEntity<List<Machine>> findAllByOwnerAndBetweenDate(
            @RequestBody MachineSearchRequest machineSearchRequest,
            @RequestHeader("authorization") String authorizationHeader
    ) {
        return new ResponseEntity<>(machineService.findAllByOwnerAndBetweenDate(machineSearchRequest), HttpStatus.OK);
    }

}
