package pl.rationalworks.borderchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("pl.rationalworks.borderchecker.properties")
public class BorderCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BorderCheckerApplication.class, args);
	}

}
