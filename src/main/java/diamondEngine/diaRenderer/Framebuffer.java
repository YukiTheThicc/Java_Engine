package diamondEngine.diaRenderer;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {

    // ATTRIBUTES
    private int fboId = 0;
    private Texture texture = null;

    // CONSTRUCTORS
    public Framebuffer(int width, int height) {

        this.fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, this.fboId);
        this.texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getId(), 0);

        int rboId = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboId);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboId);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            DiaLogger.log(Framebuffer.class, "Error creating frame buffer, framebuffer is not complete", DiaLoggerLevel.ERROR);
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    // GETTERS & SETTERS
    public int getFboId() {
        return fboId;
    }

    public Texture getTexture() {
        return texture;
    }

    // METHODS
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, this.fboId);
    }

    public void unBind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
