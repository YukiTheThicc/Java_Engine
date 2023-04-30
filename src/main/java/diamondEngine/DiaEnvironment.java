package diamondEngine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamondEngine.diaComponents.Camera;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaRenderer.Framebuffer;
import diamondEngine.diaUtils.serializers.ComponentSerializer;
import diamondEngine.diaUtils.serializers.EntitySerializer;
import org.joml.Vector2f;
import sapphire.imgui.SappDrawable;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public class DiaEnvironment implements SappDrawable {

    // ATTRIBUTES
    private long uid;
    private DiaEnvironment parent;
    private Framebuffer frame;
    private List<DiaEnvironment> children;
    private List<DiaEntity> entities;
    private List<Component> components;
    private Camera mainCamera;
    private String name;
    private boolean isInitialized;

    // CONSTRUCTORS
    public DiaEnvironment(String name) {
        this.uid = Diamond.genId();
        this.parent = null;
        this.name = name;
        this.frame = new Framebuffer(1600, 900);
        this.isInitialized = false;
        this.mainCamera = new Camera(new Vector2f());
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    public DiaEnvironment(String name, DiaEnvironment parent) {
        this.uid = Diamond.genId();
        this.parent = parent;
        this.name = name;
        this.frame = new Framebuffer(1600, 900);
        this.isInitialized = false;
        this.mainCamera = new Camera(new Vector2f());
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public String getName() {
        return name;
    }

    public List<DiaEnvironment> getChildren() {
        return children;
    }

    public DiaEnvironment getParent() {
        return parent;
    }

    public List<DiaEntity> getEntities() {
        return entities;
    }

    public List<Component> getComponents() {
        return components;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public long getUid() {
        return uid;
    }

    public Framebuffer getFrame() {
        return frame;
    }

    // METHODS
    public void addChild(DiaEnvironment environment) {
        if (environment != null) {
            if (!environment.isInitialized) environment.init();
            children.add(environment);
        }
    }

    public void addComponent(Component component) {
        if (component != null) {
            components.add(component);
        }
    }

    public void addEntity(DiaEntity entity) {
        if (entity != null) {
            entities.add(entity);
        }
    }

    public void init() {
        isInitialized = true;
    }

    public void update(float dt) {

        mainCamera.changeProjection();
        for (DiaEnvironment child : children) child.update(dt);
        for (Component component : components) component.update(dt);
        for (DiaEntity e : entities) {
            e.update(dt);
        }
    }

    public void startFrame() {

        this.frame.bind();
        glClearColor(0.1f, 0.1f, 0.1f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void endFrame() {
        this.frame.unBind();
    }

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(DiaEntity.class, new EntitySerializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            List<DiaEntity> toSerialize = new ArrayList<>();
            for (DiaEntity e : entities) {
                if (e.isToSerialize()) {
                    toSerialize.add(e);
                }
            }
            writer.write(gson.toJson(toSerialize));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(DiaEntity.class, new EntitySerializer())
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {
            long maxEntityId = -1;
            long maxCompId = -1;
            DiaEntity[] entities = gson.fromJson(inFile, DiaEntity[].class);
            for (int i = 0; i < entities.length; i++) {
                addEntity(entities[i]);

                for (Component c : entities[i].getComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }

                if (entities[i].getUid() > maxEntityId) {
                    maxEntityId = entities[i].getUid();
                }
            }

            maxEntityId++;
            maxCompId++;
        }
    }

    public void destroy() {

    }

    @Override
    public void imgui() {

    }

    @Override
    public boolean selectable() {
        return false;
    }
}
