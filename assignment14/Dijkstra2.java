import java.util.*;

public class Dijkstra2 {

    // --- Graph Structures (simplified for demonstration) ---

    // Represents a vertex in the graph
    static class Vertex {
        String id;
        int distance; // Stores d(v)
        // This simulates the "locator" l from the pseudocode.
        // In a practical Java implementation, if you use a standard PriorityQueue,
        // you often manage this by storing a reference to the element in the PQ
        // or by using a Map to find the PQ entry.
        PQEntry pqEntryRef; // Reference to the entry in the PriorityQueue

        public Vertex(String id) {
            this.id = id;
            this.distance = Integer.MAX_VALUE; // Initialize with "infinity"
        }

        @Override
        public String toString() {
            return id + " (Dist: " + (distance == Integer.MAX_VALUE ? "∞" : distance) + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return id.equals(vertex.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    // Represents an edge in the graph
    static class Edge {
        Vertex source;
        Vertex destination;
        int weight;

        public Edge(Vertex source, Vertex destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        // Helper to get the opposite vertex of an edge given one endpoint
        public Vertex opposite(Vertex v) {
            if (v.equals(source)) {
                return destination;
            } else if (v.equals(destination)) {
                return source;
            }
            return null; // Should not happen in a valid graph traversal
        }
    }

    // A simple graph representation using an adjacency list
    static class Graph {
        List<Vertex> vertices;
        Map<Vertex, List<Edge>> adjacencies;

        public Graph() {
            this.vertices = new ArrayList<>();
            this.adjacencies = new HashMap<>();
        }

        public void addVertex(Vertex v) {
            vertices.add(v);
            adjacencies.put(v, new ArrayList<>());
        }

        public void addEdge(Vertex u, Vertex v, int weight) {
            Edge edge = new Edge(u, v, weight);
            adjacencies.get(u).add(edge);
            adjacencies.get(v).add(edge); // For an undirected graph
        }

        public Collection<Vertex> getVertices() {
            return vertices;
        }

        public List<Edge> getIncidentEdges(Vertex v) {
            return adjacencies.getOrDefault(v, Collections.emptyList());
        }
    }

    // Entry for the PriorityQueue. Stores the vertex and its current distance.
    static class PQEntry implements Comparable<PQEntry> {
        Vertex vertex;
        int distance; // The key for the priority queue

        public PQEntry(Vertex vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(PQEntry other) {
            return Integer.compare(this.distance, other.distance);
        }

        // Important: equals and hashCode for proper removal from PriorityQueue
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PQEntry pqEntry = (PQEntry) o;
            return vertex.equals(pqEntry.vertex); // Equality based on the vertex
        }

        @Override
        public int hashCode() {
            return vertex.hashCode();
        }
    }

    // --- Dijkstra's Algorithm Implementation ---

    /**
     * Implements Dijkstra's Algorithm to find the shortest distances from a source vertex
     * in a graph.
     *
     * @param G The graph.
     * @param s The source vertex.
     * @return A map containing the shortest distance from 's' to each reachable vertex.
     */
    public static Map<Vertex, Integer> dijkstraDistances(Graph G, Vertex s) {
        // Q <- new heap-based priority queue
        // We use Java's PriorityQueue. To efficiently handle "replaceKey",
        // we also use a Map to keep track of the PQEntry object for each Vertex.
        PriorityQueue<PQEntry> Q = new PriorityQueue<>();
        Map<Vertex, PQEntry> vertexToPQEntryMap = new HashMap<>(); // This acts as the "locator" store

        // for all v ∈ G.vertices() do
        for (Vertex v : G.getVertices()) {
            // if v = s then
            if (v.equals(s)) {
                // setDistance(v, 0)
                v.distance = 0;
            } else {
                // setDistance(v, ∞)
                v.distance = Integer.MAX_VALUE; // Represent infinity
            }

            // l <- Q.insertItem(getDistance(v), v)
            // setLocator(v, l)
            PQEntry entry = new PQEntry(v, v.distance);
            Q.add(entry);
            v.pqEntryRef = entry; // Store reference to the PQ entry in the vertex
            vertexToPQEntryMap.put(v, entry); // Store entry in the locator map
        }

        // while !Q.isEmpty() do
        while (!Q.isEmpty()) {
            // u <- Q.removeMin()
            PQEntry minEntry = Q.poll();
            Vertex u = minEntry.vertex;

            // This is crucial: If we've already found a shorter path to 'u'
            // since this entry was added to the PQ (due to a 'replaceKey' operation
            // being simulated by re-adding entries), then this extracted entry is stale.
            if (u.distance < minEntry.distance) {
                continue; // Skip processing this outdated entry
            }

            // for all e ∈ G.incidentEdges(u) do
            for (Edge e : G.getIncidentEdges(u)) {
                // z <- G.opposite(u, e) { relax edge e }
                Vertex z = e.opposite(u);
                if (z == null) continue; // Safety check

                // r <- getDistance(u) + weight(e)
                int r = u.distance + e.weight;

                // if r < getDistance(z) then
                if (r < z.distance) {
                    // setDistance(z, r)
                    z.distance = r;

                    // Q.replaceKey(getLocator(z), r)
                    // Simulating replaceKey: remove the old entry and add a new one.
                    // This involves removing the old PQEntry object based on its identity.
                    PQEntry oldEntry = vertexToPQEntryMap.get(z);
                    if (oldEntry != null) {
                        Q.remove(oldEntry); // O(N) for standard PQ, but necessary
                    }
                    PQEntry newEntry = new PQEntry(z, z.distance);
                    Q.add(newEntry); // O(log N)
                    z.pqEntryRef = newEntry; // Update the reference
                    vertexToPQEntryMap.put(z, newEntry); // Update the locator map
                }
            }
        }

        // Collect the final distances
        Map<Vertex, Integer> shortestDistances = new HashMap<>();
        for (Vertex v : G.getVertices()) {
            shortestDistances.put(v, v.distance);
        }
        return shortestDistances;
    }

    // --- Main method for testing ---
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Create vertices
        Vertex A = new Vertex("A");
        Vertex B = new Vertex("B");
        Vertex C = new Vertex("C");
        Vertex D = new Vertex("D");
        Vertex E = new Vertex("E");
        Vertex F = new Vertex("F");

        // Add vertices to the graph
        graph.addVertex(A);
        graph.addVertex(B);
        graph.addVertex(C);
        graph.addVertex(D);
        graph.addVertex(E);
        graph.addVertex(F);

        // Add edges
        graph.addEdge(A, B, 7);
        graph.addEdge(A, C, 9);
        graph.addEdge(A, F, 14);
        graph.addEdge(B, C, 10);
        graph.addEdge(B, D, 15);
        graph.addEdge(C, D, 11);
        graph.addEdge(C, F, 2);
        graph.addEdge(D, E, 6);
        graph.addEdge(E, F, 9);

        // Run Dijkstra from source A
        System.out.println("Running Dijkstra's from Vertex A:");
        Map<Vertex, Integer> distances = dijkstraDistances(graph, A);

        for (Map.Entry<Vertex, Integer> entry : distances.entrySet()) {
            System.out.println("Distance from A to " + entry.getKey().id + ": " +
                (entry.getValue() == Integer.MAX_VALUE ? "Infinity" : entry.getValue()));
        }

        System.out.println("\n--- Resetting for new run ---");
        // Reset distances for a new run from a different source
        for (Vertex v : graph.getVertices()) {
            v.distance = Integer.MAX_VALUE;
            v.pqEntryRef = null; // Clear old references
        }

        // Run Dijkstra from source C
        System.out.println("Running Dijkstra's from Vertex C:");
        distances = dijkstraDistances(graph, C);

        for (Map.Entry<Vertex, Integer> entry : distances.entrySet()) {
            System.out.println("Distance from C to " + entry.getKey().id + ": " +
                (entry.getValue() == Integer.MAX_VALUE ? "Infinity" : entry.getValue()));
        }
    }
}