# PlantUML Generator Gradle Plugin

A Gradle plugin that automatically generates [PlantUML](https://plantuml.com/) class diagrams from your Java source code. It parses your project's Java files and produces a `.puml` file describing your class structure.

## Usage

### 1. Include the Build

If you are using this as a composite build (local development), include it in your consumer project's `settings.gradle`:

```gradle
includeBuild('../plantuml-generator')
```

### 2. Apply the Plugin

In your consumer project's `build.gradle` (e.g., `app/build.gradle`), apply the plugin:

```gradle
plugins {
    id 'application' // or 'java'
    id 'abgnpr.plantuml-generator'
}
```

### 3. Run the Task

The plugin registers a `generateUml` task. Run it via Gradle:

```bash
./gradlew generateUml
```

### 4. Output

By default, the diagram will be generated at:
`diagrams/<project-name>-uml-diagram.puml` relative to your subproject.

You can customize this in your `build.gradle` if needed (task configuration is standard Gradle).

## Prerequisites: Installing PlantUML

To visualize the generated `.puml` files, you need PlantUML and Graphviz installed on your system.

### Linux (Debian/Ubuntu/Mint)

```bash
sudo apt update
sudo apt install plantuml graphviz
```

### Linux (Fedora/RHEL/CentOS)

```bash
sudo dnf install plantuml graphviz
```

### Windows

1.  **Install Java:** Ensure you have a JDK installed.
2.  **Install Graphviz:**
    *   Download the installer from [Graphviz Download Page](https://graphviz.org/download/).
    *   Run the installer and make sure to select "Add Graphviz to the system PATH for all users" (or current user).
3.  **Install PlantUML:**
    *   **Option A (Chocolatey):** `choco install plantuml`
    *   **Option B (Manual):** Download `plantuml.jar` from [plantuml.com](https://plantuml.com/download) and run it via `java -jar plantuml.jar`.
    *   **Option C (VS Code Extension):** Install the "PlantUML" extension by Jebbs in VS Code. This is the recommended way for viewing diagrams directly in your editor.

## Development

This project uses Gradle.

*   **Build:** `./gradlew build`
*   **Test:** `./gradlew test`
