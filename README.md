# Recycling Pinball

An educational, physics-based 2D arcade game built with a custom Java engine on top of libGDX. The game teaches waste sorting and sustainable living through satisfying pinball mechanics.

## Quick Start

### Running the Application
Ensure you have Java 21 installed.

**Mac/Linux:**
```bash
ALSOFT_DRIVERS=pulse ./gradlew lwjgl3:run
```

**Windows:**
```bash
.\gradlew.bat lwjgl3:run
```

## Game Mechanics & Controls

### Controls
*   **A / Left Arrow**: Left flipper
*   **D / Right Arrow**: Right flipper
*   **Mouse Drag (Down)**: Pull shooter rod (release to launch)
*   **S / Down Arrow**: Pull shooter rod via keyboard
*   **ESC**: Pause/Resume game

### Core Loop
1.  **Launch**: Use the shooter rod to launch the pinball into the "dirty" beach environment.
2.  **Interact**: Use flippers to maintain momentum and aim for targets. Flipper speed affects launch force!
3.  **Collect**: Strike recyclable trash (Plastic, Paper, Glass) to earn points and "clean" the beach.
4.  **Win**: Collect all required trash targets before exhausting your 3 balls.

## Architecture

The project follows a strict **3-tier Layered Architecture** to ensure framework independence and scalability.

### Project Structure
```
core/src/main/java/com/sit/recyclingpinball/
├── engine/              # Reusable framework-agnostic game engine
│   ├── entities/        # Entity hierarchy (AbstractEntity → Static/Dynamic)
│   ├── interfaces/      # System contracts (ICollidable, Movable, InputListener)
│   ├── managers/        # Core systems (Collision via QuadTree, EntityManager, Input)
│   ├── physics/         # SAT-based collision math (Circle, Box, OBB)
│   └── platform/        # LibGDX adapters (The ONLY layer importing com.badlogic.gdx)
└── logic/               # Pinball-specific game implementation
    ├── entities/        # Game objects (Pinball, Flipper, Trash, ShooterRod)
    ├── events/          # Visitor-based event bus for decoupled policy
    ├── factories/       # Scene, State, and Trash factories
    ├── level/           # Data-driven JSON level pipeline
    ├── scenes/          # Concrete scenes (Menu, LevelSelect, Simulation)
    └── states/          # State pattern for Pinball behavior
```

## Key Engineering Highlights

*   **Custom Physics Engine**: Implements the **Separating Axis Theorem (SAT)** for precise collision detection between Circles, Boxes, and **Oriented Bounding Boxes (OBB)**.
*   **Performance Scaling**: Utilizes a **QuadTree** for broad-phase spatial partitioning, reducing collision checks from $O(n^2)$ to $O(n \log n)$.
*   **Data-Driven Levels**: Levels are defined in external JSON files, allowing for rapid iteration of layouts without recompiling code.
*   **Decoupled Policy**: A hybrid **Observer/Visitor** event bus separates physics triggers from gameplay rules (scoring, audio).
*   **Platform Abstraction**: All LibGDX-specific logic is encapsulated behind delegation wrappers, making the core engine and game logic strictly Java-pure.

## Build & Development

### Useful Gradle Commands
*   `./gradlew lwjgl3:run`: Run the application
*   `./gradlew build`: Build all modules
*   `./gradlew clean`: Clean build artifacts
*   `./gradlew spotlessApply`: Format code to project standards

## Technical Details
*   **Java Version**: 21
*   **Framework**: [libGDX](https://libgdx.com/) 1.14.0
*   **Build Tool**: Gradle 9.2.1
*   **Patterns Used**: Factory, State, Builder, Adapter, Facade, Observer, Visitor, Double Dispatch.
