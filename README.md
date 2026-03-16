---

# World Gen

**World Gen** is a visual experiment with the **Wave Function Collapse (WFC)** algorithm implemented in **Java using Greenfoot**.
The project generates a procedural color map where each tile collapses from multiple possibilities into a final state based on adjacency constraints.

The simulation demonstrates how **local constraints propagate across a grid to create structured patterns from randomness.**

---

## Overview

The program generates a **70×70 grid** of tiles.
Each tile initially has **10 possible color states**, ranging from **yellow → purple**.

The grid begins with a **single collapsed tile in the center**, and the algorithm gradually collapses surrounding tiles while respecting adjacency rules.

Key behaviors:

* Tiles start in a **superposition of possible colors**
* The algorithm chooses the **lowest entropy cell** to collapse next
* **Constraint propagation** updates neighboring cells
* Colors form **smooth gradients** because tiles can only neighbor similar colors

---

## Algorithm

The implementation follows the **Wave Function Collapse principle**:

### Initialization

* A grid of tiles is created.
* Each tile begins with all **10 possible colors**.
* A central tile is collapsed to start the process.

### Collapse

The system selects a tile with the **lowest entropy** (fewest possibilities) and chooses one of its possible colors.

There is a **bias toward gradually increasing colors**, creating a visual gradient across the map.

### Propagation

When a tile collapses:

* Neighboring tiles remove impossible color options.
* If a neighbor's possibilities shrink, the update propagates further.

### Completion

The process repeats until **all tiles are collapsed**.

---

## Color System

Colors form a gradient between:

```
Yellow (255,255,0)
        ↓
Purple (128,0,128)
```

Tiles may only neighbor:

* the **same color**
* the **color directly above or below it in the gradient**

This creates smooth transitions instead of chaotic noise.

---

## How to Run

### Requirements

* Java
* Greenfoot

### Steps

1. Install **Greenfoot**
2. Clone the repository

```bash
git clone https://github.com/dadkomala/world-gen.git
```

3. Open the project in **Greenfoot**
4. Run the scenario

The simulation will begin collapsing the grid automatically.

---

## Key Parameters

These constants in `MyWorld.java` control the simulation:

```java
NUM_COLORS = 10
GRID_SIZE = 70
CELL_SIZE = 5
```

You can experiment with:

* **More colors** for smoother gradients
* **Larger grids** for more complex worlds
* **Different adjacency rules** for new pattern types

---

## What This Project Demonstrates

* Wave Function Collapse fundamentals
* Constraint propagation
* Entropy-based procedural generation
* Visual simulation of algorithmic processes

This project is a **playground for experimenting with procedural generation techniques.**

---

## Author

**David Lebediev Goncear**

GitHub:
[@dadkomala](https://github.com/dadkomala)

---

## License

This project is open-source and free to use for educational or experimental purposes.

---
