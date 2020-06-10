package main;


public class App {

    public static void main(String[] args) {
        new LwjglWindow(new Renderer("./textures/mickey.jpg"));
        new LwjglWindow(new Renderer("./textures/mosaic.jpg"));
        new LwjglWindow(new Renderer("./textures/bricks.jpg"));
        new LwjglWindow(new Renderer("./textures/katka.jpg"));
    }

}
