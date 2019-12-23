package njp.raf.cloud.machine.repository;

import njp.raf.cloud.machine.domain.Machine;
import njp.raf.cloud.machine.domain.MachineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Machine SET status = :statusRequest WHERE id = :existingId")
    void updateStatus(Long existingId, MachineStatus statusRequest);

    Optional<Machine> findByUuid(String uid);

    @Query(value = "FROM Machine machine WHERE machine.owner.id = :ownerId")
    List<Machine> findAllByOwner(Long ownerId);

    @Query(value = "FROM Machine machine WHERE machine.owner.id = :ownerId AND machine.name = :name")
    List<Machine> findAllByOwnerAndName(Long ownerId, String name);

    @Query(value = "FROM Machine machine WHERE machine.owner.id = :ownerId AND machine.status IN :statuses")
    List<Machine> findAllByOwnerAndStatusIn(Long ownerId, Collection<@NotNull MachineStatus> statuses);

    @Query(value = "FROM Machine machine WHERE machine.owner.id = :ownerId " +
                    "AND :date BETWEEN machine.dateFrom AND machine.dateTo")
    List<Machine> findAllByOwnerAndBetweenDate(Long ownerId, Date date);

}
