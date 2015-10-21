package com.gdxcreature;

import MeshBoneUtil.Creature;
import MeshBoneUtil.CreatureAnimation;
import MeshBoneUtil.CreatureManager;
import MeshBoneUtil.CreatureModuleUtils;
import MeshBoneUtil.CreatureRenderer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.JsonValue;

public class GdxCreatureGame extends ApplicationAdapter {
    OrthographicCamera camera;
    SpriteBatch batch;
    CreatureManager active_creature_manager;
    CreatureRenderer active_creature_render;

    @Override
    public void create() {
        camera = new OrthographicCamera((float) Gdx.graphics.getWidth() * 0.05f,
                (float) Gdx.graphics.getHeight() * 0.05f);
        batch = new SpriteBatch();

        System.out.println("Loading json...");
        // Load json
        JsonValue json_data = CreatureModuleUtils.LoadCreatureJSONData("Ue4_export/character_raptor_data.json");

        System.out.println("Loading creature...");
        // Create creature
        Creature new_creature = new Creature(json_data);

        System.out.println("Loading animations...");
        // Create animations
        CreatureAnimation new_animation1 = new CreatureAnimation(json_data, "default");

        System.out.println("Loading creature manager...");
        // Create manager and add in animations
        CreatureManager new_creature_manager = new CreatureManager(new_creature);
        new_creature_manager.AddAnimation(new_animation1);

        // If you want to use a custom time range
        /*
        new_creature_manager.SetUseCustomTimeRane(true);
        new_creature_manager.SetCustomTimeRange(10, 20);
        */

        new_creature_manager.SetActiveAnimationName("default", false);
        new_creature_manager.SetIsPlaying(true);
        new_creature_manager.SetShouldLoop(true);

        active_creature_manager = new_creature_manager;

        // load shaders
        final String VERTEX = Gdx.files.internal("MeshBoneShader.vert").readString();
        final String FRAGMENT = Gdx.files.internal("MeshBoneShader.frag").readString();
        ShaderProgram program = new ShaderProgram(VERTEX, FRAGMENT);

        Texture new_texture = new Texture(Gdx.files.internal("Ue4_export/character_raptor_img.tga"));

        // Create the creature render object
        active_creature_render = new CreatureRenderer(new_creature_manager,
                new_texture,
                program,
                camera);

        // Set a transformation matrix for the creature
        Matrix4 xform = new Matrix4();
        xform.scl(.5f);
        active_creature_render.SetXform(xform);
    }

    @Override
    public void render() {
        //Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();

        camera.update();
        active_creature_manager.Update(Gdx.graphics.getDeltaTime());

        active_creature_render.Flush();
    }

}