package pl.rationalworks.borderchecker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Country {
    @ToString.Exclude
    private CountryName name;
    @NonNull
    private String cca3;
    @ToString.Exclude
    private String[] borders;

    public Country(@NonNull String cca3, String[] borders) {
        this.cca3 = cca3;
        this.borders = borders;
    }
}
