package sapphire.imgui.windows;

import diamondEngine.DiaEnvironment;
import diamondEngine.Diamond;
import diamondEngine.diaRenderer.Texture;
import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.Sapphire;
import sapphire.imgui.SappImGUILayer;

public class EnvHierarchyWindow extends ImguiWindow {

    // ATTRIBUTES
    private Diamond currentInstance;

    public EnvHierarchyWindow() {
        super("env_hierarchy", "Environment Hierarchy");
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

        Texture tex = Sapphire.getIcon("add.png");
        ImGui.beginGroup();
        if (ImGui.imageButton(tex.getId(), tex.getWidth(), tex.getHeight())) {
            // Button is clicked
        }
        ImGui.sameLine();
        if (ImGui.button(Sapphire.getLiteral("create_root_env"))) Diamond.get().addEmptyEnvironment();
        ImGui.endGroup();

    }

    private void drawNestedEntities() {

        for (DiaEnvironment env : Diamond.get().getEnvironments()) {
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
        if (ImGui.beginPopupContextItem("env_menu")) {
            if (ImGui.menuItem(Sapphire.getLiteral("create_entity"))) {
                DiaLogger.log("Selected item context menu on '" + Sapphire.getLiteral("create_entity") + "'");
            }
            ImGui.endPopup();
        }
    }
}
