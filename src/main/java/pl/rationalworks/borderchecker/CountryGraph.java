package pl.rationalworks.borderchecker;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.rationalworks.borderchecker.model.Country;

import java.util.*;

import static java.util.Collections.unmodifiableSet;

public class CountryGraph {
    private final Map<Vertex, Set<Vertex>> adjacencyMap;

    public CountryGraph() {
        adjacencyMap = new HashMap<>();
    }

    /**
     * @param label   label for that vertex
     * @param country associated {@link Country} instance
     * @return a new added vertex or an updated one if already present with the same label
     */
    Vertex addVertex(String label, Country country) {
        Vertex vertex = new Vertex(label, country);
        // if vertex with a given label is already present we need to update the associated country value (could be a null before)
        Optional<Vertex> v = getVertex(label);
        v.ifPresent(vertex1 -> vertex1.country = country);
        adjacencyMap.putIfAbsent(vertex, new HashSet<>());
        return vertex;
    }

    CountryGraph addEdge(Vertex v1, Vertex v2) {
        adjacencyMap.get(v1).add(v2);
        adjacencyMap.get(v2).add(v1);
        return this;
    }

    public Optional<Vertex> getVertex(String label) {
        return adjacencyMap.keySet().stream()
                .filter(v -> v.label.equals(label))
                .findFirst();
    }

    public Route findPath(String labelFrom, String labelTo) {
        Optional<Vertex> vertexFrom = getVertex(labelFrom);
        Optional<Vertex> vertexTo = getVertex(labelTo);
        if (vertexFrom.isPresent() && vertexTo.isPresent()) {
            return findPath(vertexFrom.get(), vertexTo.get());
        }
        return Route.EMPTY;
    }

    /**
     * Searches this graph using depth first traversal
     *
     * @param from {@link Vertex} instance to start from
     * @param to   destination {@link Vertex} instance
     * @return Set of vertices as a route between given vertices
     */
    private Route findPath(Vertex from, Vertex to) {
        Set<Vertex> visited = new LinkedHashSet<>();
        Route path = new Route();
        Stack<Vertex> stack = new Stack<>();
        stack.push(from);
        while (!stack.isEmpty()) {
            Vertex vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                Set<Vertex> adjVertices = adjVertices(vertex);
                if (vertex.equals(to)) {
                    path.add(vertex);
                    return path;
                }
                if (adjVertices.size() > 1) {
                    path.add(vertex);
                }

                for (Vertex v : adjVertices) {
                    stack.push(v);
                }
            }
        }
        return path;
    }

    public Set<Vertex> vertices() {
        return unmodifiableSet(adjacencyMap.keySet());
    }

    public Set<Vertex> adjVertices(Vertex vertex) {
        return unmodifiableSet(adjacencyMap.get(vertex));
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @ToString
    @Getter
    public static class Vertex {
        @EqualsAndHashCode.Include
        private final String label;
        @ToString.Exclude
        private Country country;

        public Vertex(String label, Country country) {
            this.label = label;
            this.country = country;
        }

        public Vertex(String label) {
            this.label = label;
            this.country = null;
        }
    }

    public static class Route {
        public static final Route EMPTY = new Route();
        private final Set<Vertex> route;

        public Route() {
            route = new LinkedHashSet<>();
        }

        public void add(Vertex vertex) {
            route.add(vertex);
        }

        public String[] asListOfLabels() {
            return route.stream().map(Vertex::getLabel).toArray(String[]::new);
        }
    }
}
