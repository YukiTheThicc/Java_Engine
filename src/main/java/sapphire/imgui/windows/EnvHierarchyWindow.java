package sapphire.imgui.windows;

import diamondEngine.Entity;
import diamondEngine.Environment;
import diamondEngine.Diamond;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaComponents.Grid;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiTreeNodeFlags;
import sapphire.Sapphire;
import sapphire.SapphireEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.components.SappImageLabelButton;

public class EnvHierarchyWindow extends ImguiWindow {

    private final int NODE_FLAGS = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.Selected |
            ImGuiTreeNodeFlags.OpenOnArrow |ImGuiTreeNodeFlags.OpenOnDoubleClick;

    // ATTRIBUTES
    private final SappImageLabelButton newRootEnvButton;

    public EnvHierarchyWindow() {
        super("env_hierarchy", "Environment Hierarchy");
        this.newRootEnvButton = new SappImageLabelButton(Sapphire.getIcon("2d.png"), Sapphire.getLiteral("create_root_env"), 40f, 40f);
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        if (ImGui.begin(this.getTitle(), this.getFlags())) {
            mainContextMenu();

            if (Diamond.get().getEnvironments().isEmpty()) {
                drawEmptyEnvsPrompt();
            } else {
                drawNestedEntities();
            }
        }

        ImGui.end();
    }

    private void drawEmptyEnvsPrompt() {
        if (newRootEnvButton.draw()) SapphireEvents.notify(new SappEvent(SappEventType.New_root_env));
    }

    private void drawNestedEntities() {

        for (Environment env : Diamond.get().getEnvironments()) {
            ImGui.pushID(env.getUid());

            if (ImGui.treeNodeEx(env.getName() + (env.isModified() ? " *" : ""), NODE_FLAGS)) {
                if (ImGui.isItemClicked()) {
                    SapphireEvents.notify(new SappEvent(SappEventType.Selected_object, null, env));
                }
                envContextMenu(env);
                drawEntities(env);
                drawComponents(env);
                ImGui.treePop();
            }
            ImGui.popID();
        }
    }

    private void drawComponents(Environment env) {
        for (Component component : env.getComponents()) {
            if (component.selectable()) {
                SapphireEvents.notify(new SappEvent(SappEventType.Selected_object, component));
            }
            componentContextMenu(env, component);
        }
    }

    private void drawEntities(Environment env) {
        if (!env.getEntities().isEmpty() && ImGui.treeNode("Entities")) {
            for (Entity entity : env.getEntities()) {
                ImGui.text(entity.toString());
            }
            ImGui.treePop();
        }
    }

    private void mainContextMenu() {
        if (ImGui.beginPopupContextItem("env_menu")) {
            if (ImGui.menuItem(Sapphire.getLiteral("create_env"))) SapphireEvents.notify(new SappEvent(SappEventType.Add_env));
            ImGui.endPopup();
        }
    }

    private void envContextMenu(Environment env) {
        if (ImGui.beginPopupContextItem(env.getName() + "env_item")) {

            if (ImGui.menuItem(Sapphire.getLiteral("add_grid"))) SapphireEvents.notify(
                    new SappEvent(SappEventType.Add_component, env, new Grid(32)));
            ImGui.separator();

            if (ImGui.menuItem(Sapphire.getLiteral("delete"))) SapphireEvents.notify(
                new SappEvent(SappEventType.Delete_object, env));
            ImGui.endPopup();
        }
    }

    private void entityContextMenu(Environment env) {
        if (ImGui.beginPopupContextItem(env.getName() + "entity_item")) {
            if (ImGui.menuItem(Sapphire.getLiteral("add_grid"))) SapphireEvents.notify(
                    new SappEvent(SappEventType.Add_component, env, new Grid(32)));
            ImGui.endPopup();
        }
    }

    private void componentContextMenu(Environment env, Component component) {
        if (ImGui.beginPopupContextItem("entity_item")) {
            if (ImGui.menuItem(Sapphire.getLiteral("delete"))) SapphireEvents.notify(
                    new SappEvent(SappEventType.Delete_object, env, component));
            ImGui.endPopup();
        }
    }
}
