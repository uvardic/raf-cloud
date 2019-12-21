package njp.raf.cloud.machine.repository;

import njp.raf.cloud.machine.domain.Machine;
import njp.raf.cloud.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    Optional<Machine> findByUuid(String uid);

    List<Machine> findAllByActiveTrue();

    List<Machine> findAllByActiveTrueAndName(String name);

    List<Machine> findAllByActiveTrueAndOwner(User owner);

}
