I'm taking a module on Object Oriented Programming in Java.

We are expected to apply the OOP pillars: Encapsulation, Inheritance, Abstraction, Polymorphism

Apply SOLID principles: Single Responsibility, Open-Closed, Liskov Substitution, Interface Segregation, Dependency Inversion

And apply at least 3 of the 23 design patterns: **Creational**: Abstract Factory, Builder, Factory Method, Prototype, Singleton; **Structural**: Adapter, Bridge, Composite, Decorator, Facade, Flyweight, Proxy; **Behavioral**: Chain of Responsibility, Command, Interpreter, Iterator, Mediator, Memento, Observer, State, Strategy, Template Method, Visitor. NOTE: some of these are antipatterns & avoided at all costs (eg Singletons)
# 📄 Master Technical Specification: recyclingpinball Simulator (INF1009 Part 2)

## 1. Project Overview & Objectives
*   **Topic Reference (Option A & B):** Educational Awareness / Simulation of Real Systems.
*   **Real-World Purpose:** To visually educate the public on the **SIRD Model** (Susceptible, Infected, Recovered, Deceased) of viral spread. It mathematically demonstrates why real-world interventions (masks, social distancing, vaccines) are necessary to prevent hospital systems from being overwhelmed, directly saving lives.
*   **Win/Loss Conditions:**
    *   **Win:** The virus is completely eradicated (0 `Infected` remaining) AND the `deadCount` is strictly below the map's `maxCasualties` limit.
    *   **Lose:** The `deadCount` exceeds the `maxCasualties` limit (The interventions failed and the hospital was overwhelmed), OR the virus infects the entire population with no survivors.

---

## 2. Engine Architecture & Boundary (Crucial for A+)
To satisfy the strict "Engine Reusability" and "Architecture Separation" requirements, there is an absolute boundary between `com.sit.recyclingpinball.engine` and `com.sit.recyclingpinball.logic`.

*   **The Framework Adapter Layer:** LibGDX is completely abstracted away in `engine.platform.libgdx`. The abstract engine strictly communicates via internal interfaces (`IGraphicsProvider`, `IAudioProvider`, `IInputProvider`, etc.). 
*   **Zero Logic in Engine:** The abstract engine knows *nothing* about viruses, citizens, or the SIRD model. It only knows about `AbstractEntity`, `AbstractScene`, Collisions, and rendering shapes/textures.
*   **Temporary Exceptions:** As per current team consensus, `Vector2`, `Rectangle`, and `Input` (from LibGDX) remain in the engine boundary for now and will not be abstracted until a later refactoring phase.

---

## 3. Application UI & Scene Flow (The User Experience)
The application runs at **1900x1000 resolution** and utilizes the abstract engine's `SceneManager` to navigate between states seamlessly. 

1.  **`MenuScene`:** Title logo. Buttons: "Play Game", "Exit Game".
2.  **`LevelSelectScene`:** Dynamically reads the `assets/worlds/` directory using `IOManager` and generates a UI button for every `.json` map file found. Demonstrates engine decoupling.
3.  **`EducationalContextOverlay`:** Pushed just before a level starts. Pauses time. Displays a semi-transparent black overlay spanning the whole screen with a white, word-wrapped text box perfectly centered displaying the map's real-world context.
4.  **`SimulationScene` (The Core Game):** The arena with bouncing entities (64x64 pixels). 
    *   **HUD:** Top-left corner displays clean, bold Geist text (`Susceptible: X`, `Infected: Y`, `Recovered: Z`, `Deceased: W`).
    *   **Control Panel:** The bottom 150 pixels (`y=0` to `y=150`) is a dedicated dark grey UI panel containing the intervention tool buttons.
5.  **`PauseOverlay`:** Pushed via ESC key. Pauses time. Buttons: "Resume", "Keyboard Controls", "Exit to Main Menu".
6.  **`SimulationResultOverlay`:** Pushed when Win/Loss conditions are met. Plays `win.mp3` or `lose.mp3`. Displays final statistics and educational takeaways.

---

## 4. The Core Visuals & Mechanics (The SIRD Model)
Citizens bounce off the walls infinitely. The background color is a sleek dark grey (`#1E1E24`) and static walls are drawn as solid white.

