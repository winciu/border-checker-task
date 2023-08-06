package pl.rationalworks.borderchecker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.rationalworks.borderchecker.model.Country;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.util.Arrays.array;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CountryGraphTest {

    @Test
    void shouldAllowAddingVertexWithoutAssociatedCountry() {
        CountryGraph cg = new CountryGraph();
        assertThat(cg.vertices(), empty());
        cg.addVertex("v1", null);
        cg.addVertex("v2", null);
        assertThat(cg.vertices(), hasSize(2));
    }

    @Test
    void shouldReplaceAnAssociatedCountryInVertexIfTheLabelIsSameWhileAdding() {
        CountryGraph cg = new CountryGraph();
        assertThat(cg.vertices(), empty());
        cg.addVertex("v1", null);
        assertThat(cg.vertices(), hasSize(1));

        cg.addVertex("v1", new Country("v1"));
        assertThat(cg.vertices(), hasSize(1));
        Optional<CountryGraph.Vertex> vertex = cg.getVertex("v1");
        assertThat(vertex.isPresent(), is(true));
        assertThat(vertex.get().getCountry(), is(notNullValue()));
    }

    private static Stream<Arguments> shouldFindTheCorrectPathInAGivenGraph() {
        CountryGraphBuilder builder = new CountryGraphBuilder();
        CountryGraph graph1 = builder.buildGraph(List.of(
                new Country("v1", array("v2", "v3"))
        ));
        CountryGraph graph2 = builder.buildGraph(List.of(
                new Country("v1", array("v2", "v3")),
                new Country("v2", array("v4", "v5"))
        ));
        CountryGraph graph3 = builder.buildGraph(List.of(
                new Country("v1", array("v2", "v3")),
                new Country("v2", array("v4", "v5")),
                new Country("v3", array("v1", "v2"))
        ));
        CountryGraph graph4 = builder.buildGraph(List.of(
                new Country("ITA", array("AUT", "FRA", "SMR", "SVN", "CHE", "VAT")),
                new Country("CZE", array("AUT", "DEU", "POL", "SVK")),
                new Country("AUT", array("CZE", "DEU", "HUN", "ITA", "LIE", "SVK", "SVN", "CHE"))
        ));
        return Stream.of(
                Arguments.of(graph1, "v1", "v2", new String[]{"v1", "v2"}),
                Arguments.of(graph2, "v1", "v4", new String[]{"v1", "v2", "v4"}),
                Arguments.of(graph3, "v1", "v4", new String[]{"v1", "v2", "v4"}),
                Arguments.of(graph4, "CZE", "ITA", new String[]{"CZE", "AUT", "ITA"})
        );
    }

    @ParameterizedTest
    @MethodSource
    void shouldFindTheCorrectPathInAGivenGraph(CountryGraph graph, String labelFrom, String labelTo, String[] expectedPath) {
        CountryGraph.Route path = graph.findShortestPath(labelFrom, labelTo);
        assertThat(path.asListOfLabels(), arrayContaining(expectedPath));
    }
}
