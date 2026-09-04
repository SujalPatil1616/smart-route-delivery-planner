# Smart Route & Delivery Planner

A Java-based desktop application that applies Data Structures and Algorithms to a real-world route planning and delivery management problem.

The application represents locations as a weighted graph and provides shortest-route calculation, road network analysis, location search, autocomplete, and delivery planning through a Java Swing graphical interface.

---

## 📌 Project Overview

In a real-world transportation or delivery system, multiple locations are connected through roads with different distances. To find efficient routes and manage deliveries, the system needs suitable data structures and algorithms.

This project was developed to demonstrate how commonly used Data Structures and Algorithms can be applied to build a practical software application.

The application allows users to:

- Find the shortest route between two locations
- Calculate the total route distance
- Explore the road network
- Search for locations
- Use location autocomplete
- Analyse connected components
- Detect cycles in the road network
- Generate a delivery plan
- Use all features through a graphical user interface

---

## 🚀 Features

### 🗺️ Shortest Route

Finds the shortest route between two locations using **Dijkstra's Algorithm**.

For example:

**Mumbai → Ahmedabad**

The application can determine:

**Mumbai → Nashik → Surat → Ahmedabad**

with a total distance of:

**730 km**

---

### 🔍 Location Search & Autocomplete

Allows users to search for available locations and find matching location names.

The project includes a **Trie data structure** for efficient prefix-based searching.

---

### 🌐 Road Network

Displays the locations in the system along with their connected roads and distances.

This provides a simple way to understand and analyse the underlying graph.

---

### 📦 Delivery Planner

Provides a simple delivery planning strategy by selecting nearby unvisited locations and generating a practical delivery sequence.

A greedy approach is used for this functionality.

---

### 🔄 Graph Analysis

The project also implements several graph operations:

- Breadth-First Search (BFS)
- Depth-First Search (DFS)
- Connected Components
- Cycle Detection

These operations help analyse the structure and connectivity of the road network.

---

## 🧠 Data Structures & Algorithms Used

| Data Structure / Algorithm | Purpose |
|---|---|
| Graph | Represents locations and roads |
| Adjacency List | Stores connections between locations efficiently |
| BFS | Traverses the graph level by level |
| DFS | Traverses the graph depth-first |
| Connected Components | Identifies separate groups in the road network |
| Cycle Detection | Detects cycles in the graph |
| Dijkstra's Algorithm | Finds the shortest weighted route |
| Min Heap | Efficiently selects the minimum-distance location in Dijkstra |
| Trie | Supports prefix-based location searching |
| Greedy Approach | Used for basic delivery planning |

---

## 🛠️ Technologies Used

- **Java**
- **Java Swing**
- **Data Structures & Algorithms**
- Graphs
- Dijkstra's Algorithm
- BFS & DFS
- Min Heap
- Trie
- Greedy Algorithm

---

## 🖥️ Application Screenshots

### Main Application

![Main Screen](main-screen.png)

### Shortest Route

![Shortest Route](shortest-route.png)

### Road Network

![Road Network](road-network.png)

### Delivery Planner

![Delivery Planner](delivery-planner.png)

---

## 🏗️ How the Project Works

The application models the road network as a **weighted graph**.

Each location is represented as a node, while each road is represented as an edge containing the distance between two locations.

An **adjacency list** is used to store the connections because each location is connected only to specific nearby locations.

When a user requests a route, **Dijkstra's Algorithm** calculates the shortest distance from the selected starting location to the destination.

A **Min Heap** is used to efficiently select the location with the smallest currently known distance during the shortest-path calculation.

The application also keeps track of previous locations so that the complete shortest route can be reconstructed and displayed instead of showing only the final distance.

**BFS and DFS** are used for graph traversal, while **Connected Components** and **Cycle Detection** are used to analyse the structure of the road network.

For location searching and prefix-based suggestions, the project includes a **Trie** data structure.

The **Delivery Planner** uses a simple greedy strategy to create a practical sequence of delivery locations.

All these DSA operations are connected to a **Java Swing GUI**, allowing the user to interact with the application without directly working with the underlying graph or algorithms.

---

## ▶️ How to Run

### Run from Source

Make sure Java is installed on your system.

Compile the source code:

```bash
javac RoutePlanner.java
