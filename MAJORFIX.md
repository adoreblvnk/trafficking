### STILL MAJOR: `AbstractScene` Depends on Concrete Managers

From the code:
```java
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;
```

No `IEntityManager`, `ICollisionManager`, `IInputManager`, `IMovementManager` interfaces exist. This violates DIP — the abstract scene framework depends on concrete implementations.

### STILL MAJOR: `AssemblyFactory` Is a God-Factory

`AssemblyFactory` still creates `MenuScene`, `LevelSelectScene`, `SimulationScene`, `PauseOverlay`, `SimulationResultOverlay`, plus `IPinballState` instances (IdleState, InPlayState, DrainedState). It also imports `PlatformRectangle` from the platform layer (line 1469).

Two problems:
1. SRP violation: creates all scenes AND all states.
2. It imports a platform type (`PlatformRectangle`) in game logic — another layer violation.

### STILL MAJOR: `LibGdxGraphics` Depends on Concrete `AssetManager`

```java
import com.sit.recyclingpinball.engine.managers.AssetManager;
```

The Platform adapter depends upward on an Engine Core concrete class. This should go through an `IAssetProvider` interface.

### STILL MAJOR: `ShooterRodEntity extends DynamicEntity`

```java
public class ShooterRodEntity extends DynamicEntity implements InputListener {
```

A shooter rod is anchored, responds to drag input, and fires the ball. It is not a physics-driven body with velocity/friction/mass. This remains an LSP violation.

### STILL MAJOR: `ICollider` Has Concrete Type Overloads

```java
CollisionResult checkCollision(ICollider other);
CollisionResult checkCollision(CircleCollider other);
CollisionResult checkCollision(BoxCollider other);
```

Adding a new collider type (e.g., `PolygonCollider`) still requires modifying this interface and all implementors. OCP violation persists.