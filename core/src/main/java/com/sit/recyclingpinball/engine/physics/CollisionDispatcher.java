package com.sit.recyclingpinball.engine.physics;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Centralizes collision resolution for pairs of collider types. Adding a new
 * collider type only requires registering new handlers here, without modifying
 * the ICollider interface or existing collider classes.
 */
public final class CollisionDispatcher {

    private static final Map<TypePair, BiFunction<ICollider, ICollider, CollisionResult>> handlers = new HashMap<>();

    static {
        // Circle vs Circle
        register(CircleCollider.class, CircleCollider.class,
                (a, b) -> ((CircleCollider) a).collideCircle((CircleCollider) b));

        // Circle vs Box
        register(CircleCollider.class, BoxCollider.class,
                (a, b) -> SATMathUtils.getAABBvsAABB(a.getAABB(), b.getAABB()));

        // Circle vs OBB
        register(CircleCollider.class, OBBCollider.class,
                (a, b) -> SATMathUtils.getMTV((OBBCollider) b, (CircleCollider) a).invert());

        // Box vs Box
        register(BoxCollider.class, BoxCollider.class, (a, b) -> SATMathUtils.getAABBvsAABB(a.getAABB(), b.getAABB()));

        // Box vs OBB
        register(BoxCollider.class, OBBCollider.class,
                (a, b) -> SATMathUtils.getMTV((OBBCollider) b, (BoxCollider) a).invert());

        // OBB vs OBB
        register(OBBCollider.class, OBBCollider.class, (a, b) -> SATMathUtils.getMTV((OBBCollider) a, (OBBCollider) b));
    }

    private CollisionDispatcher() {
    }

    public static void register(Class<? extends ICollider> typeA, Class<? extends ICollider> typeB,
            BiFunction<ICollider, ICollider, CollisionResult> handler) {
        handlers.put(new TypePair(typeA, typeB), handler);
    }

    public static CollisionResult dispatch(ICollider a, ICollider b) {
        TypePair key = new TypePair(a.getClass(), b.getClass());
        BiFunction<ICollider, ICollider, CollisionResult> handler = handlers.get(key);
        if (handler != null) {
            return handler.apply(a, b);
        }

        // Try reversed pair
        TypePair reversed = new TypePair(b.getClass(), a.getClass());
        handler = handlers.get(reversed);
        if (handler != null) {
            return handler.apply(b, a).invert();
        }

        // Fallback to AABB vs AABB
        return SATMathUtils.getAABBvsAABB(a.getAABB(), b.getAABB());
    }

    private static final class TypePair {
        private final Class<?> typeA;
        private final Class<?> typeB;

        TypePair(Class<?> typeA, Class<?> typeB) {
            this.typeA = typeA;
            this.typeB = typeB;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof TypePair))
                return false;
            TypePair that = (TypePair) o;
            return typeA == that.typeA && typeB == that.typeB;
        }

        @Override
        public int hashCode() {
            return Objects.hash(typeA, typeB);
        }
    }
}
