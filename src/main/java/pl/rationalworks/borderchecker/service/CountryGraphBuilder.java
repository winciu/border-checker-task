package pl.rationalworks.borderchecker.service;

import org.springframework.stereotype.Component;
import pl.rationalworks.borderchecker.model.Country;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CountryGraphBuilder {

    public CountryGraph buildGraph(List<Country> countries) {
        CountryGraph graph = new CountryGraph();
        countries.forEach(c -> {
            graph.addVertex(c.getCca3(), c);
        });
        countries.forEach(c -> {
            Optional<CountryGraph.Vertex> v1 = graph.getVertex(c.getCca3());
            // v1 should always be present since was already added in the loop above
            v1.ifPresent(vertex -> {
                Arrays.stream(c.getBorders()).forEach(borderLabel -> {
                    Optional<CountryGraph.Vertex> v2 = graph.getVertex(borderLabel);
                    v2.ifPresentOrElse(v -> graph.addEdge(v1.get(), v), () -> {
                        //this is an adjacent vertex (a neighbour) but country definition for that label has not been loaded/found yet
                        CountryGraph.Vertex addedVertex = graph.addVertex(borderLabel, null);
                        graph.addEdge(v1.get(), addedVertex);
                    });
                });
            });
        });
        return graph;
    }

}