*   🔵 **Susceptible (Healthy):** Moving normally. Vulnerable to infection on collision (base 75% chance).
*   🔴 **Infected (Sick):** Moving normally. Carries an internal 20-second viral timer. Infects Susceptible entities on collision.
*   ⚪ **Recovered (Immune):** Moving normally. Cannot be infected or infect others.
*   ⚫ **Deceased (Dead):** Turns dark grey. Velocity drops to `(0,0)`. Collision logic is disabled so they no longer act as physical barriers.

---

## 5. Required Design Patterns (Targeting A+)

### 🌟 Pattern 1: State Pattern (Behavioral)
**Location:** `logic.entities.states.IHealthState`
**Purpose:** Manages citizen health lifecycles and polymorphic behavior dynamically, satisfying the **Open-Closed Principle (OCP)**.
*   **`SusceptibleState`:** On collision with an `InfectedState` entity, evaluates probability. If infected, transitions the entity to `InfectedState`.
*   **`InfectedState`:** Tracks an internal 20s timer via `update(dt)`. At `0`, it queries `SimulationConfig`. If active infections exceed `hospitalCapacity`, the mortality probability doubles. Transitions to `DeceasedState` or `RecoveredState`.
*   **`RecoveredState`:** Immune. `handleCollision()` yields no state change.
*   **`DeceasedState`:** Disables physics collisions and halts movement.

### 🌟 Pattern 2: Observer Pattern / Event Bus (Behavioral)
**Location:** `logic.events.SimulationEventBus`, `logic.managers.SimulationStatsManager`, `logic.managers.SimulationAudioManager`
**Purpose:** Decouples game logic from UI, analytics, and audio, satisfying the **Single Responsibility Principle (SRP)**.
*   **Event:** `HealthStateChangedEvent` fires whenever a citizen's state changes.
*   **Observers:** 
    *   `SimulationStatsManager` updates S, I, R, D counts and evaluates Win/Loss conditions.
    *   `SimulationAudioManager` listens for events to play `infect.mp3`, `flatline.mp3`, etc. Features a 5-second cooldown per sound type to prevent audio spam.

### 🌟 Pattern 3: Strategy Pattern (Behavioral)
**Location:** `logic.interventions.IInterventionStrategy`
**Purpose:** Encapsulates the player's tools (the "Game" aspect) into interchangeable behaviors, replacing hardcoded conditionals.
*   **`MaskMandateStrategy`:** Has a 60-second cooldown. Reduces the global infection chance by 75%. Visually swaps the citizen sprites to `citizen_mask.png`.
*   **`SocialDistancingStrategy`:** Dynamically increases the physical collision `radius` of all citizens by 1.5x so they bounce away from each other *before* their sprites visually overlap.
*   **`VaccinationStrategy`:** Toggles a "vaccination mode". Uses a limited pool of vaccines (`vaccineCount`). The player clicks screen coordinates intersecting a `Susceptible` citizen to instantly transition them to `RecoveredState`.

### 🌟 Pattern 4: Builder Pattern (Creational)
**Location:** `logic.factories.SimulationConfigBuilder`
**Purpose:** Avoids the anti-pattern of a Singleton settings class and prevents "Telescoping Constructors" when loading complex JSON levels.
*   Reads the parsed JSON via `IOManager` and constructs a robust `SimulationConfig` object (containing `population`, `hospitalCapacity`, `maxCasualties`, `vaccineCount`, etc.) which is injected into the `SimulationScene`.

---

## 6. Applied OOP & SOLID Principles
*   **Encapsulation:** Entity states and timers are strictly hidden inside their respective `IHealthState` classes. No direct access to internal counters.
*   **Inheritance & Abstraction:** `CitizenEntity` extends `DynamicEntity`. The logic layer extends `AbstractScene`, inheriting core lifecycle methods automatically.
*   **Polymorphism:** The `CollisionManager` resolves collisions by calling `.intersects()` on the `ICollider` interface, completely unaware of whether it's checking a `BoxCollider` (walls) or `CircleCollider` (citizens).
*   **Dependency Inversion (DIP):** The logic layer depends on `IGraphicsProvider` and `IAudioProvider` (abstractions), *not* on `LibGdxGraphics` (details).
*   **Interface Segregation (ISP):** Interfaces are kept small and focused.

