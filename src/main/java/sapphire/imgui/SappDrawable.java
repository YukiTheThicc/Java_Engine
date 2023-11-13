package sapphire.imgui;

/**
 * Interface for diamond objects that can be selected as active items and inspected on the inspector window
 */
public interface SappDrawable {

    /**
     * Should draw the inspector window view of the item
     */
    void imgui();

    /**
     * Should draw a button that allows the user to select this item as the current active object
     * @return If the button has been clicked
     */
    boolean selectable();
}