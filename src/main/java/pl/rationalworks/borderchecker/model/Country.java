package pl.rationalworks.borderchecker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Country {
    private CountryName name;
    private String cca3;
    private String[] borders;
}
