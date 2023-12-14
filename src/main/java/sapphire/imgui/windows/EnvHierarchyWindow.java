package sapphire.imgui.windows;

import diamondEngine.Entity;
import diamondEngine.Environment;
import diamondEngine.Diamond;
import diamondEngine.diaComponents.Sprite;
import diamondEngine.diaComponents.tileMap.TileMap;
import diamondEngine.diaUtils.DiaHierarchyNode;
import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.Sapphire;
import sapphire.eventsSystem.SappEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.SappImGuiUtils;
import sapphire.imgui.SappImGuiLayer;
import sapphire.imgui.widgets.ImageLabelButton;

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
                Object payload = ImGui.acceptDragDropPayload("HierarchyNode");
                ImGui.endDragDropTarget();
            }

            if (isOpen) {
                envContextMenu(env);
                drawEntitiesHierarchy(env);
                ImGui.treePop();
            }
        }
    }

    private void drawEntitiesHierarchy(Environment env) {
        DiaHierarchyNode root = env.getHierarchyRoot();
        for (String childId : root.getChildren()) {
            DiaHierarchyNode childNode = env.getNodes().get(childId);
            drawEntityNode(childNode, env);
        }
    }

    private void drawEntityNode(DiaHierarchyNode node, Environment env) {
        Entity e = node.getEntity();
        if (node.getChildren().isEmpty()) {
            SappImGuiUtils.selectable(e.getUuid(), e.getName(), "entity.png", node);
        } else {
            boolean isEntityOpen = SappImGuiUtils.imageTreeNode(e.getUuid(), e.getName(), "entity.png", node);
            if (isEntityOpen) {
                for (String childId : node.getChildren()) {
                    DiaHierarchyNode childNode = env.getNodes().get(childId);
                    drawEntityNode(childNode, env);
                }
                ImGui.treePop();
            }
        }

        // Drag and drop target
        if (ImGui.beginDragDropTarget()) {
            Object payload = ImGui.acceptDragDropPayload("HierarchyNode");
            DiaLogger.log("" + payload);

            if (payload instanceof DiaHierarchyNode) {
                DiaLogger.log("Nested entity node");
                env.modifyHierarchy(node.getEntity(), ((DiaHierarchyNode) payload).getEntity());
            }
            ImGui.endDragDropTarget();
        }
        entityContextMenu(e);
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
