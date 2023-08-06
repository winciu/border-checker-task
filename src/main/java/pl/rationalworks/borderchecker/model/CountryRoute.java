package pl.rationalworks.borderchecker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.List;

@Getter
public class CountryRoute {

    private final List<String> route;

    public CountryRoute(String[] countryCodes) {
        route = List.of(countryCodes);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return route.isEmpty();
    }
}
