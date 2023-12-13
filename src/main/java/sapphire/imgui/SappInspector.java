package sapphire.imgui;

import diamondEngine.Entity;
import diamondEngine.Environment;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaUtils.DiaHierarchyNode;
import diamondEngine.diaUtils.DiaUtils;
import imgui.ImGui;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import sapphire.Sapphire;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class SappInspector {

    /**
     * Calls for the inspection method for the specific object class. If there is none specific inspection method, the
     * name of the class of the object is shown.
     * @param o Object to be inspected
     */
    public static void inspect(Object o) {
        if (o instanceof Environment) {
            inspectEnvironment((Environment) o);
        } else if (o instanceof Entity) {
            inspectEntity((Entity) o);
        } else {
            ImGui.text(o.getClass().getSimpleName());
        }
    }

    private static void inspectEnvironment(Environment e) {
        SappImGuiUtils.textLabel("UUID", e.getUuid());
        SappImGuiUtils.textLabel("Framebuffer", "" + e.getFrame().getFboId());
        ImString newName = new ImString(e.getName(), 256);
        if (SappImGuiUtils.inputText(Sapphire.getLiteral("name"), newName)) {
            if (Sapphire.get().getProject() != null && !newName.isEmpty()) {
                e.setName(newName.get());
                e.setModified();
            }
        }

        ImInt newWidth = new ImInt(e.getFrameX());
        if (SappImGuiUtils.inputInt(Sapphire.getLiteral("frame_width"), newWidth)) {
            e.changeFrame(newWidth.get(), e.getFrameY());
            e.setModified();
        }
        ImInt newHeight = new ImInt(e.getFrameY());
        if (SappImGuiUtils.inputInt(Sapphire.getLiteral("frame_height"), newHeight)) {
            e.changeFrame(e.getFrameX(), newHeight.get());
            e.setModified();
        }
        ImGui.separator();

        // UID System INFO
        if (ImGui.beginTable("Objects", 2 , ImGuiTableFlags.Borders)) {

            ImGui.tableSetupColumn("ID");
            ImGui.tableSetupColumn("Object");
            ImGui.tableHeadersRow();
            for (DiaHierarchyNode node : e.getHierarchyNodes().values()) {
                ImGui.tableNextColumn();
                ImGui.text("" + node.getEntity().getUuid());
                ImGui.tableNextColumn();
                ImGui.text(node.getEntity().getClass().getSimpleName());
            }
        }
        ImGui.endTable();

        /*
        ImGui.text("winSizeAdjustX: " + winSizeAdjustX);
        ImGui.text("winSizeAdjustY: " + winSizeAdjustY);
        ImGui.text(Window.getWidth() + " / " + Window.getHeight());
        ImGui.text((float) Window.getWidth() / frameX + " / " + (float) Window.getHeight() / frameY);
        ImGui.text("Ratio: " + getRatio());
        */
    }

    private static void inspectEntity(Entity e) {
        SappImGuiUtils.textLabel("UUID", e.getUuid());
        ImString newName = new ImString(e.getName(), 256);
        if (SappImGuiUtils.inputText(Sapphire.getLiteral("name"), newName)) {
            if (Sapphire.get().getProject() != null && !newName.isEmpty()) {
                e.setName(newName.get());
                //this.getEnv().setModified();
            }
        }

        for (Component c : e.getComponents()) {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName())) {
                inspectComponent(c);
            }
        }
    }

    private static void inspectComponent(Component c) {
        try {
            Field[] fields = c.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient) {
                    continue;
                }

                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate) {
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(c);
                String name = field.getName();

                if (type == int.class) {
                    ImInt val = new ImInt((int) value);
                    if (SappImGuiUtils.dragInt(name, val)) field.set(c, val.get());
                } else if (type == float.class) {
                    ImFloat val = new ImFloat((float) value);
                    if (SappImGuiUtils.dragFloat(name, val)) field.set(c, val.get());
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    if (ImGui.checkbox(name + ": ", val)) {
                        field.set(c, !val);
                    }
                } else if (type == Vector2f.class) {
                    Vector2f val = (Vector2f) value;
                    SappImGuiUtils.drawVec2Control(name, val);
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                } else if (type.isEnum()) {
                    String[] enumValues = DiaUtils.getEnumValues(type);
                    String enumType = ((Enum<?>)value).name();
                    ImInt index = new ImInt(DiaUtils.indexOf(enumType, enumValues));
                    if (ImGui.combo(field.getName(), index, enumValues, enumValues.length)) {
                        field.set(c, type.getEnumConstants()[index.get()]);
                    }
                }
                if (isPrivate) {
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
