package diaEditor.imgui.windows;

import imgui.type.ImBoolean;

public abstract class ImguiWindow {

    // ATTRIBUTES
    private static int WID_COUNTER = 0;
    private String title;
    private ImBoolean isActive;
    private int wid = -1;

    // CONSTRUCTORS
    public ImguiWindow(String title) {
        generateId();
        this.title = title;
        this.isActive = new ImBoolean(false);
    }

    // GETTERS & SETTERS
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

    public int getWid() {
        return wid;
    }

    // METHODS
    public void generateId() {
        if (wid == -1) {
            this.wid = WID_COUNTER++;
        }
    }

    public abstract void imgui();
}
