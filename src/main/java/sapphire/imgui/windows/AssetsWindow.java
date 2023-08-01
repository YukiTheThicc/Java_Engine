package sapphire.imgui.windows;

import diamondEngine.Template;
import diamondEngine.diaAudio.Sound;
import diamondEngine.diaEvents.DiaEvent;
import diamondEngine.diaEvents.DiaEvents;
import diamondEngine.diaEvents.DiaObserver;
import diamondEngine.diaRenderer.Shader;
import diamondEngine.diaRenderer.Texture;
import diamondEngine.diaUtils.DiaAssetManager;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import sapphire.Sapphire;
import sapphire.SappEvents;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappEventType;
import sapphire.imgui.SappImGuiLayer;
import sapphire.imgui.SappImGui;
import sapphire.imgui.components.AssetImageButton;
import sapphire.imgui.components.ImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Asset window class. Holds the data structures and functionalities to store all the necessary data to render the
 * buttons for available assets. To avoid a high volume of queries for retrieving the currently available assets each
 * frame, each time an asset is loaded an event is dispatched so classes that need to update their state can do so only
 * when strictly necessary. Implements the DiaObject interface so it can be notified.
 */
public class AssetsWindow extends ImguiWindow implements DiaObserver {

    // ATTRIBUTES
    private float toolbarWidth;
    private final ImageButton addAssetButton;
    private final ImageButton removeAssetButton;
    private final ImageButton copyAssetButton;
    private final List<AssetImageButton> shaderButtons;
    private final List<AssetImageButton> textureButtons;
    private final List<AssetImageButton> soundButtons;
    private final List<AssetImageButton> templateButtons;

    // CONSTRUCTORS
    public AssetsWindow() {
        super("assets", "Assets");
        this.toolbarWidth = SappImGui.SMALL_ICON_SIZE + ImGui.getStyle().getFramePaddingX() * 4;
        this.addAssetButton = new ImageButton(Sapphire.getIcon("add.png"), SappImGui.SMALL_ICON_SIZE, SappImGui.SMALL_ICON_SIZE);
        this.removeAssetButton = new ImageButton(Sapphire.getIcon("trash.png"), SappImGui.SMALL_ICON_SIZE, SappImGui.SMALL_ICON_SIZE);
        this.copyAssetButton = new ImageButton(Sapphire.getIcon("copy.png"), SappImGui.SMALL_ICON_SIZE, SappImGui.SMALL_ICON_SIZE);
        this.shaderButtons = new ArrayList<>();
        this.textureButtons = new ArrayList<>();
        this.soundButtons = new ArrayList<>();
        this.templateButtons = new ArrayList<>();
        DiaEvents.addObserver(this);
        getFirstElements();
    }

    // METHODS
    @Override
    public void imgui(SappImGuiLayer layer) {

        ImGui.setNextWindowSize(400f, 400f, ImGuiCond.FirstUseEver);
        if (ImGui.begin(this.getTitle(), this.getFlags())) {
            ImGui.columns(2);
            ImGui.setColumnWidth(0, toolbarWidth);
            toolbar();
            ImGui.nextColumn();
            if (ImGui.beginTabBar(this.getTitle(), this.getFlags())) {
                drawTab(Sapphire.getLiteral("textures"), textureButtons);
                drawTab(Sapphire.getLiteral("sounds"), soundButtons);
                drawTab(Sapphire.getLiteral("shaders"), shaderButtons);
                drawTab(Sapphire.getLiteral("templates"), templateButtons);
                ImGui.endTabBar();
            }
        }

        ImGui.end();
    }

    /**
     * Renders the toolbar for the asset window
     */
    private void toolbar() {
        ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
        if (addAssetButton.draw()) SappEvents.notify(new SappEvent(SappEventType.Add_asset));
        if (removeAssetButton.draw());
        //if (copyAssetButton.draw());
        ImGui.popStyleColor(1);
    }

    /**
     * Draws an arbitrary assets tab for the asset window
     * @param tabLabel Label for the asset tab
     * @param assets List of assets to bhe drawn inside the tab
     */
    private void drawTab(String tabLabel, List<AssetImageButton> assets) {
        if (assets != null && !assets.isEmpty() && ImGui.beginTabItem(tabLabel)) {
            for (AssetImageButton b : assets) {
                b.draw();
                if (ImGui.getContentRegionAvailX() < (float) b.getButtonSize() - b.getPaddingX()) {
                    ImGui.newLine();
                    ImGui.setCursorPos(ImGui.getCursorPosX(), ImGui.getCursorPosY() + b.getButtonSize());
                }
            }
            ImGui.endTabItem();
        }
    }

    /**
     * Queries the first assets available when creating the window. Should be called when creating the window or changing
     * the project
     */
    private void getFirstElements() {
        for (Texture t : DiaAssetManager.getAllTextures()) {
            textureButtons.add(new AssetImageButton(t));
        }

        for (Sound so : DiaAssetManager.getAllSounds()) {
            File tmp = new File(so.getPath());
            soundButtons.add(new AssetImageButton(so.getPath(), tmp.getName(), Sapphire.getIcon("sfx.png")));
        }
    }

    @Override
    public void onNotify(DiaEvent event) {
        switch(event.type) {
            case ASSET_ADDED:
                if (event.payload instanceof Shader) {

                } else if (event.payload instanceof Texture) {
                    textureButtons.add(new AssetImageButton((Texture) event.payload));
                } else if (event.payload instanceof Sound) {
                    Sound newSound = (Sound) event.payload;
                    File tmp = new File(newSound.getPath());
                    soundButtons.add(new AssetImageButton(newSound.getPath(), tmp.getName(), Sapphire.getIcon("sfx.png")));
                } else if (event.payload instanceof Template) {

                }
                break;
            case ASSET_REMOVED:
                break;
        }
    }
}
