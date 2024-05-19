package emitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Emitter {
    private final String fullPath;
    private String header;
    private String code;
    
    public Emitter(String fullPath) {
        this.fullPath = fullPath;
        this.header = "";
        this.code = "";
    }
    
    public void emit(String code) {
        this.code += code;
    }
    
    public void emitLine(String code) {
        this.code += code + "\n";
    }
    
    public void emitIndent(int indents) {
        this.code += "    ".repeat(indents);
    }
    
    public void headerLine(String code) {
        this.header += code + "\n";
    }
    
    public void headerIndent(int indents) {
        this.header += "    ".repeat(indents);
    }
    
    public void writeFile() throws IOException {
        Path outputFile = Paths.get(fullPath);
        String outputContents = this.header + this.code;
        Files.writeString(outputFile, outputContents, StandardCharsets.UTF_8);
    }
}
