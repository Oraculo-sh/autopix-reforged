# ForgeGradle Documentation
=========================

This is the official documentation for [ForgeGradle], a [Gradle] plugin for developing [MinecraftForge] and mods using MinecraftForge.

This documentation is _only_ for ForgeGradle, **this is not a Java, Groovy, or Gradle tutorial**.

Adding the Plugin
-----------------

ForgeGradle uses Gradle 8; it can be added using the `plugins` block in the `build.gradle` by adding the following information to the `settings.gradle`:

```gradle
// In settings.gradle
pluginManagement {
    repositories {
        // ...

        // Add the MinecraftForge maven
        maven { url = 'https://maven.minecraftforge.net/' }
    }
}

plugins {
    // Add toolchain resolver
    id 'org.gradle.toolchains.foojay-resolver-convention' version '0.5.0'
}
```

```gradle
// In build.gradle
plugins {
    // Add the ForgeGradle plugin
    id 'net.minecraftforge.gradle' version '[6.0,6.2)'

    // ...
}
```

[ForgeGradle]: https://github.com/MinecraftForge/ForgeGradle
[Gradle]: https://gradle.org/
[MinecraftForge]: https://github.com/MinecraftForge/MinecraftForge


# Getting Started with ForgeGradle
================================

If you have never used ForgeGradle before, here is the minimum amount of information needed to get a development environment setup.

#### Prerequisites

* An installation of the Java Development Kit (JDK)

Minecraft Versions | Java Development Kit Version
:---:              | :---:
1.12 - 1.16        | [JDK 8][jdk8]
1.17               | [JDK 16][jdk16]
1.18 - 1.19        | [JDK 17][jdk17]

* Familiarity with an Integrated Development Environment (IDE)
    * It is preferable to use one with some form of Gradle integration

## Setting Up ForgeGradle

