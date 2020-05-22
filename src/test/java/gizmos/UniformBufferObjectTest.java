package gizmos;

import com.edenrump.graphic.math.Std140Compatible;
import com.edenrump.graphic.math.glColumnVector;
import com.edenrump.graphic.math.glSquareMatrix;
import com.edenrump.graphic.mesh.Mesh;
import com.edenrump.graphic.mesh.MeshUtils;
import com.edenrump.graphic.openGL_gpu.UniformBlockBuffer;
import com.edenrump.graphic.openGL_gpu.UniformBuffer;
import com.edenrump.graphic.render.FlatRenderer;
import com.edenrump.graphic.shaders.Shader;
import com.edenrump.graphic.shaders.ShaderProgram;
import com.edenrump.graphic.time.Time;
import com.edenrump.graphic.viewport.Window;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;

public class UniformBufferObjectTest {

    static float[] positions = {
            -0.5f, 0.5f,//v0
            -0.5f, -0.5f,//v1
            0.5f, -0.5f,//v2
            0.5f, 0.5f,//v3
    };
    static int[] indices = {
            0, 1, 3,//top left triangle (v0, v1, v3)
            3, 1, 2//bottom right triangle (v3, v1, v2)
    };

    //files required for this shader
    final static String VERTEX_FILE_LOCATION = "src/test/resources/shaders/UniformBufferObjectTestShader.vert";
    final static String FRAGMENT_FILE_LOCATION = "src/test/resources/shaders/UniformBufferObjectTestShader.frag";

    static Mesh GUI;
    private static Window window;
    private static Time gameTime;

    public static void main(String[] args) {
        gameLoop().run();
    }

    private static Runnable gameLoop() {

        return () -> {
            window = new Window(0.5, 0.5, "Attribute Test", Color.YELLOW);
            window.create(false);
            window.show();
            gameTime = Time.getInstance();

            ShaderProgram uniformBufferTestShader = new ShaderProgram();
            Shader v = Shader.loadShader(GL_VERTEX_SHADER, VERTEX_FILE_LOCATION);
            Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_FILE_LOCATION);
            uniformBufferTestShader.attachShaders(v, f);
            uniformBufferTestShader.link();
            v.delete();
            f.delete();

            int bufferBlockBinding = 0;
            String uniformBlockName = "TestBlock";

            UniformBlockBuffer ubo = new UniformBlockBuffer();
            ubo.blockBind(bufferBlockBinding);
            uniformBufferTestShader.bindUniformBlock(uniformBlockName, bufferBlockBinding);

            Std140Compatible mat4Padding = new glSquareMatrix(4);
            Std140Compatible vec3ColorY = new glColumnVector(0.7f, 0.1f, 0.1f);
            FloatBuffer buffer = BufferUtils.createFloatBuffer(80);
            mat4Padding.storeStd140DataInBuffer(buffer);
            vec3ColorY.storeStd140DataInBuffer(buffer);
            buffer.flip();
            ubo.updateData(buffer);

            GUI = MeshUtils.loadMesh2D(positions, indices);
            FlatRenderer flatRenderer = new FlatRenderer(uniformBufferTestShader);
            flatRenderer.addMesh(GUI);

            while (!window.isCloseRequested()) {
                gameTime.updateTime();
                window.update();
                window.prepareForRender();
                flatRenderer.render();
                window.transferBuffersAfterRender();
            }

            flatRenderer.cleanUp();
            window.terminate();
        };
    }

}