package de.Roboter007.voxiety.core.renderer.shader;

import de.Roboter007.voxiety.core.renderer.Asset;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader implements Asset {

    private int shaderProgramID;
    private boolean beingUsed;

    private String vertexSrc;
    private String fragmentSrc;
    private String path;

    public Shader(String path) {
        this.path = path;

        try {
            String src = new String(Files.readAllBytes(Path.of(path)));
            String[] splitString = src.split("(#type)( )+([a-zA-Z]+)");

            int index = src.indexOf("#type") + 6;
            int eol = src.indexOf("\r\n", index);
            String firstPattern = src.substring(index, eol).trim();

            index = src.indexOf("#type", eol) + 6;
            eol = src.indexOf("\r\n", index);
            String secondPattern = src.substring(index, eol).trim();

            if(firstPattern.equals("vertex")) {
                vertexSrc = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSrc = splitString[1];
            } else {
                System.out.println("Unexpected token: '" + firstPattern + "'");
            }

            if(secondPattern.equals("vertex")) {
                vertexSrc = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSrc = splitString[2];
            } else {
                System.out.println("Unexpected token: '" + secondPattern + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false: "Error: Could not open file for shader: '" + path + "'";
        }
    }

    public String path() {
        return path;
    }

    public void compile() {
        // Compile & Link Shaders

        int vertexID, fragmentID;

        // -> Vertex Shader
        // Load
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        //Shader Src to GPU
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        //Check For Errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if(success == GL_FALSE) {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '" + path + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, length));
            assert false: "";
        }

        // -> Fragment Shader
        // Load
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //Shader Src to GPU
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        //Check For Errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);

        if(success == GL_FALSE) {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '" + path + "'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, length));
            assert false: "";
        }


        //Link Shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        //Check For Linking Errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE) {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '" + path + "'\n\tLinking of Shaders failed");
            System.out.println(glGetProgramInfoLog(fragmentID, length));
            assert false: "";
        }
    }

    public void use() {
        if(!beingUsed) {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLoc = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLoc, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat) {
        int varLoc = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix3fv(varLoc, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varLoc = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLoc, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        int varLoc = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLoc, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        int varLoc = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLoc, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float f) {
        int varLoc = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLoc, f);
    }

    public void uploadInt(String varName, int i) {
        int varLoc = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLoc, i);
    }

    public void uploadTexture(String varName, int slot) {
        int varLoc = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLoc, slot);
    }

    public void uploadIntArray(String varName, int[] array) {
        int varLoc = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1iv(varLoc, array);
    }

}
