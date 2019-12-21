package njp.raf.cloud.machine.service;

import njp.raf.cloud.exception.machine.MachineNotFoundException;
import njp.raf.cloud.exception.user.UserNotFoundException;
import njp.raf.cloud.machine.domain.Machine;
import njp.raf.cloud.machine.domain.MachineStatus;
import njp.raf.cloud.machine.repository.MachineRepository;
import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class MachinePreparationService {

    private final MachineRepository machineRepository;

    private final UserRepository userRepository;

    MachinePreparationService(MachineRepository machineRepository, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
    }

    public Machine prepareMachineForDelete(Long existingId) {
        Machine machine = machineRepository.findById(existingId)
                .orElseThrow(() -> new MachineNotFoundException(
                        String.format("Machine with id: %d wasn't found!", existingId)
                ));

        machine.setActive(false);

        return machine;
    }

    public Machine prepareMachineForSave(Machine machineRequest, Long ownerId) {
        machineRequest.setUuid(generateUuid());
        machineRequest.setStatus(MachineStatus.STOPPED);
        machineRequest.setOwner(findOwner(ownerId));
        machineRequest.setActive(true);

        return machineRequest;
    }

    private String generateUuid() {
        String uuid = UUID.randomUUID().toString();

        while (uuidExists(uuid))
            uuid = UUID.randomUUID().toString();

        return uuid;
    }

    private boolean uuidExists(String uuid) {
        return machineRepository.findByUuid(uuid).isPresent();
    }

    private User findOwner(Long ownerId) {
        return userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id: %d was't found!", ownerId)
                ));
    }

    public Machine prepareMachineForUpdate(Long existingId, Machine machineRequest) {
        Machine machine = findMachine(existingId);

        machine.setStatus(machineRequest.getStatus());
        machine.setName(machineRequest.getName());

        return machine;
    }

    private Machine findMachine(Long existingId) {
        return machineRepository.findById(existingId)
                .orElseThrow(() -> new MachineNotFoundException(
                        String.format("Machine with id: %d wasn't found", existingId)
                ));
    }

}
