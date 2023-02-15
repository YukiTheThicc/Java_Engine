package diaEditor.imgui.windows;

import imgui.type.ImBoolean;

public abstract class ImguiWindow {

    // ATTRIBUTES
    private String id;
    private String title;
    private ImBoolean isActive;

    // CONSTRUCTORS
    public ImguiWindow(String id, String title) {
        this.id = id;
        this.title = title;
        this.isActive = new ImBoolean(false);
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

    // METHODS
    public abstract void imgui();
}
