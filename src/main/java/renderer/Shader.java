package renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;


public class Shader {

    private int shaderProgramID;
    private String vertexSrc;
    private String fragmentSrc;
    private String filepath;


    public Shader(String filepath){
        this.filepath = filepath;

        try {
            // Find the first pattern after #type 'pattern'
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String [] splitString = source.split("(#type)( )+([a-zA-Z]+)");
            int index = source.indexOf("#type") + 6;

            // For Mac & Linux use \n. For windows use \r\n
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();
            System.out.println(firstPattern);

            // Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            // For Mac & Linux use \n. For windows use \r\n
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();

            // First pattern
            if(firstPattern.equals("vertex")){
                vertexSrc = splitString[1];
            } else if (firstPattern.equals("fragment")){
                fragmentSrc = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern);
            }

            // Second pattern
            if(secondPattern.equals("vertex")){
                vertexSrc = splitString[2];
            } else if (secondPattern.equals("fragment")){
                fragmentSrc = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern);
            }

        } catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }

        System.out.println(vertexSrc);
        System.out.println(fragmentSrc);
    }

    public void compile(){
        int vertexID, fragmentID;

        // Load & Compile vertex
        vertexID = GL20.glCreateShader(GL_VERTEX_SHADER);
        // Pass shader source to the GPU
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Load & Compile vertex
        fragmentID = GL20.glCreateShader(GL_FRAGMENT_SHADER);
        // Pass shader source to the GPU
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders & check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE ){
            int len = glGetShaderi(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking of shader failed.");
            System.out.println(glGetShaderInfoLog(shaderProgramID, len));
            assert false : "";
        }

    }

    public void use(){
        // Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }


}
