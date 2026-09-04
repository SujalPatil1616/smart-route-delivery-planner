import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoutePlanner {

    // =========================
    // LOCATION
    // =========================

    static class Location {
        String name;
        Road head;

        Location(String name) {
            this.name = name;
        }
    }

    // =========================
    // ROAD
    // =========================

    static class Road {
        Location destination;
        int distance;
        Road next;

        Road(Location destination, int distance) {
            this.destination = destination;
            this.distance = distance;
        }
    }

    // =========================
    // HEAP NODE
    // =========================

    static class HeapNode {
        int index;
        int distance;

        HeapNode(int index, int distance) {
            this.index = index;
            this.distance = distance;
        }
    }

    // =========================
    // MIN HEAP
    // =========================

    static class MinHeap {

        HeapNode[] heap;
        int size;

        MinHeap(int capacity) {
            heap = new HeapNode[capacity];
        }

        boolean isEmpty() {
            return size == 0;
        }

        void add(int index, int distance) {

            if (size == heap.length) {
                resize();
            }

            heap[size] = new HeapNode(index, distance);

            int current = size;
            size++;

            while (current > 0) {

                int parent = (current - 1) / 2;

                if (heap[parent].distance <=
                        heap[current].distance) {
                    break;
                }

                swap(parent, current);
                current = parent;
            }
        }

        HeapNode remove() {

            if (size == 0) {
                return null;
            }

            HeapNode result = heap[0];

            size--;

            if (size > 0) {

                heap[0] = heap[size];

                int current = 0;

                while (true) {

                    int left = current * 2 + 1;
                    int right = current * 2 + 2;
                    int smallest = current;

                    if (left < size &&
                            heap[left].distance <
                                    heap[smallest].distance) {
                        smallest = left;
                    }

                    if (right < size &&
                            heap[right].distance <
                                    heap[smallest].distance) {
                        smallest = right;
                    }

                    if (smallest == current) {
                        break;
                    }

                    swap(current, smallest);
                    current = smallest;
                }
            }

            return result;
        }

        void swap(int a, int b) {

            HeapNode temp = heap[a];
            heap[a] = heap[b];
            heap[b] = temp;
        }

        void resize() {

            HeapNode[] newHeap =
                    new HeapNode[heap.length * 2];

            for (int i = 0; i < heap.length; i++) {
                newHeap[i] = heap[i];
            }

            heap = newHeap;
        }
    }

    // =========================
    // TRIE
    // =========================

    static class TrieNode {

        TrieNode[] children =
                new TrieNode[26];

        boolean isEnd;
    }

    static class Trie {

        TrieNode root = new TrieNode();

        void insert(String word) {

            word = word.toLowerCase();

            TrieNode current = root;

            for (int i = 0; i < word.length(); i++) {

                char ch = word.charAt(i);

                if (ch < 'a' || ch > 'z') {
                    continue;
                }

                int index = ch - 'a';

                if (current.children[index] == null) {
                    current.children[index] =
                            new TrieNode();
                }

                current = current.children[index];
            }

            current.isEnd = true;
        }
    }

    // =========================
    // GRAPH
    // =========================

    static class Graph {

        Location[] locations =
                new Location[50];

        int size;

        Trie trie = new Trie();

        void addLocation(String name) {

            locations[size] =
                    new Location(name);

            trie.insert(name);

            size++;
        }

        Location findLocation(String name) {

            for (int i = 0; i < size; i++) {

                if (locations[i].name
                        .equalsIgnoreCase(name)) {

                    return locations[i];
                }
            }

            return null;
        }

        int getIndex(Location location) {

            for (int i = 0; i < size; i++) {

                if (locations[i] == location) {
                    return i;
                }
            }

            return -1;
        }

        void addRoad(
                String from,
                String to,
                int distance
        ) {

            Location source =
                    findLocation(from);

            Location destination =
                    findLocation(to);

            Road road1 =
                    new Road(destination, distance);

            road1.next = source.head;
            source.head = road1;

            Road road2 =
                    new Road(source, distance);

            road2.next = destination.head;
            destination.head = road2;
        }

        // =========================
        // DIJKSTRA
        // =========================

        int[] dijkstra(
                int startIndex,
                int[] previous
        ) {

            int[] distance =
                    new int[size];

            boolean[] visited =
                    new boolean[size];

            for (int i = 0; i < size; i++) {

                distance[i] =
                        Integer.MAX_VALUE;

                previous[i] = -1;
            }

            distance[startIndex] = 0;

            MinHeap heap =
                    new MinHeap(size + 5);

            heap.add(startIndex, 0);

            while (!heap.isEmpty()) {

                HeapNode node =
                        heap.remove();

                int current =
                        node.index;

                if (visited[current]) {
                    continue;
                }

                visited[current] = true;

                Road road =
                        locations[current].head;

                while (road != null) {

                    int next =
                            getIndex(
                                    road.destination
                            );

                    int newDistance =
                            distance[current] +
                                    road.distance;

                    if (!visited[next] &&
                            newDistance <
                                    distance[next]) {

                        distance[next] =
                                newDistance;

                        previous[next] =
                                current;

                        heap.add(
                                next,
                                newDistance
                        );
                    }

                    road = road.next;
                }
            }

            return distance;
        }

        // =========================
        // SHORTEST PATH
        // =========================

        String shortestPath(
                String startName,
                String endName
        ) {

            Location start =
                    findLocation(startName);

            Location end =
                    findLocation(endName);

            if (start == null ||
                    end == null) {

                return "Location not found.";
            }

            int startIndex =
                    getIndex(start);

            int endIndex =
                    getIndex(end);

            int[] previous =
                    new int[size];

            int[] distance =
                    dijkstra(
                            startIndex,
                            previous
                    );

            if (distance[endIndex] ==
                    Integer.MAX_VALUE) {

                return "No route exists.";
            }

            int[] path =
                    new int[size];

            int pathSize = 0;

            int current = endIndex;

            while (current != -1) {

                path[pathSize++] =
                        current;

                current =
                        previous[current];
            }

            StringBuilder result =
                    new StringBuilder();

            result.append(
                    "BEST ROUTE\n\n"
            );

            for (int i = pathSize - 1;
                 i >= 0;
                 i--) {

                result.append(
                        locations[path[i]].name
                );

                if (i != 0) {
                    result.append("  →  ");
                }
            }

            result.append(
                    "\n\n"
            );

            result.append(
                    "Total Distance: "
            );

            result.append(
                    distance[endIndex]
            );

            result.append(
                    " km"
            );

            return result.toString();
        }

        // =========================
        // SEARCH
        // =========================

        String search(String name) {

            Location location =
                    findLocation(name);

            if (location == null) {
                return "Location not found.";
            }

            StringBuilder result =
                    new StringBuilder();

            result.append(
                    "LOCATION FOUND\n\n"
            );

            result.append(
                    location.name
            );

            result.append(
                    "\n\nCONNECTED ROADS\n\n"
            );

            Road road =
                    location.head;

            while (road != null) {

                result.append("• ");

                result.append(
                        road.destination.name
                );

                result.append(
                        "  —  "
                );

                result.append(
                        road.distance
                );

                result.append(
                        " km\n"
                );

                road = road.next;
            }

            return result.toString();
        }

        // =========================
        // NETWORK
        // =========================

        String network() {

            StringBuilder result =
                    new StringBuilder();

            result.append(
                    "ROAD NETWORK\n\n"
            );

            for (int i = 0; i < size; i++) {

                result.append(
                        locations[i].name
                );

                result.append(" → ");

                Road road =
                        locations[i].head;

                while (road != null) {

                    result.append(
                            road.destination.name
                    );

                    result.append("(");

                    result.append(
                            road.distance
                    );

                    result.append(" km)");

                    if (road.next != null) {
                        result.append(" → ");
                    }

                    road = road.next;
                }

                result.append("\n\n");
            }

            return result.toString();
        }

        // =========================
        // AUTOCOMPLETE
        // =========================

        String[] autocomplete(
                String prefix
        ) {

            String[] result =
                    new String[10];

            int count = 0;

            prefix =
                    prefix.toLowerCase();

            for (int i = 0;
                 i < size && count < 10;
                 i++) {

                if (locations[i].name
                        .toLowerCase()
                        .startsWith(prefix)) {

                    result[count++] =
                            locations[i].name;
                }
            }

            return result;
        }

        // =========================
        // DELIVERY PLAN
        // =========================

        String deliveryPlan(
                String startName,
                String[] deliveryNames
        ) {

            Location current =
                    findLocation(startName);

            if (current == null) {
                return "Starting location not found.";
            }

            Location[] deliveries =
                    new Location[
                            deliveryNames.length
                            ];

            for (int i = 0;
                 i < deliveryNames.length;
                 i++) {

                deliveries[i] =
                        findLocation(
                                deliveryNames[i]
                        );

                if (deliveries[i] == null) {

                    return "Location not found: "
                            + deliveryNames[i];
                }
            }

            boolean[] delivered =
                    new boolean[
                            deliveries.length
                            ];

            int totalDistance = 0;

            StringBuilder result =
                    new StringBuilder();

            result.append(
                    "DELIVERY PLAN\n\n"
            );

            result.append(
                    "Starting Point: "
            );

            result.append(
                    current.name
            );

            result.append("\n\n");

            for (int step = 0;
                 step < deliveries.length;
                 step++) {

                int nearest = -1;

                int nearestDistance =
                        Integer.MAX_VALUE;

                for (int i = 0;
                     i < deliveries.length;
                     i++) {

                    if (delivered[i]) {
                        continue;
                    }

                    int distance =
                            shortestDistance(
                                    current.name,
                                    deliveries[i].name
                            );

                    if (distance != -1 &&
                            distance <
                                    nearestDistance) {

                        nearestDistance =
                                distance;

                        nearest = i;
                    }
                }

                if (nearest == -1) {

                    return "Some locations are unreachable.";
                }

                result.append(
                        (step + 1)
                );

                result.append(
                        ". "
                );

                result.append(
                        current.name
                );

                result.append(
                        " → "
                );

                result.append(
                        deliveries[nearest].name
                );

                result.append(
                        "   ("
                );

                result.append(
                        nearestDistance
                );

                result.append(
                        " km)\n"
                );

                totalDistance +=
                        nearestDistance;

                current =
                        deliveries[nearest];

                delivered[nearest] =
                        true;
            }

            result.append(
                    "\nTotal Distance: "
            );

            result.append(
                    totalDistance
            );

            result.append(
                    " km"
            );

            return result.toString();
        }

        int shortestDistance(
                String start,
                String end
        ) {

            Location source =
                    findLocation(start);

            Location destination =
                    findLocation(end);

            if (source == null ||
                    destination == null) {

                return -1;
            }

            int[] previous =
                    new int[size];

            int[] distance =
                    dijkstra(
                            getIndex(source),
                            previous
                    );

            int index =
                    getIndex(destination);

            if (distance[index] ==
                    Integer.MAX_VALUE) {

                return -1;
            }

            return distance[index];
        }
    }

    // =========================
    // APPLICATION
    // =========================

    static Graph graph =
            new Graph();

    static JFrame frame;

    static JTextArea output;

    static JComboBox<String> fromBox;

    static JComboBox<String> toBox;

    static JLabel statusLabel;

    // =========================
    // LOAD DATA
    // =========================

    static void loadData() {

        graph.addLocation("Mumbai");
        graph.addLocation("Nashik");
        graph.addLocation("Pune");
        graph.addLocation("Surat");
        graph.addLocation("Ahmedabad");

        graph.addRoad(
                "Mumbai",
                "Nashik",
                165
        );

        graph.addRoad(
                "Mumbai",
                "Pune",
                150
        );

        graph.addRoad(
                "Nashik",
                "Pune",
                210
        );

        graph.addRoad(
                "Nashik",
                "Surat",
                300
        );

        graph.addRoad(
                "Pune",
                "Surat",
                420
        );

        graph.addRoad(
                "Surat",
                "Ahmedabad",
                265
        );
    }

    // =========================
    // GUI
    // =========================

    static void createGUI() {

        frame =
                new JFrame(
                        "Smart Route & Delivery Planner"
                );

        frame.setSize(
                900,
                650
        );

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        frame.setLayout(
                new BorderLayout()
        );

        // HEADER

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBorder(
                new EmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );

        JLabel title =
                new JLabel(
                        "SMART ROUTE PLANNER"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Intelligent route and delivery management"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        JPanel titlePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        frame.add(
                header,
                BorderLayout.NORTH
        );

        // MAIN

        JPanel main =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        main.setBorder(
                new EmptyBorder(
                        10,
                        30,
                        10,
                        30
                )
        );

        // ROUTE CARD

        JPanel routePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                12,
                                8
                        )
                );

        routePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Find Shortest Route"
                        ),
                        new EmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        JLabel fromLabel =
                new JLabel("From");

        JLabel toLabel =
                new JLabel("To");

        routePanel.add(fromLabel);
        routePanel.add(toLabel);
        routePanel.add(new JLabel());

        fromBox =
                new JComboBox<>();

        toBox =
                new JComboBox<>();

        JButton routeButton =
                new JButton(
                        "FIND ROUTE"
                );

        routeButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        routeButton.addActionListener(
                e -> findRoute()
        );

        routePanel.add(fromBox);
        routePanel.add(toBox);
        routePanel.add(routeButton);

        main.add(
                routePanel,
                BorderLayout.NORTH
        );

        // RESULT

        JPanel resultPanel =
                new JPanel(
                        new BorderLayout()
                );

        resultPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Route Result"
                )
        );

        output =
                new JTextArea();

        output.setEditable(false);

        output.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        17
                )
        );

        output.setLineWrap(true);

        output.setWrapStyleWord(true);

        output.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        output.setText(
                "Welcome!\n\n" +
                "Select your starting location and destination,\n" +
                "then click FIND ROUTE."
        );

        JScrollPane scroll =
                new JScrollPane(output);

        resultPanel.add(
                scroll,
                BorderLayout.CENTER
        );

        main.add(
                resultPanel,
                BorderLayout.CENTER
        );

        frame.add(
                main,
                BorderLayout.CENTER
        );

        // BOTTOM

        JPanel bottom =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        JPanel buttons =
                new JPanel(
                        new GridLayout(
                                1,
                                5,
                                10,
                                10
                        )
                );

        JButton searchButton =
                new JButton(
                        "Search"
                );

        JButton autocompleteButton =
                new JButton(
                        "Autocomplete"
                );

        JButton networkButton =
                new JButton(
                        "Road Network"
                );

        JButton deliveryButton =
                new JButton(
                        "Delivery Planner"
                );

        JButton clearButton =
                new JButton(
                        "Clear"
                );

        searchButton.addActionListener(
                e -> searchLocation()
        );

        autocompleteButton.addActionListener(
                e -> autocomplete()
        );

        networkButton.addActionListener(
                e -> showNetwork()
        );

        deliveryButton.addActionListener(
                e -> deliveryPlanner()
        );

        clearButton.addActionListener(
                e -> {
                    output.setText("");
                    statusLabel.setText(
                            "Ready"
                    );
                }
        );

        buttons.add(searchButton);
        buttons.add(autocompleteButton);
        buttons.add(networkButton);
        buttons.add(deliveryButton);
        buttons.add(clearButton);

        bottom.add(
                buttons,
                BorderLayout.CENTER
        );

        statusLabel =
                new JLabel(
                        "Ready"
                );

        statusLabel.setBorder(
                new EmptyBorder(
                        8,
                        5,
                        5,
                        5
                )
        );

        bottom.add(
                statusLabel,
                BorderLayout.SOUTH
        );

        frame.add(
                bottom,
                BorderLayout.SOUTH
        );

        loadLocations();

        frame.setVisible(true);
    }

    // =========================
    // LOAD LOCATIONS
    // =========================

    static void loadLocations() {

        for (int i = 0;
             i < graph.size;
             i++) {

            fromBox.addItem(
                    graph.locations[i].name
            );

            toBox.addItem(
                    graph.locations[i].name
            );
        }

        fromBox.setSelectedItem(
                "Mumbai"
        );

        toBox.setSelectedItem(
                "Ahmedabad"
        );
    }

    // =========================
    // FIND ROUTE
    // =========================

    static void findRoute() {

        String from =
                (String)
                        fromBox.getSelectedItem();

        String to =
                (String)
                        toBox.getSelectedItem();

        output.setText(
                graph.shortestPath(
                        from,
                        to
                )
        );

        statusLabel.setText(
                "Route calculated successfully"
        );
    }

    // =========================
    // SEARCH
    // =========================

    static void searchLocation() {

        String name =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter location:",
                        "Search Location",
                        JOptionPane.QUESTION_MESSAGE
                );

        if (name == null ||
                name.trim().isEmpty()) {
            return;
        }

        output.setText(
                graph.search(
                        name.trim()
                )
        );

        statusLabel.setText(
                "Location search completed"
        );
    }

    // =========================
    // AUTOCOMPLETE
    // =========================

    static void autocomplete() {

        String prefix =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter location prefix:",
                        "Autocomplete",
                        JOptionPane.QUESTION_MESSAGE
                );

        if (prefix == null ||
                prefix.trim().isEmpty()) {
            return;
        }

        String[] suggestions =
                graph.autocomplete(
                        prefix.trim()
                );

        StringBuilder result =
                new StringBuilder();

        result.append(
                "LOCATION SUGGESTIONS\n\n"
        );

        boolean found = false;

        for (String location :
                suggestions) {

            if (location != null) {

                result.append(
                        "• "
                );

                result.append(
                        location
                );

                result.append(
                        "\n"
                );

                found = true;
            }
        }

        if (!found) {

            result.append(
                    "No matching locations found."
            );
        }

        output.setText(
                result.toString()
        );

        statusLabel.setText(
                "Autocomplete completed"
        );
    }

    // =========================
    // ROAD NETWORK
    // =========================

    static void showNetwork() {

        output.setText(
                graph.network()
        );

        statusLabel.setText(
                "Road network displayed"
        );
    }

    // =========================
    // DELIVERY PLANNER
    // =========================

    static void deliveryPlanner() {

        String start =
                JOptionPane.showInputDialog(
                        frame,
                        "Starting location:",
                        "Delivery Planner",
                        JOptionPane.QUESTION_MESSAGE
                );

        if (start == null ||
                start.trim().isEmpty()) {
            return;
        }

        String deliveries =
                JOptionPane.showInputDialog(
                        frame,
                        "Enter delivery locations\n" +
                        "separated by commas.\n\n" +
                        "Example:\n" +
                        "Nashik, Surat, Ahmedabad",
                        "Delivery Planner",
                        JOptionPane.QUESTION_MESSAGE
                );

        if (deliveries == null ||
                deliveries.trim().isEmpty()) {
            return;
        }

        String[] deliveryNames =
                deliveries.split(",");

        for (int i = 0;
             i < deliveryNames.length;
             i++) {

            deliveryNames[i] =
                    deliveryNames[i].trim();
        }

        output.setText(
                graph.deliveryPlan(
                        start.trim(),
                        deliveryNames
                )
        );

        statusLabel.setText(
                "Delivery plan generated"
        );
    }

    // =========================
    // MAIN
    // =========================

    public static void main(
            String[] args
    ) {

        loadData();

        SwingUtilities.invokeLater(
                () -> createGUI()
        );
    }
}