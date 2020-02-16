package com.zach.engine;

public class Main implements Runnable
{
    private Thread thread;
    private Window window;

    public Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    private Renderer renderer;

    public Input getInput() {
        return input;
    }

    private Input input;
    private AbstractGame game;

    private boolean running = false;
    private final double UPDATE_CAP = 1.0f/600.0f;
    private int width = 640;
    private int height = 360;
    private float scale = 3f;

    public int getFps() {
        return fps;
    }

    private int fps = 0;

    public Window getWindow() {
        return window;
    }
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title = "Duck Duck Goose";

    public Main(AbstractGame game)
    {
        this.game = game;
    }
    public void initialize()
    {
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);

        thread = new Thread(this);
        thread.run();
    }
    public void uninitialize()
    {
        running = false;
        System.exit(0);
    }
    public void run()
    {
        running = true;

        boolean render = false;

        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0f;
        double elapsedTime = 0;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        fps = 0;

        game.init(this);

        while(running)
        {
            render = false;

            firstTime = System.nanoTime() / 1000000000.0f;
            elapsedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += elapsedTime;
            frameTime += elapsedTime;

            while(unprocessedTime >= UPDATE_CAP)
            {
                unprocessedTime -= UPDATE_CAP;
                render = true;

                game.update(this, (float)(UPDATE_CAP));
                input.update();

                if(frameTime >= 1.0f)
                {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
            }

            if(render)
            {
                renderer.clear();
                game.render(this, renderer);
                renderer.process();
                //renderer.drawText("FPS:" + fps, 0, 0, 0xffffffff, 1);
                window.update();
                frames++;

            }
            else
                {
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
        }

        dispose();
    }
    private void dispose()
    {

    }
}
