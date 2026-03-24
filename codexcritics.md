# Project Critique

## entities

### PinballEntity.java
**line 27, 92**  
**what's wrong:**  
state construction is hardcoded with `new InPlayState()` and `new IdleState(this)`.  
the entity should not decide how its own state objects are built.

### PinballEntity.java
**line 96-99**  
**what's wrong:**  
this tries to use polymorphic collision resolution, but the overall gameplay flow still needs a cast elsewhere.  
the design is not consistently object-oriented yet.

### FlipperEntity.java
**line 24-35**  
**what's wrong:**  
flipper setup is packed with magic numbers for angles and dimensions even though constants already exist.

### FlipperEntity.java
**line 45**  
**what's wrong:**  
collider creation uses raw literals like `180, 40, 20, 20`.  
these should not be buried in the constructor.

### FlipperEntity.java
**line 78-79**  
**what's wrong:**  
collision response directly modifies another entity's velocity with hardcoded boost factors.  
this is tightly coupled and hard to tune.

### FlipperEntity.java
**line 91, 98**  
**what's wrong:**  
flipper rotational velocity is hardcoded as `512f` in multiple places.  
repeated literals make balancing painful.

### ShooterRodEntity.java
**line 23**  
**what's wrong:**  
collider dimensions are hardcoded with `64, 160` instead of reusing constants properly.

### ShooterRodEntity.java
**line 46-55**  
**what's wrong:**  
ball-resting logic is tied to raw geometry values like `160` and `24`.  
why must this entity know the exact ball radius by literal?

### ShooterRodEntity.java
**line 67, 79**  
**what's wrong:**  
touch Y conversion uses `1000f - y`.  
this is a hardcoded screen height inside an entity, which is a strong design smell.

### ShooterRodEntity.java
**line 98, 131**  
**what's wrong:**  
launch velocity multiplier `20f` is hardcoded in more than one place.

### TrashEntity.java
**what's wrong:**  
this file is generally okay structurally, but trash collection still depends on scene-side casting rather than trash-side polymorphism.

## events

### PinballEventBus.java
**line 7, 10**  
**what's wrong:**  
simple and readable, but this is a tightly coupled synchronous event bus with no protection against listeners mutating the list during dispatch.

### PinballEventBus.java
**line 23-26**  
**what's wrong:**  
every event is broadcast directly over a mutable `ArrayList`.  
this is fragile if the project grows.

### PinballEventVisitor.java
**line 3-13**  
**what's wrong:**  
visitor is fine here, but every new event expands the interface for every listener whether they need it or not.

## factories

### TrashFactory.java
**line 9-22**  
**what's wrong:**  
factory exists, which is good, but it is static and still tightly bound to one concrete entity type and one creation style.

### TrashFactory.java
**line 10**  
**what's wrong:**  
ID generation with `System.nanoTime()` is not a clean domain identity strategy.  
it is convenient, not architectural.

## level

### BaseLevelBlueprint.java
**line 8-29**  
**what's wrong:**  
the level is still a wall of hardcoded coordinates.  
this is fine for a prototype, but weak for scalability and reuse.

### BaseLevelBlueprint.java
**line 10-28**  
**what's wrong:**  
if I want to rebalance or resize the board, I must edit many literals directly.  
this is not data-driven enough for an A-grade architecture story.

### BoardBuilder.java
**line 12**  
**what's wrong:**  
builder owns a mutable `BoardLayout`, which is okay, but it also owns too much concrete creation logic.

### BoardBuilder.java
**line 16-17, 23-24, 29-30, 46-48**  
**what's wrong:**  
this builder directly creates concrete walls, flippers, and shooter rod instead of receiving factories/providers.

### BoardBuilder.java
**line 17, 24, 30, 47**  
**what's wrong:**  
`System.nanoTime()` is repeated all over object creation again.

### ILevelBlueprint.java
**line 6**  
**what's wrong:**  
`construct(BoardBuilder builder, PinballEventBus eventBus)` exposes concrete assembly concerns directly.  
the blueprint abstraction is not as clean as it could be.

## managers

### AssetManager.java
**line 14, 27-31**  
**what's wrong:**  
singleton.  
this weakens testability and fights against dependency injection.

### AssetManager.java
**line 50-69**  
**what's wrong:**  
sound loading is centralized, which is good, but only loading is centralized.  
playback behavior is still scattered around scenes and overlays.

### SoundManager.java
**line 29-36**  
**what's wrong:**  
sound loading can happen here too, which overlaps responsibility with `AssetManager`.

### SoundManager.java
**line 47-52**  
**what's wrong:**  
this manager exists, but many classes bypass it and call the audio provider directly.  
that makes the architecture inconsistent.

### CollisionManager.java
**line 23-27**  
**what's wrong:**  
default bounds are hardcoded to `1920x1080`.  
collision logic should not quietly assume a specific world size.

### CollisionManager.java
**line 55-104**  
**what's wrong:**  
broad phase, pair filtering, collision checking, and response dispatch all live in one manager.  
this class is carrying too many responsibilities.

### CollisionManager.java
**line 136, 141, 150-151**  
**what's wrong:**  
explicit casts to `Movable` are still present.  
this weakens the "no downcasting / polymorphism-first" argument.

### CollisionManager.java
**line 82-94**  
**what's wrong:**  
duplicate-pair filtering through identity hash comparison is clever, but not very readable or elegant for maintainers.

### GameAudioManager.java
**line 13-15**  
**what's wrong:**  
manager self-registers to the event bus in its constructor, which hides lifecycle coupling.

