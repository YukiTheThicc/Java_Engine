package sapphire.imgui.windows;

import diamondEngine.DiaEntity;
import diamondEngine.DiaEnvironment;
import diamondEngine.Diamond;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaComponents.Grid;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.Sapphire;
import sapphire.SapphireEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.components.SappImageLabelButton;

public class EnvHierarchyWindow extends ImguiWindow {

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

        for (DiaEnvironment env : Diamond.get().getEnvironments()) {
            if (ImGui.treeNode(env.getName())) {

                itemContextMenu(env);
                if (!env.getEntities().isEmpty() && ImGui.treeNode("Entities")) {
                    for (DiaEntity entity : env.getEntities()) {
                        ImGui.text(entity.toString());
                    }
                    ImGui.treePop();
                }

                for (Component component : env.getComponents()) {
                    if (component.selectable()) SapphireEvents.notify(new SappEvent(SappEventType.Selected_object, component));
                }

                ImGui.treePop();
            }
        }
    }

    private void mainContextMenu() {
        if (ImGui.beginPopupContextItem("env_menu")) {
            if (ImGui.menuItem(Sapphire.getLiteral("create_env"))) SapphireEvents.notify(new SappEvent(SappEventType.Add_env));
            ImGui.endPopup();
        }
    }

    private void itemContextMenu(DiaEnvironment env) {
        if (ImGui.beginPopupContextItem(env.getName() + "env_item")) {
            if (ImGui.menuItem(Sapphire.getLiteral("add_grid"))) SapphireEvents.notify(
                    new SappEvent(SappEventType.Add_component, env, new Grid(1)));
            ImGui.endPopup();
        }
    }


}
