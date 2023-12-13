package diamondEngine.diaComponents;

import diamondEngine.Entity;
import imgui.ImGui;

public interface Component {

    /**
     * !!! REVISE !!! It may be better to re-think this approach. To allow components to work properly WITHIN the current
     * serialization system, it is necessary to implement an init function that initializes transient attributes as they
     * are not serialized. Of course this is only necessary when the component contains transient attributes.
     */
    void init();

    void update(float dt);

    Component copy();

    void destroy();
}
