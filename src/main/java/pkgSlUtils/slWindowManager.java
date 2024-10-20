package pkgSlUtils;

import static pkgDriver.slSpot.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;

public class slWindowManager {
    private static long glfw_win;
    private static slWindowManager my_window;
    private int width;
    private int height;

    private slWindowManager() {}

    public long initGLFWWindow(int width, int height, String title){
        GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        this.width = width;
        this.height = height;

        glfw_win = glfwCreateWindow(width, height, title, 0, 0);
        if (glfw_win == 0) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        // Center the window
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(glfw_win, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        glfwMakeContextCurrent(glfw_win);
        glfwShowWindow(glfw_win);

        return glfw_win; // Return the window handle
    }

    public static slWindowManager get(){
        if(my_window == null){
            my_window = new slWindowManager();
        }

        return my_window;
    }

    public void destroyGlfwWindow(){
        glfwDestroyWindow(glfw_win);
        glfwTerminate();
    }

    public void swapBuffers(){
        glfwSwapBuffers(glfw_win);
    }

    public static boolean isGlfwWindowClosed(){
        return glfwWindowShouldClose(glfw_win);
    }

    public void updateContextToThis(){
        glfwMakeContextCurrent(glfw_win);
    }

    public int[] getWindowSize(){
        return new int[] { this.width, this.height };
    }

//    public int[] getCurrentWindowSize() {
//        int[] size = new int[2];
//        glfwGetFramebufferSize(glfw_win, size);
//        return size;
//    }

}
