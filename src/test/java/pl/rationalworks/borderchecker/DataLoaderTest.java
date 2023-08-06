package pl.rationalworks.borderchecker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.rationalworks.borderchecker.model.Country;
import pl.rationalworks.borderchecker.service.DataLoader;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class DataLoaderTest {

    @Autowired
    private DataLoader dataLoader;

    @Test
    public void shouldLoadInputDataProperly() throws IOException {
        List<Country> countries = dataLoader.loadData();
        assertThat(countries, hasSize(5));
        Set<String> names = countries.stream().map(c->c.getName().getCommon()).collect(Collectors.toSet());
        assertThat("Afghanistan", is(in(names)));
    }
}
