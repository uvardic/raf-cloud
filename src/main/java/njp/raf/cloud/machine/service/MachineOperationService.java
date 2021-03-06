package njp.raf.cloud.machine.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import njp.raf.cloud.exception.async.InvalidMachineStateAsyncException;
import njp.raf.cloud.exception.async.MachineOperationInProgressAsyncException;
import njp.raf.cloud.exception.machine.MachineNotFoundException;
import njp.raf.cloud.machine.domain.Machine;
import njp.raf.cloud.machine.domain.MachineStatus;
import njp.raf.cloud.machine.repository.MachineRepository;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class MachineOperationService {

    private static final long MIN_OPERATION_TIME = 10000L;

    private static final long MAX_OPERATION_TIME = 15000L;

    private static final long LOCK_TRY_TIMEOUT = 100L;

    private final LockRegistry lockRegistry;

    private final MachineRepository machineRepository;

    @Async
    public void lockedOperationFor(Long existingId, Consumer<Long> operation) {
        Lock lock = lockRegistry.obtain(Long.toString(existingId));

        if (lockAcquired(lock)) {
            try {
                operation.accept(existingId);
            } finally {
                lock.unlock();
            }
        } else throw new MachineOperationInProgressAsyncException("Another machine operation in progress!");
    }

    @Async
    @SneakyThrows
    public void start(Long existingId) {
        if (machineNotActive(existingId))
            throw new InvalidMachineStateAsyncException("Machine needs to be active!");

        if (machineNotStopped(existingId))
            throw new InvalidMachineStateAsyncException("Machine needs to be stopped first!");

        Lock lock = lockRegistry.obtain(Long.toString(existingId));

        if (lockAcquired(lock)) {
            try {
                Thread.sleep(calculateMachineOperationTime());
                machineRepository.updateStatus(existingId, MachineStatus.RUNNING);
            } finally {
                lock.unlock();
            }
        } else throw new MachineOperationInProgressAsyncException("Another machine operation in progress!");
    }

    @Async
    @SneakyThrows
    public void stop(Long existingId) {
        if (machineNotActive(existingId))
            throw new InvalidMachineStateAsyncException("Machine needs to be active!");

        if (machineNotRunning(existingId))
            throw new InvalidMachineStateAsyncException("Machine needs to be running first!");

        Lock lock = lockRegistry.obtain(Long.toString(existingId));

        if (lockAcquired(lock)) {
            try {
                Thread.sleep(calculateMachineOperationTime());
                machineRepository.updateStatus(existingId, MachineStatus.STOPPED);
            } finally {
                lock.unlock();
            }
        } else throw new MachineOperationInProgressAsyncException("Another machine operation in progress!");
    }

    @Async
    @SneakyThrows
    public void restart(Long existingId) {
        if (machineNotActive(existingId))
            throw new InvalidMachineStateAsyncException("Machine needs to be active!");

        if (machineNotRunning(existingId))
            throw new InvalidMachineStateAsyncException("Machine needs to be running first!");

        Lock lock = lockRegistry.obtain(Long.toString(existingId));

        if (lockAcquired(lock)) {
            try {
                long operationTime = calculateMachineOperationTime();

                Thread.sleep(operationTime / 2);
                machineRepository.updateStatus(existingId, MachineStatus.STOPPED);
                Thread.sleep(operationTime / 2);
                machineRepository.updateStatus(existingId, MachineStatus.RUNNING);
            } finally {
                lock.unlock();
            }
        } else throw new MachineOperationInProgressAsyncException("Another machine operation in progress!");
    }

    @SneakyThrows
    private boolean lockAcquired(Lock lock) {
        return lock.tryLock(LOCK_TRY_TIMEOUT, TimeUnit.MICROSECONDS);
    }

    private boolean machineNotRunning(Long existingId) {
        return !findMachine(existingId).getStatus().equals(MachineStatus.RUNNING);
    }

    private boolean machineNotStopped(Long existingId) {
        return !findMachine(existingId).getStatus().equals(MachineStatus.STOPPED);
    }

    private boolean machineNotActive(Long existingId) {
        return !findMachine(existingId).isActive();
    }

    private Machine findMachine(Long existingId) {
        return machineRepository.findById(existingId)
                .orElseThrow(() -> new MachineNotFoundException(
                        String.format("Machine with id: %d wasn't found!", existingId)
                ));
    }

    private long calculateMachineOperationTime() {
        return ThreadLocalRandom.current().nextLong(MIN_OPERATION_TIME, MAX_OPERATION_TIME);
    }

}
