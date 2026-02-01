package plantumlgenerator.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import plantumlgenerator.PlantUmlGenerator;

import java.io.IOException;

public abstract class GenerateUmlTask extends DefaultTask {

    @InputDirectory
    public abstract DirectoryProperty getInputDir();

    @OutputFile
    public abstract RegularFileProperty getOutputFile();

    @TaskAction
    public void generate() throws IOException {
        PlantUmlGenerator.generate(
            getInputDir().get().getAsFile().toPath(),
            getOutputFile().get().getAsFile().toPath()
        );
    }
}
