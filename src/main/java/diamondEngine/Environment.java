package diamondEngine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamondEngine.diaComponents.Camera;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaRenderer.Framebuffer;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaMath;
import diamondEngine.diaUtils.serializers.ComponentSerializer;
import diamondEngine.diaUtils.serializers.EntitySerializer;
import imgui.ImGui;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector2i;
import sapphire.Sapphire;
import sapphire.SapphireProject;
import sapphire.imgui.SappDrawable;
import sapphire.imgui.SappImGui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Environment implements SappDrawable {

    // CONSTANTS
    public static final String ENVS_EXT = ".denv";
    public static final int DEFAULT_FRAME_X = 480;
    public static final int DEFAULT_FRAME_Y = 270;

    // ATTRIBUTES
    private String parent;
    private Camera mainCamera;
    private final List<Environment> children;
    private final List<Entity> entities;
    private final List<Component> components;
    private String name;
    private transient List<Entity> entitiesToRemove;
    private transient List<Component> componentsToRemove;
    private transient Framebuffer frame;
    private int frameX;
    private int frameY;
    private boolean isInitialized;
    private transient long uid;
    private transient boolean isDirty = false;
    private transient boolean toRemove = false;

    // CONSTRUCTORS
    public Environment(String name) {
        this.uid = Diamond.genId();
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.parent = null;
        this.name = name;
        this.isInitialized = false;
        Vector2i pm = DiaMath.getFractionFromFloat((float) frameX / frameY);
        this.mainCamera = new Camera(new Vector2f(), pm.x, pm.y);
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.components = new ArrayList<>();
        this.entitiesToRemove = new ArrayList<>();
        this.componentsToRemove = new ArrayList<>();
    }

    public Environment(String name, String parent) {
        this.uid = Diamond.genId();
        this.frameX = DEFAULT_FRAME_X;
        this.frameY = DEFAULT_FRAME_Y;
        this.parent = parent;
        this.name = name;
        this.isInitialized = false;
        Vector2i pm = DiaMath.getFractionFromFloat((float) frameX / frameY);
        this.mainCamera = new Camera(new Vector2f(), pm.x, pm.y);
        this.children = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.components = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public String getName() {
        return name;
    }

    public List<Environment> getChildren() {
        return children;
    }

    public String getParent() {
        return parent;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Component> getComponents() {
        return components;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public Framebuffer getFrame() {
        return frame;
    }

    public int getFrameX() {
        return frameX;
    }

    public int getFrameY() {
        return frameY;
    }

    public void setDirty() {
        isDirty = true;
    }

    public void setToRemove() {
        this.toRemove = true;
    }

    public boolean isToRemove() {
        return toRemove;
    }

    // METHODS
    public void init() {
        frame = new Framebuffer(frameX, frameY);
        isInitialized = true;
    }

    public void addChild(Environment environment) {
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


    public void removeComponent(Component component) {
        if (component != null) {
            componentsToRemove.add(component);
            this.isDirty = true;
        }
    }

    public void addEntity(Entity entity) {
        if (entity != null) {
            entities.add(entity);
        }
    }

    public void removeEntity(Entity entity) {
        if (entity != null) {
            this.entitiesToRemove.add(entity);
            this.isDirty = true;
        }
    }

    public void changeFrame(int frameX, int frameY) {
        this.frameX = frameX;
        this.frameY = frameY;
        frame.destroy();
        frame = new Framebuffer(frameX, frameY);
    }

    public float getAspectRatio() {
        return (float) frameX / frameY;
    }

    private boolean changeName(String path, String newName) {
        File file = new File(path + "/" + name + ENVS_EXT);
        File newFile = new File(path + "/" + newName + ENVS_EXT);

        if (!newFile.exists()) {
            if (file.exists()) {
                if (!newName.isEmpty()) {
                    if (file.renameTo(newFile)) {
                        DiaLogger.log(this.getClass(), "Changed environment name from '" + name + "' to '" + newName + "'", DiaLoggerLevel.INFO);
                        name = newName;
                    } else {
                        DiaLogger.log(this.getClass(), "Failed to rename environment '" + name + "'", DiaLoggerLevel.ERROR);
                    }
                }
            } else {
                DiaLogger.log(this.getClass(), "Couldn't find file for environment '" + name + "'", DiaLoggerLevel.ERROR);
            }
        } else {
            return true;
        }
        return false;
    }

    public void startFrame() {

        this.frame.bind();
        glClearColor(0.1f, 0.1f, 0.1f, 1f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void endFrame() {
        this.frame.unBind();
    }

    public void update(float dt) {

        if (isDirty) {
            for (Entity e : entitiesToRemove) {
                entities.remove(e);
            }
            for (Component c : componentsToRemove) {
                components.remove(c);
            }
            entitiesToRemove.clear();
            components.clear();
            isDirty = false;
        }
        mainCamera.changeProjection();
        for (Environment child : children) child.update(dt);
        for (Component component : components) component.update(dt);
        for (Entity e : entities) {
            e.update(dt);
        }
    }

    public void save(String path) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(Entity.class, new EntitySerializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            Files.createDirectories(Paths.get(path));
            FileWriter writer = new FileWriter(path + "/" + name + ENVS_EXT);
            writer.write(gson.toJson(this));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String path) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .registerTypeAdapter(Entity.class, new EntitySerializer())
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get(path + "/" + name + ENVS_EXT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {
            long maxEntityId = -1;
            long maxCompId = -1;
            Entity[] entities = gson.fromJson(inFile, Entity[].class);
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
        }
    }

    public void destroy() {

    }

    @Override
    public void imgui() {
        SappImGui.textLabel("Framebuffer", "" + frame.getFboId());
        ImString newName = new ImString(name, 256);
        if (SappImGui.inputText(Sapphire.getLiteral("name"), newName)) {
            if (Sapphire.get().getProject() != null) {
                boolean fileExists = changeName(
                        Sapphire.get().getProject().getRoot().getPath().getAbsolutePath() + "/" + SapphireProject.ENVS_DIR,
                        newName.get()
                );
                if (fileExists) {
                    float imgSize = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2;
                    ImGui.sameLine();
                    ImGui.image(Sapphire.getIcon("info.png").getId(), imgSize, imgSize, 0, 1, 1, 0);
                    if (ImGui.isItemHovered()) ImGui.setTooltip(Sapphire.getLiteral("file_already_exists"));
                }
            }
        }

        ImInt newWidth = new ImInt(frameX);
        if (SappImGui.inputInt(Sapphire.getLiteral("frame_width"), newWidth)) changeFrame(newWidth.get(), frameY);
        ImInt newHeight = new ImInt(frameY);
        if (SappImGui.inputInt(Sapphire.getLiteral("frame_height"), newHeight)) changeFrame(frameX, newHeight.get());

        ImGui.text(Sapphire.getLiteral("camera"));
        ImGui.separator();
        ImFloat zoom = new ImFloat(mainCamera.getZoom());
        if (SappImGui.dragFloat(Sapphire.getLiteral("zoom"), zoom)) mainCamera.setZoom(zoom.get());
        SappImGui.drawVec2Control(Sapphire.getLiteral("position"), mainCamera.pos);
    }

    @Override
    public boolean selectable() {
        return false;
    }
}
