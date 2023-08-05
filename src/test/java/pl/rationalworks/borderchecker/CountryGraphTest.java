package pl.rationalworks.borderchecker;

import org.junit.jupiter.api.Test;
import pl.rationalworks.borderchecker.model.Country;

import java.util.List;
import java.util.Optional;

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

    @Test
    void shouldFindTheCorrectPathInAGivenGraph() {
        CountryGraphBuilder builder = new CountryGraphBuilder();
        CountryGraph countryGraph = builder.buildGraph(List.of(
                new Country("v1", array("v2", "v3"))
        ));
        CountryGraph.Route path = countryGraph.findPath("v1", "v2");
        assertThat(path.asListOfLabels(), arrayContaining("v1", "v2"));
    }
}
