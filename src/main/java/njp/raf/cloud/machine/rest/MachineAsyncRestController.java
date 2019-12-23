package njp.raf.cloud.machine.rest;

import lombok.RequiredArgsConstructor;
import njp.raf.cloud.machine.service.MachineOperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("cloud/machine/async")
public class MachineAsyncRestController {

    private final MachineOperationService machineOperationService;

    @GetMapping("/start/existingId={existingId}")
    public ResponseEntity<?> start(
            @PathVariable Long existingId
    ) {
        machineOperationService.start(existingId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/stop/existingId={existingId}")
    public ResponseEntity<?> stop(
            @PathVariable Long existingId
    ) {
        machineOperationService.stop(existingId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
