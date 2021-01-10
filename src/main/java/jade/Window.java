package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window {

    private static Window window = null;
    private long glfwWindow;

    private static int width, height;
    private String title;

    // Background RGB Values
    public static float r, g, b;

    private static Scene currentScene;


    private Window() {
        this.width = 1366;
        this.height = 768;
        this.title = "Martong Us";
        this.r = 0.0f;
        this.g = 1.0f;
        this.b = 1.0f;

    }


    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false: "Unknown scene " + newScene;
                break;
        }
    }

    public static Window get() {
        if(Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Print all GLFW errors to standard err out
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()){
            throw new IllegalStateException("Unable to Initialize GLFW.");
        }

        // Set Configs
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create Window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        // Set Mouse callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's OpenGL context,
        // or any context that is managed externally. LWJGL detects the context that is
        // current in the current thread, creates the GLCapabilities instance and makes
        // the OpenGL bindings available for use.
        GL.createCapabilities();
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        Window.changeScene(0);

        while(!glfwWindowShouldClose(glfwWindow)){
            // Poll events
            glfwPollEvents();
            glClearColor(r, g, b, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            if(KeyListener.isKeyPressed(GLFW_KEY_Q)){
                glfwSetWindowShouldClose(glfwWindow, true);
            }
            if( dt >= 0.0f)
                currentScene.update(dt);

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

}
