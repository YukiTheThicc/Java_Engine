package sapphire.imgui.windows;

import diamondEngine.Entity;
import diamondEngine.Environment;
import diamondEngine.Diamond;
import diamondEngine.diaAssets.Texture;
import diamondEngine.diaComponents.Sprite;
import diamondEngine.diaComponents.tiles.TileMap;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiTreeNodeFlags;
import sapphire.Sapphire;
import sapphire.eventsSystem.SappEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.SappImGuiUtils;
import sapphire.imgui.SappImGuiLayer;
import sapphire.imgui.widgets.ImageLabelButton;

import java.util.List;

public class EnvHierarchyWindow extends ImguiWindow {

    // ATTRIBUTES
    private final ImageLabelButton newRootEnvButton;

    public EnvHierarchyWindow() {
        super("env_hierarchy", "Environment Hierarchy");
        this.newRootEnvButton = new ImageLabelButton(Sapphire.getIcon("2d.png"), Sapphire.getLiteral("create_root_env"), 40f, 40f);
    }

    @Override
    public void imgui(SappImGuiLayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        if (ImGui.begin(this.getTitle(), this.getFlags())) {
            mainContextMenu();

            if (Diamond.get().getEnvironments().isEmpty()) {
                drawEmptyEnvsPrompt();
            } else {
                drawNestedEnvironments();
            }
        }
        ImGui.end();
    }

    private void drawEmptyEnvsPrompt() {
        if (newRootEnvButton.draw()) SappEvents.notify(new SappEvent(SappEventType.New_root_env));
    }

    private void drawNestedEnvironments() {

        for (Environment env : Diamond.get().getEnvironments()) {
            boolean isOpen = SappImGuiUtils.imageTreeNode(env.getUuid(), (char) 0xe000 + " " + env.getName() + (env.isModified() ? " *" : ""),
                    "sapp.png", env);

            // Drag and drop target
            if (ImGui.beginDragDropTarget()) {
                Object payload = ImGui.acceptDragDropPayload("Selectable");
                if (payload instanceof Entity) {
                    env.addEntity((Entity) payload);
                }
                ImGui.endDragDropTarget();
            }

            if (isOpen) {
                envContextMenu(env);
                drawEntities(env.getEntities());
                ImGui.treePop();
            }
        }
    }

    private void drawEntities(List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getNestedEntities().size() == 0) {
                if (SappImGuiUtils.selectable(entity.getUuid(), entity.getName(), "entity.png", entity)) {
                    SappEvents.notify(new SappEvent(SappEventType.Selected_object, null, null, entity));
                }
            } else {
                if (SappImGuiUtils.imageTreeNode(entity.getUuid(), entity.getName(), "entity.png", entity)) {
                    drawEntities(entity.getNestedEntities());
                    ImGui.treePop();
                }
            }

            // Drag and drop target
            if (ImGui.beginDragDropTarget()) {
                Object payload = ImGui.acceptDragDropPayload("Selectable");
                if (payload instanceof Entity) {
                    entity.addNestedEntity((Entity) payload);
                }
                ImGui.endDragDropTarget();
            }

            entityContextMenu(entity);
        }
    }

    private void mainContextMenu() {
        if (ImGui.beginPopupContextItem("env_menu")) {
            if (ImGui.menuItem(Sapphire.getLiteral("create_env")))
                SappEvents.notify(new SappEvent(SappEventType.Add_env));
            ImGui.endPopup();
        }
    }

    /**
     * Context menu for an environment
     *
     * @param env Environment for which the menu is being drawn
     */
    private void envContextMenu(Environment env) {
        if (ImGui.beginPopupContextItem(env.getUuid())) {

            if (ImGui.menuItem(Sapphire.getLiteral("make_current"))) SappEvents.notify(
                    new SappEvent(SappEventType.Make_current, env));
            ImGui.separator();

            if (ImGui.menuItem(Sapphire.getLiteral("add_entity"))) SappEvents.notify(
                    new SappEvent(SappEventType.Add_object, env, new Entity()));
            ImGui.separator();

            if (ImGui.menuItem(Sapphire.getLiteral("remove"))) SappEvents.notify(
                    new SappEvent(SappEventType.Delete_object, env));
            ImGui.endPopup();
        }
    }

    /**
     * Context menu for an entity
     *
     * @param e Entity
     */
    private void entityContextMenu(Entity e) {
        if (ImGui.beginPopupContextItem(e.getUuid())) {

            // Component addition options
            if (ImGui.menuItem(Sapphire.getLiteral("add_tile_map"))) SappEvents.notify(
                    new SappEvent(SappEventType.Add_object, null, e, new TileMap(32)));
            if (ImGui.menuItem(Sapphire.getLiteral("add_sprite_renderer"))) SappEvents.notify(
                    new SappEvent(SappEventType.Add_object, null, e, new Sprite()));
            if (ImGui.menuItem(Sapphire.getLiteral("add_text_renderer"))) SappEvents.notify(
                    new SappEvent(SappEventType.Add_object, null, e, new Sprite()));
            ImGui.separator();

            if (ImGui.menuItem(Sapphire.getLiteral("copy"))) SappEvents.notify(
                    new SappEvent(SappEventType.Copy_object, e.getEnv(), e));
            if (ImGui.menuItem(Sapphire.getLiteral("delete"))) SappEvents.notify(
                    new SappEvent(SappEventType.Delete_object, null, e));
            ImGui.endPopup();
        }
    }
}
