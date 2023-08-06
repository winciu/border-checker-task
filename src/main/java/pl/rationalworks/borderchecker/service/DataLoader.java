package pl.rationalworks.borderchecker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.rationalworks.borderchecker.model.Country;
import pl.rationalworks.borderchecker.properties.DataLoaderProperties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

    private final ObjectMapper objectMapper;
    private final DataLoaderProperties properties;
    private final RestTemplate restTemplate;
    private boolean dataLoaded;
    private List<Country> countries;

    public List<Country> loadData() throws IOException {
        if (dataLoaded) {
            log.trace("Data already loaded");
            return countries;
        }
        switch (properties.getMode()) {
            case "local" -> loadData(properties.getSourceLocal());
            case "remote" -> loadData(properties.getSourceRemote());
            default -> throw new IllegalArgumentException("Unknown data source mode: " + properties.getMode());
        }

        dataLoaded = true;
        return countries;
    }

    private void loadData(URI sourceRemote) {
        log.info("Data loaded from remote source {}", sourceRemote);
        ResponseEntity<Country[]> response = restTemplate.getForEntity(sourceRemote, Country[].class);
        Country[] body = response.getBody();
        if (Objects.isNull(body)) {
            return;
        }
        countries = Arrays.stream(body).toList();
    }

    private void loadData(String dataFilePath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(dataFilePath)) {
            CollectionType countriesType = objectMapper.getTypeFactory().constructCollectionType(List.class, Country.class);
            countries = objectMapper.readValue(inputStream, countriesType);
        }
    }

}
