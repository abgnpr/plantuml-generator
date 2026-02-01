package plantumlgenerator.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;

public class PlantUmlPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getTasks().register("generateUml", GenerateUmlTask.class, task -> {
            task.setGroup("documentation");
            task.setDescription("Generates PlantUML diagram from source code");

            // Convention: Default to main java source set
            project.getPluginManager().withPlugin("java", plugin -> {
                JavaPluginExtension javaExtension = project.getExtensions().getByType(JavaPluginExtension.class);
                task.getInputDir().set(javaExtension.getSourceSets().getByName("main").getJava().getSourceDirectories().getSingleFile());
            });

            // Convention: Output to diagrams/project-name.puml
            task.getOutputFile().convention(
                project.getLayout().getProjectDirectory().file("diagrams/" + project.getName() + "-uml-diagram.puml")
            );
        });
    }
}
