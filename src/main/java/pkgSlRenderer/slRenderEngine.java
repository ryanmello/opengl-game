package pkgSlRenderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import java.util.Random;
import pkgSlUtils.slWindowManager;

import static org.lwjgl.opengl.GL11.*;

public class slRenderEngine {
    private final int TRIANGLES_PER_CIRCLE = 40;
    private final float C_RADIUS = 0.05f;
    private final int MAX_CIRCLES = 100;
    private final int UPDATE_INTERVAL = 10;

    private Random random;

    public slRenderEngine() {
        random = new Random();
    };

    public void initOpenGL(slWindowManager windowManager){
        GL.createCapabilities();
        int[] windowSize = windowManager.getWindowSize();
        glViewport(0, 0, windowSize[0], windowSize[1]);

        float CC_RED = 0.0f, CC_GREEN = 0.0f, CC_BLUE = 1.0f, CC_ALPHA = 1.0f;
        glClearColor(CC_RED, CC_GREEN, CC_BLUE, CC_ALPHA);
    }

    public void render(){
        while(!slWindowManager.isGlfwWindowClosed()){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            for(int i = 0; i < MAX_CIRCLES; i++){
                float[] color = generateRandomColor();
                float[] position = generateRandomPosition();

                glColor4f(color[0], color[1], color[2], color[3]);
                drawCircle(position[0], position[1], C_RADIUS);
            }

            slWindowManager.get().swapBuffers();
            GLFW.glfwPollEvents();

            try {
                Thread.sleep((long)(UPDATE_INTERVAL * 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        slWindowManager.get().destroyGlfwWindow();
    }

    private void drawCircle(float x, float y, float radius){
        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(x, y);

        for(int i = 0; i <= TRIANGLES_PER_CIRCLE; i++){
            double angle = 2 * Math.PI * i / TRIANGLES_PER_CIRCLE;
            float dx = (float)(radius * Math.cos(angle));
            float dy = (float)(radius * Math.sin(angle));
            glVertex2f(x + dx, y + dy);
        }

        glEnd();
    }

    private float[] generateRandomColor(){
        return new float[] { random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f };
    }

    private float[] generateRandomPosition(){
        float x = -1.0f + C_RADIUS + (random.nextFloat() * (2.0f - 2 * C_RADIUS));
        float y = -1.0f + C_RADIUS + (random.nextFloat() * (2.0f - 2 * C_RADIUS));
        return new float[]{x, y};
    }
}

