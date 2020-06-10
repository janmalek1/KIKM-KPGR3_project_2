package main;

import lwjglutils.*;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2019-09-02
 */
public class Renderer extends AbstractRenderer {

    /*konstruktor s cestou k textuře jako vstupním parametrem*/
    public Renderer(String texturePath) {
        this.texturePath = texturePath;
    }

    private int shaderProgram;
    private OGLBuffers buffers;
    private OGLTexture2D textureMosaic;


    private int size = 8;
    private float scale = 1f;
    private float thresholdNaive = 0.5f;
    private float thresholdRandom = 0.0f;

    int locMode, locSize, locScale, locThresholdNaive, locThresholdRandom;

    private final float MIN_SCALE = 0.05f, MAX_SCALE = 1.9f;
    private final float MIN_NAIVE = 0.00f, MAX_NAIVE = 1.0f;
    private final float MIN_RANDOM = -2.0f, MAX_RANDOM = 2.0f;

    String texturePath;

    @Override
    public void init() {

        OGLUtils.printOGLparameters();
        OGLUtils.printLWJLparameters();
        OGLUtils.printJAVAparameters();
        OGLUtils.shaderCheck();

        // Set the clear color
        //glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
        //textRenderer = new OGLTextRenderer(width, height);

        shaderProgram = ShaderUtils.loadProgram("/dither");
        //glUseProgram(shaderProgram);

        //nastavení lokátorů
        locMode = glGetUniformLocation(shaderProgram, "mode");
        locSize = glGetUniformLocation(shaderProgram, "size");
        locScale = glGetUniformLocation(shaderProgram, "scale");
        locThresholdNaive = glGetUniformLocation(shaderProgram, "thresholdNaive");
        locThresholdRandom = glGetUniformLocation(shaderProgram, "thresholdRandom");

        /*v této úloze nepotřebujeme jemný grid*/
        buffers = GridFactory.generateGrid(2, 2);

        //načtení textury
        try {
            textureMosaic = new OGLTexture2D(texturePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void display() {

        glUseProgram(shaderProgram);

        /*postará se o případnou změnu velikosti okna*/
        glViewport(0, 0, width, height);

        /*textura*/
        textureMosaic.bind(shaderProgram, "textureMosaic", 0);

        // 0 - ORIGINÁLNÍ FOTKA, levý horní roh
        glUniform1i(locMode, 0);
        buffers.draw(GL_TRIANGLES, shaderProgram);

        // 1 - NAIVE DITHERING, pravý horní roh
        glUniform1i(locMode, 1);
        glUniform1f(locThresholdNaive, thresholdNaive);
        buffers.draw(GL_TRIANGLES, shaderProgram);

        // 2 - RANDOM DITHERING, levý dolní roh
        glUniform1i(locMode, 2);
        glUniform1f(locThresholdRandom, thresholdRandom);
        buffers.draw(GL_TRIANGLES, shaderProgram);

        // 3 - ORDERED DITHERING, prový dolní roh
        glUniform1i(locMode, 3);
        glUniform1i(locSize, size);
        glUniform1f(locScale, scale);
        buffers.draw(GL_TRIANGLES, shaderProgram);

    }

    private void changeScale(float change) {
        scale += change;

        if (scale > MAX_SCALE) {
            scale = MAX_SCALE;
        }

        if (scale < MIN_SCALE) {
            scale = MIN_SCALE;
        }

        System.out.println("The scaling coefficient for ORDERED DITHERING changed to: " + scale);
    }

    private void changeThresholdRandom(float change) {
        thresholdRandom += change;

        if (thresholdRandom > MAX_RANDOM) {
            thresholdRandom = MAX_RANDOM;
        }

        if (thresholdRandom < MIN_RANDOM) {
            thresholdRandom = MIN_RANDOM;
        }

        System.out.println("The coefficient for RANDOM DITHERING changed to: " + thresholdRandom);
    }

    private void changeThresholdNaive(float change) {
        thresholdNaive += change;

        if (thresholdNaive > MAX_NAIVE) {
            thresholdNaive = MAX_NAIVE;
        }

        if (thresholdNaive < MIN_NAIVE) {
            thresholdNaive = MIN_NAIVE;
        }

        System.out.println("The coefficient for NAIVE DITHERING changed to: " + thresholdNaive);
    }

    private void changeSizeMatrix() {
        if (size == 8) {
            size = 4;
            System.out.println("size: " + size);
        } else {
            size = 8;
            System.out.println("size: " + size);
        }

        System.out.println("The size of the Matrix for ORDERED DITHERING changed to: " + size);
    }

    @Override
    public GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }

    /*implementace listenerů na stisky kláves*/
    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (action == GLFW_PRESS || action == GLFW_REPEAT) {
                switch (key) {
                    case GLFW_KEY_Q:
                        changeThresholdNaive(+0.01f);
                        break;
                    case GLFW_KEY_A:
                        changeThresholdNaive(-0.01f);
                        break;
                    case GLFW_KEY_W:
                        changeThresholdRandom(+0.05f);
                        break;
                    case GLFW_KEY_S:
                        changeThresholdRandom(-0.05f);
                        break;
                    case GLFW_KEY_E:
                        changeScale(+0.02f);
                        break;
                    case GLFW_KEY_D:
                        changeScale(-0.02f);
                        break;
                    case GLFW_KEY_SPACE:
                        changeSizeMatrix();
                        break;
                }
            }
        }
    };

}