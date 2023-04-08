package sapphire.imgui.windows;

import diamondEngine.DiaEnvironment;
import diamondEngine.Diamond;
import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.Sapphire;
import sapphire.SapphireEvents;
import sapphire.events.SappEvent;
import sapphire.events.SappEventType;
import sapphire.imgui.SappImGUILayer;
import sapphire.imgui.components.SappImageButton;

public class EnvHierarchyWindow extends ImguiWindow {

    // ATTRIBUTES
    private final SappImageButton newRootEnvButton;

    public EnvHierarchyWindow() {
        super("env_hierarchy", "Environment Hierarchy");
        this.newRootEnvButton = new SappImageButton(Sapphire.getIcon("2d.png"), Sapphire.getLiteral("create_root_env"));
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        ImGui.begin(this.getTitle(), this.getFlags());
        mainContextMenu();

        if (Diamond.get().getEnvironments().isEmpty()) {
            drawEmptyEnvsPrompt();
        } else {
            drawNestedEntities();
        }

        ImGui.end();
    }

    private void drawEmptyEnvsPrompt() {
        if (newRootEnvButton.draw()) SapphireEvents.notify(new SappEvent(SappEventType.New_root_env));
    }

    private void drawNestedEntities() {

        for (DiaEnvironment env : Diamond.get().getEnvironments()) {
            itemContextMenu();
            if (ImGui.treeNode(env.getName())) {

                ImGui.treePop();
            }
        }
    }

    private void mainContextMenu() {
        if (ImGui.beginPopupContextItem("env_menu")) {
            if (ImGui.menuItem(Sapphire.getLiteral("create_env"))) {
               DiaLogger.log("Selected env context menu on '" + Sapphire.getLiteral("create_env") + "'");
            }
            ImGui.endPopup();
        }

    }

    private void itemContextMenu() {
        if (ImGui.beginPopupContextItem("env_item")) {
            if (ImGui.menuItem(Sapphire.getLiteral("create_entity"))) {
                DiaLogger.log("Selected item context menu on '" + Sapphire.getLiteral("create_entity") + "'");
            }
            ImGui.endPopup();
        }
    }
}
