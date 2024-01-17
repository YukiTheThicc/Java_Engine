package diamondEngine.diaRenderer;

import diamondEngine.diaAssets.Texture;
import diamondEngine.diaUtils.diaLogger.DiaLogger;
import diamondEngine.diaUtils.diaLogger.DiaLoggerLevel;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {

    // ATTRIBUTES
    private int fboId;
    private Texture texture;

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
        glViewport(0, 0, texture.getWidth(), texture.getHeight());
        glBindFramebuffer(GL_FRAMEBUFFER, this.fboId);
    }

    public void unBind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void destroy() {
        unBind();
        glDeleteTextures(texture.getId());
        glDeleteFramebuffers(fboId);
        this.fboId = 0;
        this.texture = null;
    }
}
