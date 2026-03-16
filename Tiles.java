import greenfoot.*;

public class Tiles extends Actor
{
    private static final greenfoot.Color[] COLORS = new greenfoot.Color[10];

    static {
        // Yellow (255,255,0) → Purple (128,0,128)
        for (int i = 0; i < 10; i++) {
            float ratio = i / 9.0f;

            int r = (int)(255 - (255 - 128) * ratio);
            int g = (int)(255 - 255 * ratio);
            int b = (int)(0 + 128 * ratio);

            COLORS[i] = new greenfoot.Color(r, g, b);
        }
    }

    public void setColor(int c)
    {
        GreenfootImage img = new GreenfootImage(25, 25);

        if (c >= 0 && c < 10) {
            img.setColor(COLORS[c]);
        } else {
            img.setColor(greenfoot.Color.GRAY);
        }

        img.fillRect(0, 0, 25, 25);

        setImage(img);
    }
}