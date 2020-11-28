package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double lastX, lastY;
    private double posX, posY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.posX = 0.0;
        this.posY = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;

    }
    public static MouseListener get() {
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePosCallback(long glfwWindow, double posX, double posY) {
        get().lastX = get().posX;
        get().lastX = get().posX;
        get().posX = posX;
        get().posY = posY;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }
    public static void mouseButtonCallback(long glfwWindow, int button, int action, int mods) {
        if(button > get().mouseButtonPressed.length) {
            return;
        }
        if(action == GLFW_PRESS) {
            get().mouseButtonPressed[button] = true;
        } else if(action == GLFW_RELEASE) {
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long glfwWindow, double scrollX, double scrollY){
        get().scrollX = scrollX;
        get().scrollY = scrollY;
    }

    // Return all values to 0
    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().posX;
        get().lastY = get().posY;
    }

    public static float getX() {
        return (float)get().posX;
    }

    public static float getY() {
        return (float)get().posY;
    }

    public static float getDx() {
        return (float)(get().lastX - get().posX);
    }

    public static float getDy() {
        return (float)(get().lastY - get().posY);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if(button > get().mouseButtonPressed.length) return false;
        return get().mouseButtonPressed[button];
    }
}
