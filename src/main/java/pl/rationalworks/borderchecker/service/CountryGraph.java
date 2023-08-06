package pl.rationalworks.borderchecker.service;

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
    public Vertex addVertex(String label, Country country) {
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

    public Route findShortestPath(String labelFrom, String labelTo) {
        Optional<Vertex> vertexFrom = getVertex(labelFrom);
        Optional<Vertex> vertexTo = getVertex(labelTo);
        if (vertexFrom.isPresent() && vertexTo.isPresent()) {
            return findShortestPath(vertexFrom.get(), vertexTo.get());
        }
        return Route.EMPTY;
    }

    private Route findShortestPath(Vertex from, Vertex to) {
        Set<Vertex> visited = new HashSet<>();
        Queue<Vertex> queue = new LinkedList<>();
        Map<Vertex, Vertex> previousNodes = new HashMap<>(); // map to keep track of previous node of the given node
        queue.add(from);
        visited.add(from);
        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();
            for (Vertex neighbour : adjVertices(vertex)) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.add(neighbour);
                    previousNodes.put(neighbour, vertex);
                }
            }
        }
        // now (based on the previous nodes) we need to reconstruct the path going backwards from destination vertex
        Route route = new Route();
        for (Vertex v = to; v != null; v = previousNodes.get(v)){
            route.add(v);
        }
        route.reverse();

        // check if there is a connection between given vertices (origin and destination). This might not be true if
        // the graph is disjoint.
        if (route.first().equals(from)) {
            return route;
        }
        return Route.EMPTY;
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
        private final List<Vertex> route;

        public Route() {
            route = new ArrayList<>();
        }

        public void add(Vertex vertex) {
            route.add(vertex);
        }

        public String[] asListOfLabels() {
            return route.stream().map(Vertex::getLabel).toArray(String[]::new);
        }

        public void reverse() {
            Collections.reverse(this.route);
        }

        public Vertex first() {
            return route.get(0);
        }
    }
}
