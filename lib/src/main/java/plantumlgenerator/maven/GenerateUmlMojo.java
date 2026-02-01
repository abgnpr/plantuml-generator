package plantumlgenerator.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import plantumlgenerator.PlantUmlGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.COMPILE)
public class GenerateUmlMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "inputDir", required = true)
    private File inputDir;

    @Parameter(defaultValue = "${project.basedir}/diagrams/${project.name}-uml-diagram.puml", property = "outputFile", required = true)
    private File outputFile;

    public void execute() throws MojoExecutionException {
        getLog().info("Generating PlantUML diagram...");
        getLog().info("Input: " + inputDir);
        getLog().info("Output: " + outputFile);

        // Ensure output directory exists
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try {
            PlantUmlGenerator.generate(inputDir.toPath(), outputFile.toPath());
        } catch (IOException e) {
            throw new MojoExecutionException("Error generating PlantUML diagram", e);
        }
    }
}