### GameAudioManager.java
**line 20, 25, 30**  
**what's wrong:**  
it plays sounds directly through `IAudioProvider`, while other UI classes also do the same.  
audio control is still split across the codebase.

### EntityManager.java
**line 14-15**  
**what's wrong:**  
comments claim thread-safe concurrent access, but this project looks single-threaded.  
this adds complexity without clear need.

### EntityManager.java
**line 36-45**  
**what's wrong:**  
replacing entities by duplicate ID is allowed silently.  
that can hide bugs.

## scenes

### SimulationScene.java
**line 38-42**  
**what's wrong:**  
direct construction of `EntityManager`, `CollisionManager`, `InputManager`, and `MovementManager` inside the scene.  
this is the clearest DI violation.

### SimulationScene.java
**line 41**  
**what's wrong:**  
logic scene directly constructs `PlatformRectangle` from the libgdx-named math package.  
the abstraction boundary is leaking.

### SimulationScene.java
**line 51, 54, 58-59, 80**  
**what's wrong:**  
this scene creates its own event bus, builder, score manager, audio manager, and pinball.  
it is doing too much orchestration.

### SimulationScene.java
**line 85-89**  
**what's wrong:**  
trash collection still depends on a cast:  
`TrashEntity t = (TrashEntity) b;`  
this is exactly the sort of thing the rubric warns against.

### SimulationScene.java
**line 98-102**  
**what's wrong:**  
if `isWon()` or `isLost()` stays true across multiple frames, this can keep pushing overlays repeatedly unless another mechanism stops it.

### SimulationScene.java
**line 110-112**  
**what's wrong:**  
respawn behavior is scene-owned and manually resets coordinates/state.  
why is the scene responsible for pinball lifecycle rules?

### SimulationScene.java
**line 122, 131**  
**what's wrong:**  
raw UI layout numbers like `400`, `50`, `900`, `850`, `800`, `300` are embedded directly in rendering logic.

### MenuScene.java
**line 12-16**  
**what's wrong:**  
same DI problem.  
the scene constructs its own engine managers instead of receiving them.

### MenuScene.java
**line 103-113**  
**what's wrong:**  
the scene directly plays sounds and directly transitions scenes.  
UI behavior is tightly coupled to navigation and audio concerns.

### LevelSelectScene.java
**line 20-24**  
**what's wrong:**  
same repeated manager construction again.

### LevelSelectScene.java
**line 105-127**  
**what's wrong:**  
keyboard handling is repetitive.  
every branch duplicates the same sound-play-then-transition pattern.

### LevelSelectScene.java
**line 139-166**  
**what's wrong:**  
mouse handling repeats the same duplication again for every level button.

### PauseOverlay.java
**line 17-22**  
**what's wrong:**  
even a pause overlay boots fresh managers.  
that is a lot of structural repetition for a lightweight UI layer.

### PauseOverlay.java
**line 90-94, 108-113**  
**what's wrong:**  
direct audio calls and direct scene transitions again.  
overlays are too aware of app flow.

### SimulationResultOverlay.java
**line 23-28**  
**what's wrong:**  
same repeated constructor bootstrapping of managers.

### SimulationResultOverlay.java
**line 39-43**  
**what's wrong:**  
win/lose audio is played directly here instead of through a centralized flow controller.

### SimulationResultOverlay.java
**line 135-142**  
**what's wrong:**  
retry and menu navigation are hardwired in the overlay, which increases coupling between UI and scene construction.

## main

### Main.java
**line 20-31**  
**what's wrong:**  
this is manual wiring, not proper dependency injection.  
everything is assembled concretely in the app entry point.

### Main.java
**line 22-24**  
**what's wrong:**  
singleton asset manager initialization from `Main` introduces global state into bootstrapping.

### Main.java
**line 31**  
**what's wrong:**  
`new MenuScene(context, sceneManager)` starts the chain of scene-side construction problems immediately.

### Main.java
**line 38**  
**what's wrong:**  
direct use of `Gdx.graphics.getDeltaTime()` means timing still comes from LibGDX here instead of your own time abstraction.

## platform

### LibGdxContext.java
**line 28-34**  
**what's wrong:**  
this centralizes provider creation, which is good, but it also acts as a concrete factory/service locator hybrid.

### LibGdxContext.java
**line 71-80**  
**what's wrong:**  
methods return concrete `LibGdxTime` and `LibGdxGraphics` via casts, which weakens the abstraction barrier.

### LibGdxContext.java
**line 94**  
**what's wrong:**  
direct `Gdx.app.exit()` is fine in platform code, but it confirms that app shutdown policy is platform-bound here rather than abstracted at a higher level.

### LibGdxGraphics.java
**line 143-157**  
**what's wrong:**  
font caching and disposing look inconsistent.  
cached fonts may outlive the currently active `font`, and disposed fonts may remain referenced.

### LibGdxGraphics.java
**line 287-305**  
**what's wrong:**  
textures are lazily cached, but cleanup is incomplete.

### LibGdxGraphics.java
**line 330-351**  
**what's wrong:**  
`dispose()` does not dispose cached textures or all cached fonts, so this risks resource leaks.

## tests

### HeadlessTestLauncher.java
**line 20-30**  
**what's wrong:**  
this is a manual launcher in main source, not a normal automated test suite.

### HeadlessTestLauncher.java
**line 62-63**  
**what's wrong:**  
even the test harness constructs `PlatformRectangle` directly from the libgdx-named math package, which shows the abstraction leak is systemic.

### HeadlessTestLauncher.java
**line 114, 150**  
**what's wrong:**  
test code still relies on casts from `getEntity(...)`, which mirrors the same type-safety weakness found in the main codebase.
