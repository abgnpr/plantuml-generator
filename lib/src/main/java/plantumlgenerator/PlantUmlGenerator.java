package plantumlgenerator;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PlantUmlGenerator {

    public static void main(String[] args) throws IOException {
        String sourceRoot = "app/src/main/java"; // Default path
        String outputFile = "project-diagram.puml"; // Default output file

        if (args.length > 0) {
            sourceRoot = args[0];
        }
        if (args.length > 1) {
            outputFile = args[1];
        }

        generate(Paths.get(sourceRoot), Paths.get(outputFile));
    }

    public static void generate(Path sourceRoot, Path outputFile) throws IOException {
        System.out.println("Scanning source code in: " + sourceRoot);
        System.out.println("Generating UML diagram to: " + outputFile);

        String diagramName = outputFile.getFileName().toString().replace(".puml", "").replace(".txt", "");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.toFile()))) {
            writer.write("@startuml " + diagramName);
            writer.newLine();

            try (Stream<Path> paths = Files.walk(sourceRoot)) {
                paths.filter(Files::isRegularFile)
                     .filter(path -> path.toString().endsWith(".java"))
                     .forEach(path -> parseFile(path, writer));
            }

            writer.write("@enduml");
            writer.newLine();
        }

        System.out.println("Done.");
    }

    private static void parseFile(Path path, BufferedWriter writer) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(path);
            cu.accept(new ClassVisitor(writer), null);
        } catch (IOException e) {
            System.err.println("Error parsing file: " + path);
            e.printStackTrace();
        }
    }

    private static class ClassVisitor extends VoidVisitorAdapter<Void> {
        private final BufferedWriter writer;

        public ClassVisitor(BufferedWriter writer) {
            this.writer = writer;
        }

        private void writeLine(String line) {
            try {
                writer.write(line);
                writer.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            String type = n.isInterface() ? "interface" : "class";
            writeLine(type + " " + n.getNameAsString() + " {");

            // Fields
            for (FieldDeclaration field : n.getFields()) {
                field.getVariables().forEach(variable -> {
                    String visibility = getVisibility(field);
                    writeLine("  " + visibility + variable.getNameAsString() + " : " + variable.getTypeAsString());
                });
            }

            // Methods
            for (MethodDeclaration method : n.getMethods()) {
                String visibility = getVisibility(method);
                writeLine("  " + visibility + method.getNameAsString() + "() : " + method.getTypeAsString());
            }

            writeLine("}");

            // Inheritance
            for (ClassOrInterfaceType extended : n.getExtendedTypes()) {
                writeLine(extended.getNameAsString() + " <|-- " + n.getNameAsString());
            }

            // Implementation
            for (ClassOrInterfaceType implemented : n.getImplementedTypes()) {
                writeLine(implemented.getNameAsString() + " <|.. " + n.getNameAsString());
            }
            
            super.visit(n, arg);
        }

        private String getVisibility(FieldDeclaration fd) {
            if (fd.isPublic()) return "+";
            if (fd.isPrivate()) return "-";
            if (fd.isProtected()) return "#";
            return "~";
        }

        private String getVisibility(MethodDeclaration md) {
            if (md.isPublic()) return "+";
            if (md.isPrivate()) return "-";
            if (md.isProtected()) return "#";
            return "~";
        }
    }
}