---

## 7. Data-Driven Level Architecture (JSON Maps)
Levels are dynamically loaded to prove the engine is highly reusable and separated from hardcoded logic. Adjustments are made to accommodate the 1900x1000 screen and bottom UI panel.

**Sample JSON (`assets/worlds/supermarket.json`):**
```json
{
  "mapName": "Supermarket Aisles",
  "educationalText": "Tight aisles force citizens close together, drastically increasing the spread rate. Flatten the curve to save lives!",
  "difficulty": "Hard",
  "population": 50,
  "hospitalCapacity": 15,
  "maxCasualties": 5,
  "baseMortalityRate": 0.08,
  "baseInfectionRate": 0.75,
  "vaccineCount": 5,
  "entities":[
    { "id": "wall_top", "type": "STATIC", "x": 0, "y": 980, "w": 1900, "h": 20 },
    { "id": "wall_bottom", "type": "STATIC", "x": 0, "y": 150, "w": 1900, "h": 20 },
    { "id": "wall_left", "type": "STATIC", "x": 0, "y": 150, "w": 20, "h": 830 },
    { "id": "wall_right", "type": "STATIC", "x": 1880, "y": 150, "w": 20, "h": 830 },
    { "id": "aisle_1", "type": "STATIC", "x": 400, "y": 300, "w": 40, "h": 500 }
  ]
}
```

---

## 8. Implementation Process Flow 
*Work in this exact order to maintain the engine/logic boundary.*

*   **Phase 1: Engine Upgrades (`com.sit.recyclingpinball.engine`)**
    1. Expand `IGraphicsProvider` to include `void drawText(...)` and `void fillRectangle(...)`. Implement this in `LibGdxGraphics` using `BitmapFont` and `ShapeRenderer`.
*   **Phase 2: Data-Driven Foundation (`com.sit.recyclingpinball.logic`)**
    1. Create `SimulationConfig` and `SimulationConfigBuilder` to parse the newly structured JSONs (with 1900x1000 coordinates, populations of 40-60, and `vaccineCount`).
*   **Phase 3: Core Simulation Logic (SIRD Model)**
    1. Create the `IHealthState` interface and its 4 concrete State classes.
    2. Create `CitizenEntity` extending `DynamicEntity` (64x64 pixels). Ensure infinite bouncing.
*   **Phase 4: Observer Pattern (Events & Audio)**
    1. Implement `SimulationEventBus`, `SimulationStatsManager`, and `SimulationAudioManager`.
    2. Hook up the 5-second audio cooldowns and Win/Loss triggers.
*   **Phase 5: Interactivity & Tools (Strategies)**
    1. Implement the 3 `IInterventionStrategy` classes (Mask, Social Distancing, Vaccination).
*   **Phase 6: Custom UI System**
    1. Build `ButtonEntity` inside `logic/ui` leveraging `ui_button.png` and `IGraphicsProvider.drawText()`. Include hover-scaling logic.
*   **Phase 7: Scenes, Overlays, & Aesthetics**
    1. Build `SimulationScene` with the dark grey bottom panel (y=0 to y=150) and top-left HUD.
    2. Build `EducationalContextOverlay` (semi-transparent background with centered Geist text box).
    3. Build `MenuScene`, `LevelSelectScene`, `PauseOverlay`, and `SimulationResultOverlay`.

---

## 9. Asset Requirements Directory
These assets are pre-existing and correctly mapped:

*   **Worlds (`assets/worlds/`):**
    *   `default_park.json`, `supermarket.json`, `office_building.json`
*   **Textures (`assets/textures/`):**
    *   `citizen_susceptible.png` (Blue circle)
    *   `citizen_infected.png` (Red circle)
    *   `citizen_recovered.png` (White/Grey circle)
    *   `citizen_deceased.png` (Dark grey cross)
    *   `citizen_mask.png`
    *   `ui_button.png`
*   **Sounds (`assets/sounds/`):**
    *   `infect.mp3`, `vaccine.mp3`, `flatline.mp3`, `win.mp3`, `lose.mp3`
*   **Fonts (`assets/fonts/`):**
    *   `Geist-Bold.ttf`, `Geist-Regular.ttf`
