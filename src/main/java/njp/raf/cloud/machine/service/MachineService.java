package njp.raf.cloud.machine.service;

import lombok.RequiredArgsConstructor;
import njp.raf.cloud.exception.machine.InvalidMachineSearchRequestException;
import njp.raf.cloud.exception.machine.MachineNotFoundException;
import njp.raf.cloud.exception.user.UserNotFoundException;
import njp.raf.cloud.machine.domain.Machine;
import njp.raf.cloud.machine.domain.MachineStatus;
import njp.raf.cloud.machine.dto.MachineSearchRequest;
import njp.raf.cloud.machine.repository.MachineRepository;
import njp.raf.cloud.user.domain.User;
import njp.raf.cloud.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository machineRepository;

    private final UserRepository userRepository;

    @Transactional
    public void deleteById(Long existingId) {
        Machine machine = findById(existingId);

        machine.setActive(false);
        machine.setDateToToCurrentDate();

        machineRepository.save(machine);
    }

    @Transactional
    public Machine save(Machine machineRequest, Long ownerId) {
        machineRequest.setUuid(generateUuid());
        machineRequest.setStatus(MachineStatus.STOPPED);
        machineRequest.setOwner(findOwner(ownerId));
        machineRequest.setDateFromToCurrentDate();
        machineRequest.setActive(true);

        return machineRepository.save(machineRequest);
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

    @Transactional
    public Machine update(Long existingId, Machine machineRequest) {
        Machine machine = findById(existingId);

        machine.setStatus(machineRequest.getStatus());
        machine.setName(machineRequest.getName());

        return machineRepository.save(machine);
    }

    public Machine findById(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() -> new MachineNotFoundException(
                        String.format("Machine with id: %d wasn't found!", id)
                ));
    }

    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    public List<Machine> findAllByOwner(MachineSearchRequest machineSearchRequest) {
        if (machineSearchRequest.getOwnerId() == null)
            throw new InvalidMachineSearchRequestException("Owner id not defined!");

        return machineRepository.findAllByOwner(machineSearchRequest.getOwnerId());
    }

    public List<Machine> findAllByOwnerAndName(MachineSearchRequest machineSearchRequest) {
        if (machineSearchRequest.getOwnerId() == null)
            throw new InvalidMachineSearchRequestException("Owner id not defined!");

        if (machineSearchRequest.getName() == null)
            throw new InvalidMachineSearchRequestException("Name not defined!");

        return machineRepository.findAllByOwnerAndName(
                machineSearchRequest.getOwnerId(), machineSearchRequest.getName()
        );
    }

    public List<Machine> findAllByOwnerAndStatusIn(MachineSearchRequest machineSearchRequest) {
        if (machineSearchRequest.getOwnerId() == null)
            throw new InvalidMachineSearchRequestException("Owner id not defined!");

        if (machineSearchRequest.getStatuses() == null)
            throw new InvalidMachineSearchRequestException("Statuses not defined!");

        return machineRepository.findAllByOwnerAndStatusIn(
                machineSearchRequest.getOwnerId(), machineSearchRequest.getStatuses()
        );
    }

    public List<Machine> findAllByOwnerAndBetweenDate(MachineSearchRequest machineSearchRequest) {
        if (machineSearchRequest.getOwnerId() == null)
            throw new InvalidMachineSearchRequestException("Owner id not defined!");

        if (machineSearchRequest.getDate() == null)
            throw new InvalidMachineSearchRequestException("Date not defined!");

        return machineRepository.findAllByOwnerAndBetweenDate(
                machineSearchRequest.getOwnerId(), machineSearchRequest.getDateAsObject()
        );
    }

}
