package pl.rationalworks.borderchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.rationalworks.borderchecker.model.Country;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringConfiguration.class })
public class DataLoaderTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Value("classpath:countriesTest.json")
    private Path inputDataFile;

    @Test
    public void shouldLoadInputData() throws IOException {
        DataLoader dataLoader = new DataLoader(objectMapper);
        List<Country> countries = dataLoader.loadData(inputDataFile);
        assertThat(countries, hasSize(2));
        Set<String> names = countries.stream().map(c->c.getName().getCommon()).collect(Collectors.toSet());
        assertThat("Afghanistan", is(in(names)));
    }
}
