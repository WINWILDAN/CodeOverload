import java.awt.*;
public class CharacterSprite {
    private Image[] frames;
    private int currentFrame = 0;

    public CharacterSprite(Image[] frames) {
        this.frames = frames;
    }

    public void update() {
        currentFrame = (currentFrame + 1) % frames.length;
    }

    public Image getCurrentFrame() {
        return frames[currentFrame];
    }
}