package sapphire.imgui.windows;

import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import sapphire.imgui.SappImGUILayer;

public abstract class ImguiWindow {

    private static final float DEFAULT_SIZE_X = 400f;
    private static final float DEFAULT_SIZE_Y = 400f;

    // ATTRIBUTES
    private float sizeX;
    private float sizeY;
    private String id;
    private String title;
    private ImBoolean isActive;
    private boolean shouldClose;
    private int flags;
    private final boolean isConfigurable;

    // CONSTRUCTORS

    /**
     * Creates an ImGUI window that can be added to the layer. Window flags are set to None by default.
     * @param id ID of the new window.
     * @param title Title of the window. The String that appears on the title bar.
     * @param isConfigurable If the window should be configurable by the user. If false the window shouldn't appear in
     *                       the list of active windows.
     */
    public ImguiWindow(String id, String title, boolean isConfigurable) {
        this.id = id;
        this.title = title;
        this.isActive = new ImBoolean(false);
        this.shouldClose = false;
        this.flags = ImGuiWindowFlags.None;
        this.isConfigurable = isConfigurable;
        this.sizeX = DEFAULT_SIZE_X;
        this.sizeY = DEFAULT_SIZE_Y;
    }

    /**
     * Creates an ImGUI window that can be added to the layer. The window is set to be configurable by default.
     * Window flags are set to None by default.
     * @param id ID of the new window.
     * @param title Title of the window. The String that appears on the title bar.
     */
    public ImguiWindow(String id, String title) {
        this.id = id;
        this.title = title;
        this.isActive = new ImBoolean(false);
        this.shouldClose = false;
        this.flags = ImGuiWindowFlags.None;
        this.isConfigurable = true;
        this.sizeX = DEFAULT_SIZE_X;
        this.sizeY = DEFAULT_SIZE_Y;
    }

    // GETTERS & SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImBoolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive.set(active);
    }

    public boolean shouldClose() {
        return shouldClose;
    }

    /**
     * Sets if the window should close. Sapphire will store all windows that should close in an Array after they are
     * updated, and then proceed to remove them. In this way, if a window has the capability of close itself, its always
     * removed safely.
     * @param close
     */
    public void close(boolean close) {
        this.shouldClose = close;
    }

    public boolean isConfigurable() {
        return isConfigurable;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public float getSizeX() {
        return sizeX;
    }

    public void setSizeX(float sizeX) {
        this.sizeX = sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public void setSizeY(float sizeY) {
        this.sizeY = sizeY;
    }

    // METHODS
    public abstract void imgui(SappImGUILayer layer);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ImguiWindow)) return false;
        ImguiWindow window = (ImguiWindow) obj;
        return this.id.equals(window.getId());
    }
}
