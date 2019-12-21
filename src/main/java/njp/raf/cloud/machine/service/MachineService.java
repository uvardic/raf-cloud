package njp.raf.cloud.machine.service;

import njp.raf.cloud.exception.machine.MachineNotFoundException;
import njp.raf.cloud.machine.domain.Machine;
import njp.raf.cloud.machine.repository.MachineRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    private final MachinePreparationService machinePreparationService;

    public MachineService(MachineRepository machineRepository, MachinePreparationService machinePreparationService) {
        this.machineRepository = machineRepository;
        this.machinePreparationService = machinePreparationService;
    }

    @Transactional
    public void deleteById(Long existingId) {
        machineRepository.save(machinePreparationService.prepareMachineForDelete(existingId));
    }

    @Transactional
    public Machine save(Machine machineRequest, Long ownerId) {
        return machineRepository.save(machinePreparationService.prepareMachineForSave(machineRequest, ownerId));
    }

    @Transactional
    public Machine update(Long existingId, Machine machineRequest) {
        return machineRepository.save(machinePreparationService.prepareMachineForUpdate(existingId, machineRequest));
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

}