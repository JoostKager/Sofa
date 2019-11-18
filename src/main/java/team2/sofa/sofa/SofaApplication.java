package team2.sofa.sofa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SofaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SofaApplication.class, args);
    }

    @Bean
   public RestTemplate getRestTemplate() {
      return new RestTemplate();
   }

}
