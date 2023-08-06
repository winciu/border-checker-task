package pl.rationalworks.borderchecker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rationalworks.borderchecker.model.Country;
import pl.rationalworks.borderchecker.model.CountryRoute;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutingService {

    private final DataLoader dataLoader;
    private final CountryGraphBuilder graphBuilder;

    public CountryRoute findShortestRoute(String origin, String destination) throws IOException {
        List<Country> countries = dataLoader.loadData();
        CountryGraph countryGraph = graphBuilder.buildGraph(countries);
        CountryGraph.Route shortestPath = countryGraph.findShortestPath(origin, destination);
        return new CountryRoute(shortestPath.asListOfLabels());
    }
}
