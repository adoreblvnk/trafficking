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
