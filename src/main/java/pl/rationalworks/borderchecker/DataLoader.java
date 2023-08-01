package pl.rationalworks.borderchecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.rationalworks.borderchecker.model.Country;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {
    public final ObjectMapper objectMapper;
    private boolean dataLoaded;
    private List<Country> countries;

    public List<Country> loadData(Path path) throws IOException {
        if (dataLoaded) {
            return countries;
        }
        CollectionType countriesType = objectMapper.getTypeFactory().constructCollectionType(List.class, Country.class);
        countries = objectMapper.readValue(path.toFile(), countriesType);
        dataLoaded = true;
        return countries;
    }

}
