package diamondEngine.diaUtils;

import org.joml.Vector2f;

public class DiaMath {

    /*
    * This class is to be used as part of the custom Diamond Physics Engine
    */
    public static void rotate(Vector2f vec, float degrees, Vector2f origin) {
        float x = vec.x - origin.x;
        float y = vec.y - origin.y;

        float cos = (float)Math.cos(Math.toRadians(degrees));
        float sin = (float)Math.sin(Math.toRadians(degrees));

        float xPrime = x * cos - y * sin;
        float yPrime = x * sin + y * cos;

        xPrime += origin.x;
        yPrime += origin.y;

        vec.x = xPrime;
        vec.y = yPrime;
    }

    public static boolean compareFloat(float x, float y, float epsilon) {
        return Math.abs(x - y) <= epsilon * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
    }

    public static boolean compareVector2f(Vector2f vec1, Vector2f vec2, float epsilon) {
        return compareFloat(vec1.x, vec2.x, epsilon) && compareFloat(vec1.y, vec2.y, epsilon);
    }

    public static boolean compareFloat(float x, float y) {
        return Math.abs(x - y) <= Float.MIN_VALUE * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
    }

    public static boolean compareVector2f(Vector2f vec1, Vector2f vec2) {
        return compareFloat(vec1.x, vec2.x) && compareFloat(vec1.y, vec2.y);
    }
}
