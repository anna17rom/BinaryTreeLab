# VisualBST

> An interactive Binary Search Tree implementation with real-time visualization

This project consists of two main implementations of a Binary Search Tree (BST):
1. A C++ implementation of a generic BST
2. A Java client-server application with a graphical interface for BST visualization

## Project Structure

```
.
├── main.cpp                    # C++ implementation of generic BST
├── Tree.java                   # Java BST implementation
├── TreeServerApplication.java  # Server-side application
├── TreeClientApplication.java  # Client-side GUI application
└── README.md                   # This file
```

## Features

### C++ Implementation (main.cpp)
- Generic Binary Search Tree implementation using templates
- Supports any comparable data type
- Operations:
  - Insertion (`tree_insert`)
  - Search (`element_search`)
  - Deletion (`tree_delete`)
  - In-order traversal (`tree_in_order`)
  - Finding minimum value (`tree_minimum`)
  - Finding successor (`tree_successor`)

### Java Implementation
#### Core Features (Tree.java)
- Generic BST implementation with comparable types
- Similar operations to C++ version
- Additional depth calculation functionality

#### Client-Server Application
- **Server (TreeServerApplication.java)**
  - Handles multiple client connections
  - Processes tree operations
  - Supports three tree types: Integer, Double, and String
  - Calculates coordinates for tree visualization

- **Client (TreeClientApplication.java)**
  - JavaFX-based graphical interface
  - Real-time tree visualization
  - Interactive operations:
    - Insert elements
    - Delete elements
    - Search elements
    - Draw tree
  - Support for different tree types
  - Visual representation of tree structure

## Requirements

### For C++ Implementation
- C++ compiler (g++ recommended)
- C++11 or later

### For Java Implementation
- Java JDK 23 or later
- JavaFX SDK
- Network connectivity for client-server communication

## Installation and Setup

### Compiling C++ Version
```bash
g++ main.cpp -o tree_program
```

### Setting up Java Version

#### Method 1: Using an IDE (Recommended)
1. Import the Java files into a new JavaFX project in IntelliJ IDEA or Eclipse
2. Add JavaFX as a dependency
3. Configure the project to use Java 23 or later
4. Set up run configurations for both server and client applications

#### Method 2: Manual Setup
1. Download JavaFX SDK from https://gluonhq.com/products/javafx/
2. Extract the SDK
3. Set the PATH_TO_FX environment variable:
```bash
export PATH_TO_FX=/path/to/javafx-sdk/lib
```
4. Compile:
```bash
javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml Tree.java TreeServerApplication.java TreeClientApplication.java
```

## Running the Applications

### C++ Version
```bash
./tree_program
```

### Java Version
1. Start the server:
```bash
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml TreeServerApplication
```

2. Start the client (in a separate terminal):
```bash
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml TreeClientApplication
```

## Usage

### C++ Version
The program accepts commands through standard input:
1. Choose tree type: `integer`, `double`, or `string`
2. Available commands:
   - `insert` - Add new elements
   - `search` - Find elements
   - `delete` - Remove elements
   - `draw` - Display tree in-order
   - `bye` - Exit program

### Java Client
1. Select tree type from the menu
2. Use buttons for operations:
   - Search
   - Insert
   - Delete
   - Draw
3. Enter values in the command field
4. Click "Okay" to execute commands
5. View results in the tree visualization area

## Network Protocol
- Server runs on localhost:1234
- Communication uses simple text-based protocol
- Commands are sent as space-separated strings
- Server responds with operation results or tree coordinates for visualization

## Implementation Details

### Tree Visualization
- Uses coordinate system for node placement
- Calculates positions based on tree depth
- Maintains proper spacing between nodes
- Draws connecting lines between parent and child nodes
- Updates in real-time for all operations

### Error Handling
- Type validation for inputs
- Network connection error handling
- Null pointer checks
- Invalid command handling

