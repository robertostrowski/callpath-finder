# Pathfinder - IntelliJ plugin

Plugin for IntelliJ used for finding call paths from given package to selected method

### Building, testing, debugging

To launch an IDE with this plugin for development purposes, run

`gradlew runIde` on Windows, or

`./gradlew runIde` on Linux, or macOs

### Installing locally

Build a plugin with 
`gradlew build`, then copy the contents of `.plugin` directory into local IntelliJ plugin directory:

`<SYSTEM DRIVE>\Users\<USER ACCOUNT NAME>\.<PRODUCT><VERSION>\config\plugins` on Windows

`/home/<USER>.<PRODUCT><VERSION>\config\plugins` on Unix-based systems.

### Using the plugin

- Click inside a method definition
- Select "Method pathfinder"
- Pick a package to search for call paths from
- Results will be displayed in a custom tool window
