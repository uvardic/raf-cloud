package njp.raf.cloud.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EntityScan("njp.raf.cloud")
@EnableJpaRepositories("njp.raf.cloud")
public class DatabaseConfiguration {
}