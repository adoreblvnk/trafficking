# Traffic Simulation Game

A 2D traffic simulation game built with libGDX demonstrating Object-Oriented Programming principles including inheritance, interfaces, polymorphism, encapsulation, and design patterns.

## Quick Start

### Running the Application

**Mac/Linux:**

```bash
./gradlew lwjgl3:run
```

**Windows:**

```bash
.\gradlew.bat lwjgl3:run
```

### Controls

- **Right-click**: Spawn a new vehicle
- **Left-click + Drag**: Move vehicles
- **ESC**: Pause menu
- **F5**: Quick save
- **F9**: Quick load
- **ENTER**: Start game (from menu)

## Project Overview

This project demonstrates a clean separation between a reusable game engine (`engine/`) and game-specific logic (`logic/`). The engine provides a foundation for entity management, collision detection, input handling, and scene management that could be reused for other games.

### Key Features

- **Entity System**: Hierarchical entity management with static and dynamic entities
- **Collision Detection**: AABB-based collision system with callbacks
- **Scene Management**: Stack-based scene system supporting overlays (e.g., pause menu)
- **Save/Load System**: Persistent game state management
- **Input Handling**: Event-driven input system with listener pattern
- **Automated Testing**: Headless integration tests for engine components

## Architecture

### Project Structure

```
core/src/main/java/com/sit/trafficking/
├── engine/              # Reusable game engine
│   ├── entities/        # Entity hierarchy (AbstractEntity → Static/Dynamic)
│   ├── interfaces/      # Contracts (ICollidable, Movable, InputListener)
│   ├── managers/        # System managers (Entity, Collision, Input, Movement)
│   └── scenes/          # Scene system (AbstractScene, SceneManager)
└── logic/               # Game-specific implementation
    ├── scenes/          # Concrete scenes (MenuScene, SimulationScene)
    ├── factories/       # World factory for save/load
    └── LogicConstants.java
```

### OOP Concepts Demonstrated

#### 1. **Inheritance**

- `AbstractEntity` → `StaticEntity` / `DynamicEntity`
- `AbstractScene` → `MenuScene` / `SimulationScene` / `PauseOverlay`
- `Game` → `Main`

#### 2. **Interfaces & Polymorphism**

- `ICollidable`: Enables collision detection for any entity
- `Movable`: Enables velocity-based movement
- `InputListener`: Handles user input events
- `CollisionListener`: Observes collision events (Observer pattern)

#### 3. **Design Patterns**

- **Singleton**: `SceneManager.getInstance()` - single instance manages all scenes
- **Factory**: `World` class creates and loads game states
- **Observer**: `CollisionListener` for event-driven collision handling
- **Manager Pattern**: Separate managers for entities, collisions, input, movement

#### 4. **Encapsulation**

- Managers encapsulate system logic
- Entities encapsulate state and behavior
- Private fields with controlled access via public methods

#### 5. **Abstraction**

- Engine layer is abstract and reusable
- Logic layer implements game-specific behavior
- Clear separation of concerns

## Documentation

- **[Presentation Guide](./docs/PRESENTATION_GUIDE.md)**: Detailed guide for grading and code review
- Code comments explain the _purpose_ of code, not just what it does
- JavaDoc comments on interfaces and key classes

## Testing

Run automated integration tests:

```bash
./gradlew lwjgl3:run -PmainClass=com.sit.trafficking.testing.HeadlessTestLauncher
```

Tests cover entity management, collision detection, movement processing, and manager integration.

## Technical Details

- **Framework**: [libGDX](https://libgdx.com/) 1.14.0
- **Java Version**: 21
- **Build Tool**: Gradle
- **Platform**: Desktop (LWJGL3)

## Key Files to Review

### Core Architecture

- `engine/scenes/AbstractScene.java` - Base scene with lifecycle management
- `engine/scenes/SceneManager.java` - Scene stack management (Singleton pattern)
- `engine/entities/AbstractEntity.java` - Entity base class with collision support
- `engine/managers/EntityManager.java` - Thread-safe entity registry

### Game Logic

- `logic/scenes/SimulationScene.java` - Main game scene with vehicle simulation
- `logic/factories/World.java` - Save/load system (Factory pattern)
- `Main.java` - Application entry point

### Interfaces

- `engine/interfaces/ICollidable.java` - Collision detection contract
- `engine/interfaces/Movable.java` - Movement contract
- `engine/interfaces/InputListener.java` - Input handling contract

## Build & Development

### Useful Gradle Commands

- `./gradlew lwjgl3:run` - Run the application
- `./gradlew lwjgl3:jar` - Build executable JAR
- `./gradlew build` - Build all modules
- `./gradlew clean` - Clean build artifacts

### Project Modules

- `core`: Main module with game engine and logic
- `lwjgl3`: Desktop platform launcher
