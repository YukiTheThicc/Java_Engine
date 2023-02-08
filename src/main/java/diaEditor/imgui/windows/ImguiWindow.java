package diaEditor.imgui.windows;

public abstract class ImguiWindow {

    private static int WID_COUNTER = 0;
    private boolean isActive;
    private int wid = -1;

    // GETTERS & SETTERS
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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
