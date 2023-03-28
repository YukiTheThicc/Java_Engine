package sapphire.imgui.windows;

import diamondEngine.diaUtils.DiaLogger;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import sapphire.Sapphire;
import sapphire.imgui.SappImGUILayer;

public class EnvHierarchyWindow extends ImguiWindow {

    public EnvHierarchyWindow() {
        super("env_hierarchy", "Environment Hierarchy");
    }

    @Override
    public void imgui(SappImGUILayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        ImGui.begin(this.getTitle(), this.getFlags());
        mainContextMenu();

        /*if (Diamond) {

        }*/

        ImGui.end();
    }

    private void drawNestedEntities() {

        /*if () {

        }*/
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
