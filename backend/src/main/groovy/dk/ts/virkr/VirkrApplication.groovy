package dk.ts.virkr

import org.springframework.boot.SpringApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Created by sorenhartvig on 19/05/2017.
 */
@Configuration
@ComponentScan
@EnableScheduling
class VirkrApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(VirkrApplication.class);
  }

  static void main(String[] args) {
    SpringApplication.run(VirkrApplication.class, args)
  }
}
