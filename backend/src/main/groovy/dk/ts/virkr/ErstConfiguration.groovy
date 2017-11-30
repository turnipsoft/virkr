package dk.ts.virkr

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * @author Rune Molin, rmo@nine.dk
 */
@Profile("ERST")
@Configuration
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration, HibernateJpaAutoConfiguration, DataSourceTransactionManagerAutoConfiguration])
@ComponentScan(basePackages = ["dk.ts.virkr"])
class ErstConfiguration {
}
