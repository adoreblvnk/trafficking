package com.sit.trafficking.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.managers.LevelManager;
import com.sit.trafficking.engine.managers.PhysicsManager;
import com.sit.trafficking.engine.managers.SceneManager;
import com.sit.trafficking.utils.Constants;

public class SimulationScreen extends AbstractScreen {

    private final Box2DDebugRenderer debugRenderer;
    private final InputMultiplexer inputMultiplexer;
    
    // Explosion Visuals
    private Vector2 explosionPos = new Vector2();
    private boolean drawExplosion = false;

    public SimulationScreen() {
        super();
        this.debugRenderer = new Box2DDebugRenderer();
        
        // Reset and Load
        EntityManager.getInstance().clear();
        LevelManager.getInstance().loadLevel("levels/level1.json");

        // Input Handling
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    spawnDynamicEntity(screenX, screenY);
                    return true;
                } else if (button == Input.Buttons.RIGHT) {
                    triggerExplosion(screenX, screenY);
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    SceneManager.getInstance().pushScreen(new PauseScreen());
                    return true;
                }
                if (keycode == Input.Keys.SPACE) {
                    PhysicsManager.getInstance().setTimeScale(0.1f);
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    PhysicsManager.getInstance().setTimeScale(1.0f);
                    return true;
                }
                return false;
            }
        });
    }

    private void triggerExplosion(int screenX, int screenY) {
        // Convert Screen -> World
        Vector3 worldPos = viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
        float worldX = worldPos.x / Constants.PPM;
        float worldY = worldPos.y / Constants.PPM;
        
        final Vector2 epicenter = new Vector2(worldX, worldY);
        final float radius = 3.0f;
        final float force = 1000.0f; // Strong impulse

        // For Visuals
        explosionPos.set(worldX * Constants.PPM, worldY * Constants.PPM);
        drawExplosion = true;

        // Query Box2D World
        PhysicsManager.getInstance().getWorld().QueryAABB(fixture -> {
            Body body = fixture.getBody();
            if (body.getType() == BodyDef.BodyType.DynamicBody) {
                Vector2 bodyPos = body.getPosition();
                float dist = bodyPos.dst(epicenter);
                
                if (dist <= radius) {
                    Vector2 direction = bodyPos.cpy().sub(epicenter).nor();
                    // Falloff: Stronger at center
                    float strength = (1 - (dist / radius)) * force;
                    body.applyLinearImpulse(direction.scl(strength), bodyPos, true);
                }
            }
            return true; // Keep querying
        }, worldX - radius, worldY - radius, worldX + radius, worldY + radius);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void spawnDynamicEntity(int screenX, int screenY) {
        // Convert Screen -> World
        Vector3 worldPos = viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
        float worldX = worldPos.x / Constants.PPM;
        float worldY = worldPos.y / Constants.PPM;

        float w = 0.5f; // Meters
        float h = 0.5f;

        BodyDef bdef = new BodyDef();
        bdef.position.set(worldX, worldY);
        bdef.type = BodyDef.BodyType.DynamicBody;
        
        Body body = PhysicsManager.getInstance().getWorld().createBody(bdef);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w / 2, h / 2);
        
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1.0f;
        fdef.friction = 0.5f;
        fdef.restitution = 0.6f;
        fdef.filter.categoryBits = Constants.BIT_PLAYER;
        fdef.filter.maskBits = (short) (Constants.BIT_WALL | Constants.BIT_PLAYER);
        
        body.createFixture(fdef);
        shape.dispose();
        
        DynamicEntity entity = new DynamicEntity(body, w, h);
        EntityManager.getInstance().addEntity(entity);
    }

    @Override
    public void render(float delta) {
        // Clear background
        super.render(delta); // Handles UI stage

        PhysicsManager.getInstance().update(delta);
        EntityManager.getInstance().update(delta);

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeType.Filled);
        EntityManager.getInstance().render(shapeRenderer);
        
        // Draw Explosion (1 Frame)
        if (drawExplosion) {
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.circle(explosionPos.x, explosionPos.y, 50); // 50 pixel radius
            drawExplosion = false;
        }
        
        shapeRenderer.end();

        // Optional Debug Renderer
        // debugRenderer.render(PhysicsManager.getInstance().getWorld(), viewport.getCamera().combined.scl(Constants.PPM));
    }
    
    @Override
    public void dispose() {
        super.dispose();
        debugRenderer.dispose();
    }
}
