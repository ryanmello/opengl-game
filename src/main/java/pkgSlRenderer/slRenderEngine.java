package pkgSlRenderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import java.util.Random;

import pkgDriver.slSpot;
import pkgSlUtils.slWindowManager;

import static org.lwjgl.opengl.GL11.*;

public class slRenderEngine {
    private Random random;
    private final int MIN_SIDES = slSpot.MIN_SIDES;
    private final int MAX_SIDES = slSpot.MAX_SIDES;
    private final int UPDATE_INTERVAL = slSpot.FRAME_DELAY;
    private int currentSides;
    private float[] currentColor;

    public slRenderEngine() {
        random = new Random();
        currentSides = MIN_SIDES;
        currentColor = generateRandomColor();
    }

    public void initOpenGL(slWindowManager windowManager) {
        GL.createCapabilities();
        int[] windowSize = windowManager.getWindowSize();
        glViewport(0, 0, windowSize[0], windowSize[1]);

        float CC_RED = 0.0f, CC_GREEN = 0.0f, CC_BLUE = 1.0f, CC_ALPHA = 1.0f;
        glClearColor(CC_RED, CC_GREEN, CC_BLUE, CC_ALPHA);
    }

    public void render() {
        while (!slWindowManager.isGlfwWindowClosed()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            int rows = slSpot.NUM_ROWS;
            int cols = slSpot.NUM_COLS;
            float cellWidth = 2.0f / cols;
            float cellHeight = 2.0f / rows;
            float radius = (Math.min(cellWidth, cellHeight) / 2);

            glColor4f(currentColor[0], currentColor[1], currentColor[2], currentColor[3]);

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    float x = -1.0f + (col * cellWidth) + (cellWidth / 2);
                    float y = 1.0f - (row * cellHeight) - (cellHeight / 2);

                    drawPolygon(x, y, radius, currentSides);
                }
            }

            currentSides++;
            if (currentSides > MAX_SIDES) {
                currentSides = MIN_SIDES;
            }

            currentColor = generateRandomColor();

            slWindowManager.get().swapBuffers();
            GLFW.glfwPollEvents();

            try {
                Thread.sleep(UPDATE_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        slWindowManager.get().destroyGlfwWindow();
    }

    private void drawPolygon(float x, float y, float radius, int sides) {
        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(x, y);

        for (int i = 0; i <= sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            float dx = (float) (radius * Math.cos(angle));
            float dy = (float) (radius * Math.sin(angle));
            glVertex2f(x + dx, y + dy);
        }

        glEnd();
    }

    private float[] generateRandomColor() {
        return new float[]{random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f};
    }
}