1. First download a copy of the [Modder Development Kit (MDK)][mdk] from MinecraftForge and extract the zip to an empty directory.
1. Open the directory you extracted the MDK to within your IDE of choice. If your IDE integrates with Gradle, import it as a Gradle project.
1. Customize your Gradle buildscript for your mod:
    1. Set `archivesBaseName` to the desired mod id. Additionally, replace all occurrences of `examplemod` with the mod id as well.
    1. Change the `group` to your desired package name. Make sure to follow existing [naming conventions][group].
    1. Change the `version` number to reflect the current version of your mod. It is highly recommended to use [Forge's extension on semantic versioning][semver].


!!! important
    Make sure that any changes to the mod id are reflected in the mods.toml and main mod class. See [Structuring Your Mod][structuring] on the Forge docs for more information.

4. Reload or refresh your Gradle project using your IDE. If your IDE does not have Gradle integration, run the following from a shell in your project's directory:

```sh
./gradlew build --refresh-dependencies
```

5. If your IDE is either Eclipse, IntelliJ IDEA, or Visual Studio Code, you can generate run configurations using one of the following commands, respectively:

#### Eclipse

```sh
./gradlew genEclipseRuns
```

#### IntelliJ IDEA

```sh
./gradlew genIntellijRuns
```

#### Visual Studio Code

```sh
./gradlew genVSCodeRuns
```

You can the run the client, server, etc. using one of the generated run configurations.

!!! tip
    If your IDE is not listed, you can still run the configurations using `./gradlew run*` (e.g., `runClient`, `runServer`, `runData`). You can use these commands with the supported IDEs as well.

Congratulations, now you have a development environment set up!


[jdk8]: https://adoptium.net/temurin/releases/?version=8
[jdk16]: https://adoptium.net/temurin/releases/?version=16
[jdk17]: https://adoptium.net/temurin/releases/?version=17

[mdk]: https://files.minecraftforge.net/
[group]: https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html
[semver]: https://docs.minecraftforge.net/en/latest/gettingstarted/versioning/
[structuring]: https://docs.minecraftforge.net/en/latest/gettingstarted/structuring/


# ForgeGradle Configurations
==========================

ForgeGradle has numerous configurations that can change how the development environment is configured. Most configurations are set using the `minecraft` block; however, some others can be specified within the `dependencies` block or modify the built `jar`, such as `reobfJar`.

Enabling Access Transformers
----------------------------

[Access Transformers][at] can widen the visibility or modify the `final` flag of Minecraft classes, methods, and fields. To enable access transformers in the production environment, you can set `accessTransformer` to configuration file in question:

```gradle
minecraft {
    // ...

    // Add an access transformer file relative to the project's directory
    accessTransformer = project.file('src/main/resources/META-INF/accesstransformer.cfg')
}
```

!!! important
    While the access transformer in the development environment can be read from anywhere the user specifies, in production, the file will only be read from `META-INF/accesstransformer.cfg`.

Human-Readable Mappings
-----------------------

Minecraft's source code is obfuscated. As such, all classes, methods, and fields have machine-generated names with no package structures. Function-local variable names, meanwhile, are turned into a snowman (`☃`) due to how the Local Variable Table is stored. It is difficult to create mods using obfuscated names as reverse engineering them is tedious, may change between versions, and may be invalid in the language itself but not in the bytecode.

To address the last two issues, Forge fuzzily maps each class, method, field, and parameter to a unique identifier, known as the SRG name, via the [ForgeAutoRenamingTool][fart]. SRG mappings are used in production when the game is being run by the user client.

To allow easier development, ForgeGradle allows the user to choose a mapping set to apply on top of SRG mappings, which we will refer to as human-readable mappings. ForgeGradle knows how to convert the mod jar to SRG mappings for use in production via the `reobf*` task.

The mapping set used can be specified by setting the `mappings` field in the `minecraft` block. The `mappings` field takes in two arguments: `channel` which is the type of the mapping set, and `version` which is the version of the mapping set to apply.

Currently, there are three default mapping sets built into ForgeGradle:

* `official` - This uses the mapping set provided by Mojang. These mappings do not have parameter names and only exist from 1.14 onward.
* `stable` - This uses a mapping set generated by MCP. These are typically incomplete and no longer exist as of 1.17.
* `snapshot` - This also is a mapping set generated by MCP, similar to a nightly build of a program. These are also typically incomplete and no longer exist as of 1.17.

!!! note
    The class names used in production are from `stable` prior to 1.17 and from `official` from 1.17 onwards.

```gradle
mappings {
    // Sets the mappings to use those from Mojang for Minecraft 1.19.4.
    mappings channel: 'official', version: '1.19.4'

    // ...
}
```

### Parchment

Parchment is an official project maintained by ParchmentMC which provides open, community-sourced parameter names and javadocs on top of the `official` mapping set. You can learn how setup and use the parchment mapping set on [their website][parchment].

Preparing Run Tasks
-------------------

Run tasks (`run*`) have two separate pipelines depending on whether they are executed through `gradlew` or a run configuration. By default, there are two tasks that prepare the workspace for execution:

First, there are `prepare*` tasks which are executed before `run*` tasks and ensure that mapping files are prepared for the game. The `prepare*Compile` task is typically only executed as a dependency of `run*` tasks to make sure that the game is compiled before it is run.

If your IDE is either Eclipse or IntelliJ IDEA, the run configuration can be configured to execute the `prepare*` tasks before starting the game by setting `enableEclipsePrepareRuns` or `enableIdeaPrepareRuns`, respectively, to `true`. This will allow you to invoke custom Gradle tasks before your IDE launches the game.

```gradle
minecraft {
    // ...

    // Enable the 'prepare*' task for run configurations
    enableEclipsePrepareRuns true
    enableIdeaPrepareRuns true
}
```

### Copy IDE Resources

The `copyIdeResources` property can be used to copy resources configured by the `processResources` task to the IDE's resource output directories. This allows IDE run configurations that do not invoke Gradle (IntelliJ configured to use the IDEA runner or Eclipse) to use buildscript configurable resources. Usually, you need to enable this property when you are replacing values in files like the `mods.toml`.  
This only applies to Eclipse and IntelliJ IDEA via the `copyEclipseResources` and `copyIntellijResources` tasks, respectively.

```gradle
minecraft {
    // ...

    // Copies the files from 'processResources' to the IDE's resource output directories
    copyIdeResources true
}
```

### Run Configuration Folders

Run configurations can be sorted into folders if the `generateRunFolders` is set to `true`. This reads the `folderName` property set in the specific [run configuration][run] to determine the organizational structure.

```gradle
minecraft {
    // ...

    // When true, run configurations will be grouped into folders by their 'folderName'
    generateRunFolders true
}
```

[at]: https://docs.minecraftforge.net/en/latest/advanced/accesstransformers/
[fart]: https://github.com/MinecraftForge/ForgeAutoRenamingTool
[parchment]: https://parchmentmc.org/docs/getting-started
[run]: ./runs.md#run-configurations


#  Run Configurations
==================

Run configurations define how an instance of the game is going to run. This includes arguments, working directories, task names, etc. Run configurations are defined within the `minecraft.runs` block. While no runs are configured by default, [Forge][userdev] does provide the configurations `client`, `server`, `data`, or `gameTestServer`.

```gradle
minecraft {
    // ...
    runs {
        // Configure runs here
    }
}
```

Run configurations can be added similar to any `NamedDomainObjectContainer` using closures.

```gradle
// Inside the minecraft block
runs {
    // Creates or configures the run configuration named 'client'
    client {
        // Configure run
    }
}
```

The following configurations properties are available:

```gradle 
// Inside the runs block
client {
    // The name of the Gradle run tasks,
    // Defaults to 'runX' where X is the container name
    taskName 'runThing'

    // Sets the entrypoint of the program to launch
    // Forge sets userdev main to be 'cpw.mods.bootstraplauncher.BootstrapLauncher'
    main 'com.example.Main'

    // Sets the working directory of the config
    // Defaults to './run'
    workingDirectory 'run'

    // Sets the name of the module for IntelliJ IDEA to configure for its runs
    // Defaults to '<project_name>.main'
    ideaModule 'example.main'

    // Sets the name of the folder that the run configuration should be added to
    // Defaults to the name of the project
    folderName 'example'

    // Sets whether this should run a Minecraft client
    // If not specified, checks the following
    // - Is there an environment property 'thing' that contains 'client'
    // - Does the configuration name contain 'client'
    // - Is main set to 'mcp.client.Start'
    // - Is main set to 'net.minecraft.client.main.Main'
    client true

    // Set the parent of this configuration to inherit from
    parent runs.example

    // Sets the children of this configuration
    children runs.child

    // Merges this configuration and specifies whether to overwrite existing properties
    merge runs.server, true

    // If not false, will merge the arguments of the parent with this configuration
    inheritArgs false

    // If not false, will merge the JVM arguments of the parent with this configuration
    inheritJvmArgs false

    // Adds a sourceset to the classpath
    // If none is specified, adds sourceSet.main
    source sourceSets.api

    // Sets an environment property for the run
    // Value will be interpreted as a file or a string
    environment 'envKey', 'value'

    // Sets a system property
    // Value will be interpreted as a file or a string
    property 'propKey', 'value'

    // Sets an argument to be passed into the application
    // Can specify multiple with 'args'
    arg 'hello'

    // Sets a JVM argument
    // Can specify multiple with 'jvmArgs'
    jvmArg '-Xmx2G'

    // Sets a token
    // Currently, the following tokens are being used:
    // - runtime_classpath
    // - minecraft_classpath
    token 'tokenKey', 'value'

    // Sets a token that's lazily initialized
    // Should usually be used instead of 'token', for example when the token resolves Gradle configurations
    lazyToken('lazyTokenKey') {
      'value'
    }

    // If true, compile all projects instead of for the current task
    // This is only used by IntelliJ IDEA
    buildAllProjects false
}
```

!!! tip
    You can see a list of all configured userdev properties within the [MinecraftForge buildscript][buildscript].

Mod Configurations
------------------

A mod in the current environment can be added using the `mods` block within a Run configuration. Mod blocks are also `NamedDomainObjectContainer`s.

```gradle
// Inside the runs block
client {
    // ...

    mods {
        other_mod {
            // ...
        }

        // Configures the 'example' mod
        example {
            // Add a source set to a mod's sources
            source sourceSets.main

            // Merges this configuration and specifies whether to overwrite existing properties
            merge mods.other_mod, true
        }
    }
}
```

[userdev]: https://github.com/MinecraftForge/MinecraftForge/blob/42115d37d6a46856e3dc914b54a1ce6d33b9872a/build.gradle#L374-L430
[buildscript]: https://github.com/MinecraftForge/MinecraftForge/blob/d4836bc769da003528b6cebc7e677a5aa23a8228/build.gradle#L434-L470


# Advanced Configurations
=======================

ForgeGradle contains a few specific or nuanced configuration techniques depending on the complexity of your build project.

Reobfuscating Source Sets
-------------------------

By default, the `reobf*` abd `rename*` tasks only contain files on the main source set's classpath. To reobfuscate files on a different classpath, they need to be added to the `libraries` property within the task.

```gradle
// Adds another source set's classpath to 'reobf' task.
tasks.withType('reobfJar') {
    libraries.from sourceSets.api.classpath
}
```


# Dependencies
============

Dependencies are not only used to develop interoperability between mods or add additional libraries to the game, but it also determines what version of Minecraft to develop for. This will provide a quick overview on how to modify the `repositories` and `dependencies` block to add dependencies to your development environment.

> This will not explain Gradle concepts in depth. It is highly recommended to read the [Gradle Dependency Management guide][guide] before continuing.

`minecraft`
-----------

The `minecraft` dependency specifies the version of Minecraft to use and must be included in the `dependencies` block. Any artifact, except artifacts which have the group `net.minecraft`, will apply any patches provided with the dependency. This typically only specifies the `net.minecraftforge:forge` artifact.

```gradle
dependencies {
    // Version of Forge artifact is in the form '<mc_version>-<forge_version>'
    // 'mc_version' is the version of Minecraft to load (e.g., 1.19.4)
    // 'forge_version' is the version of Forge wanted for that Minecraft version (e.g., 45.0.23)
    // Vanilla can be compiled against using 'net.minecraft:joined:<mc_version>' instead
    minecraft 'net.minecraftforge:forge:1.19.4-45.0.23'
}
```

Mod Dependencies
----------------

In a typical development environment, Minecraft is deobfuscated to intermediate mappings, used in production, and then transformed into whatever [human-readable mappings][mappings] the modder specified. Mod artifacts, when built, are obfuscated to production mappings (SRG), and as such, are unable to be used directly as a Gradle dependency.

As such, all mod dependencies need to be wrapped with `fg.deobf` before being added to the intended configuration.

```gradle
dependencies {
    // Assume we have already specified the 'minecraft' dependency

    // Assume we have some artifact 'examplemod' that can be obtained from a specified repository
    implementation fg.deobf('com.example:examplemod:1.0')
}
```

### Local Mod Dependencies

If the mod you are trying to depend on is not available on a maven repository (e.g., [Maven Central][central], [CurseMaven], [Modrinth]), you can add a mod dependency using a [flat directory] instead:

```gradle
repositories {
    // Adds the 'libs' folder in the project directory as a flat directory
    flatDir {
        dir 'libs'
    }
}

dependencies {
    // ...

    // Given some <group>:<name>:<version>:<classifier (default None)>
    //   with an extension <ext (default jar)>
    // Artifacts in flat directories will be resolved in the following order:
    // - <name>-<version>.<ext>
    // - <name>-<version>-<classifier>.<ext>
    // - <name>.<ext>
    // - <name>-<classifier>.<ext>

    // If a classifier is explicitly specified
    //  artifacts with the classifier will take priority:
    // - examplemod-1.0-api.jar
    // - examplemod-api.jar
    // - examplemod-1.0.jar
    // - examplemod.jar
    implementation fg.deobf('com.example:examplemod:1.0:api')
}
```

!!! note
    The group name can be anything but must not be empty for flat directory entries as they are not checked when resolving the artifact file.

Non-Minecraft Dependencies
--------------------------

Non-Minecraft dependencies are not loaded by Forge by default in the development environment. To get Forge to recognize the non-Minecraft dependency, they must be added to the `minecraftLibrary` configuration. `minecraftLibrary` works similarly to the `implementation` configuration within Gradle, being applied during compile time and runtime.

```gradle
dependencies {
    // ...

    // Assume there is some non-Minecraft library 'dummy-lib'
    minecraftLibrary 'com.dummy:dummy-lib:1.0'
}
```

> Non-Minecraft dependencies added to the development environment will not be included in built artifact by default! You must use [Jar-In-Jar][jij] to include the dependencies within the artifact on build.

[guide]: https://docs.gradle.org/8.1.1/userguide/dependency_management.html
[mappings]: ../configuration/index.md#human-readable-mappings

[central]: https://central.sonatype.com/
[CurseMaven]: https://cursemaven.com/
[Modrinth]: https://docs.modrinth.com/docs/tutorials/maven/

[flat]: https://docs.gradle.org/8.1.1/userguide/declaring_repositories.html#sub:flat_dir_resolver

[jij]: ./jarinjar.md


#  Jar-in-Jar
==========

Jar-in-Jar is a way to load dependencies for mods from within the jars of the mods. To accomplish this, Jar-in-Jar generates a metadata json within `META-INF/jarjar/metadata.json` on build containing the artifacts to load from within the jar.

Jar-in-Jar is a completely optional system which can be enabled using `jarJar#enable` before the `minecraft` block. This will include all dependencies from the `jarJar` configuration into the `jarJar` task. You can configure the task similarly to other jar tasks:

```gradle
// In build.gradle

// Enable the Jar-in-Jar system for your mod
jarJar.enable()


// Configure the 'jarJar' task
// 'all' is the default classifier
tasks.named('jarJar') {
    // ...
}
```

Adding Dependencies
-------------------

You can add dependencies to be included inside your jar using the `jarJar` configuration. As Jar-in-Jar is a negotiation system, all versions should supply a supported range.

```gradle
// In build.gradle
dependencies {
    // Compiles against and includes the highest supported version of examplelib
    //   between 2.0 (inclusive) and 3.0 (exclusive)
    jarJar(group: 'com.example', name: 'examplelib', version: '[2.0,3.0)')
}
```

If you need to specify an exact version to include rather than the highest supported version in the range, you can use `jarJar#pin` within the dependency closure. In these instances, the artifact version will be used during compile time while the pinned version will be bundled inside the mod jar.

```gradle
// In build.gradle
dependencies {
    // Compiles against the highest supported version of examplelib
    //   between 2.0 (inclusive) and 3.0 (exclusive)
    jarJar(group: 'com.example', name: 'examplelib', version: '[2.0,3.0)') {
      // Includes examplelib 2.8.0
      jarJar.pin(it, '2.8.0')
    }
}
```

You can additionally pin a version range while compiling against a specific version instead:

```gradle
// In build.gradle
dependencies {
    // Compiles against examplelib 2.8.0
    jarJar(group: 'com.example', name: 'examplelib', version: '2.8.0') {
      // Includes the highest supported version of examplelib
      //   between 2.0 (inclusive) and 3.0 (exclusive)
      jarJar.pin(it, '[2.0,3.0)')
    }
}
```

### Using Runtime Dependencies

If you would like to include the runtime dependencies of your mod inside your jar, you can invoke `jarJar#fromRuntimeConfiguration` within your buildscript. If you decide to use this option, it is highly suggested to include dependency filters; otherwise, every single dependency -- including Minecraft and Forge -- will be bundled in the jar as well. To support more flexible statements, the `dependency` configuration has been added to the `jarJar` extension and task. Using this, you can specify patterns to include or exclude from the configuration:

```gradle
// In build.gradle

// Add runtime dependencies to jar
jarJar.fromRuntimeConfiguration()

// ...

jarJar {
    // Include or exclude dependencies here from runtime configuration
    dependencies {
        // Exclude any dependency which begins with 'com.google.gson.'
        exclude(dependency('com.google.gson.*'))
    }
}
```

!!! tip
    It is generally recommended to set at least one `include` filter when using `#fromRuntimeConfiguration`.

Publishing a Jar-in-Jar to Maven
--------------------------------

For archival reasons, ForgeGradle supports publishing Jar-in-Jar artifacts to a maven of choice, similar to how the [Shadow plugin][shadow] handles it. In practices, this is not useful or recommended.

```gradle
// In build.gradle (has 'maven-publish' plugin)

publications {
    mavenJava(MavenPublication) {
        // Add standard java components and Jar-in-Jar artifact
        from components.java
        jarJar.component(it)

        // ...
    }
}
```


[shadow]: https://imperceptiblethoughts.com/shadow/getting-started/