package com.sit.recyclingpinball.engine.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Open registry for collider-pair collision handlers.
 *
 * New collider types can register handlers without modifying ICollider
 * or existing collider implementations.
 */
public final class CollisionDispatcher {

    private static final List<CollisionRule> RULES = new ArrayList<>();

    static {
        register(CircleCollider.class, CircleCollider.class, CircleCollider::collideCircle);
        register(CircleCollider.class, BoxCollider.class,
                (circle, box) -> SATMathUtils.getAABBvsAABB(circle.getAABB(), box.getAABB()));
        register(CircleCollider.class, OBBCollider.class,
                (circle, obb) -> SATMathUtils.getMTV(obb, circle).invert());

        register(BoxCollider.class, BoxCollider.class,
                (boxA, boxB) -> SATMathUtils.getAABBvsAABB(boxA.getAABB(), boxB.getAABB()));
        register(BoxCollider.class, OBBCollider.class,
                (box, obb) -> SATMathUtils.getMTV(obb, box).invert());

        register(OBBCollider.class, OBBCollider.class, SATMathUtils::getMTV);
    }

    private CollisionDispatcher() {
    }

    public static <A extends ICollider, B extends ICollider> void register(
            Class<A> leftType,
            Class<B> rightType,
            BiFunction<A, B, CollisionResult> handler) {
        RULES.add(new CollisionRule(leftType, rightType,
                (left, right) -> handler.apply(leftType.cast(left), rightType.cast(right))));
    }

    public static CollisionResult dispatch(ICollider left, ICollider right) {
        for (var rule : RULES) {
            if (rule.matches(left, right)) {
                return rule.apply(left, right);
            }
            if (rule.matches(right, left)) {
                return rule.apply(right, left).invert();
            }
        }

        return SATMathUtils.getAABBvsAABB(left.getAABB(), right.getAABB());
    }

    private record CollisionRule(
            Class<? extends ICollider> leftType,
            Class<? extends ICollider> rightType,
            BiFunction<ICollider, ICollider, CollisionResult> handler) {

        private boolean matches(ICollider left, ICollider right) {
            return leftType.isInstance(left) && rightType.isInstance(right);
        }

        private CollisionResult apply(ICollider left, ICollider right) {
            return handler.apply(left, right);
        }
    }
}
