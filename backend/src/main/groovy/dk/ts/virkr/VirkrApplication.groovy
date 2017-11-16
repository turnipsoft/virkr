package dk.ts.virkr

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Created by sorenhartvig on 19/05/2017.
 */
@SpringBootApplication
@EnableScheduling
class VirkrApplication {

    static void main(String []args) {
        SpringApplication.run(VirkrApplication.class, args)
    }
}
