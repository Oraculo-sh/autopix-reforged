# Project setup

import { FileTree, Tabs, TabItem } from "@astrojs/starlight/components";

As the Paper team primarily uses [IntelliJ IDEA](https://www.jetbrains.com/idea/), this guide will be focused on that IDE.
However, the steps below should apply to other IDEs as well, with some minor changes.

The Paper team uses [Gradle](https://gradle.org/) as its build system, and its tools are implemented for Gradle.
Most of the code below can be altered to work with other build systems, such as Maven, but this guide will only cover Gradle.

Follow the guide [here](https://docs.gradle.org/current/userguide/migrating_from_maven.html) to learn how to migrate from Maven to Gradle.

## Creating a new project

Open your IDE and select the option to create a new project.
In IntelliJ, you will get the option to select the type of project you want to create - select `New Project`.
Select `Gradle - Kotlin DSL` and click `Create`.

You will land into the `build.gradle.kts` file where you can add your dependencies.

### Adding Paper as a dependency

To add Paper as a dependency, you will need to add the Paper repository to your `build.gradle.kts` or `pom.xml` file as well as the dependency itself.

<Tabs>
  <TabItem label="Gradle Kotlin DSL">
    ```kotlin title="build.gradle.kts" replace
    repositories {
      maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
      }
    }

    dependencies {
      compileOnly("io.papermc.paper:paper-api:\{LATEST_PAPER_RELEASE}-R0.1-SNAPSHOT")
    }

    java {
      toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
    ```
  </TabItem>
  <TabItem label="Gradle Groovy DSL">
    ```groovy title="build.gradle" replace
    repositories {
      maven {
        name = 'papermc'
        url = 'https://repo.papermc.io/repository/maven-public/'
      }
    }

    dependencies {
      compileOnly 'io.papermc.paper:paper-api:\{LATEST_PAPER_RELEASE}-R0.1-SNAPSHOT'
    }
    ```
  </TabItem>
  <TabItem label="Maven POM">
    ```xml title="pom.xml" replace
    <project>
      <repositories>
        <repository>
          <id>papermc</id>
          <url>https://repo.papermc.io/repository/maven-public/</url>
        </repository>
      </repositories>

      <dependencies>
        <dependency>
          <groupId>io.papermc.paper</groupId>
          <artifactId>paper-api</artifactId>
          <version>\{LATEST_PAPER_RELEASE}-R0.1-SNAPSHOT</version>
          <scope>provided</scope>
        </dependency>
      </dependencies>
    </project>
    ```
  </TabItem>
</Tabs>

### Setting up the `src` directory

:::note

If your IDE creates a `src` directory automatically, you can skip this step.

:::

To set up the `src` directory, you will need to create a new directory called `src` and then create a new directory called `main` inside of it.
Inside of `main`, create two new directories called `java` and `resources`.

It should look like this:

<FileTree>
  - example-plugin/
    - build.gradle.kts
    - settings.gradle.kts
    - src/
      - main/
        - java/
        - resources/
</FileTree>

### Setting up the `java` directory

You will place your Java source files in the `java` directory. You first need to create some packages to organize your code.
For this example, we will create three packages called `io.papermc.testplugin` and then create a class called `ExamplePlugin` inside of it.

<FileTree>
  - example-plugin/
    - build.gradle.kts
    - settings.gradle.kts
    - src/
      - main/
        - java/
          - io/
            - papermc/
              - testplugin/
                - ExamplePlugin.java
        - resources/
</FileTree>

### Packages

You can see here that the `ExamplePlugin` class is inside the `io.papermc.testplugin` package.
A package is a way to organize your code - essentially, it's a folder. Java packages are used to group related classes.
Oracle has a guide on [packages](https://docs.oracle.com/javase/tutorial/java/package/packages.html) if you want to learn more.

When [naming](https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html) your packages, you should use your domain name in reverse order. For example, if your domain name is `papermc.io`,
your package name should be `io.papermc`. If you do not have a domain name, you could use something like your GitHub username.
If you were Linus Torvalds, your package would be `io.github.torvalds`.

This is then followed by the name of your project.
For example, if your project was called `ExamplePlugin`, your package would be `io.github.torvalds.exampleplugin`.
This allows for a unique package name for every plugin.

### The _main_ class

The main class is the entry point to your plugin and will be the only class that extends
[`JavaPlugin`](jd:paper:org.bukkit.plugin.java.JavaPlugin) in your plugin.
This is an example of what your `ExamplePlugin` class could look like:

```java title="ExamplePlugin.java"
package io.papermc.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin implements Listener {
  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));
  }
}
```

### Setting up the `resources`

The `resources` directory is where you will place your plugin's `plugin.yml` file. See the [Plugin YML](/paper/dev/plugin-yml) page for more information.

## Using the Minecraft Development IntelliJ plugin

Alternatively, you can use the [Minecraft Development IntelliJ plugin](https://plugins.jetbrains.com/plugin/8327-minecraft-development)
to create a new project.

:::note

This tutorial only works with IntelliJ IDEA. If you are using another IDE, please follow the manual project setup guide described above.

:::

### Installing the Minecraft Development plugin
The first thing you need to do is install the [Minecraft Development](https://plugins.jetbrains.com/plugin/8327-minecraft-development) plugin.
You can do this by going to `File > Settings > Plugins` and searching for `Minecraft Development` under the `Marketplace` section.

![](./assets/installing-plugin.png)

Once you have installed the plugin, you will need to restart IntelliJ.
To do that you can click the `Restart IDE` button that appears after installing the plugin.

![](./assets/restart-ide.png)

### Creating a new project
Once you have installed the plugin, you can create a new project by going to `File > New > Project...` and selecting `Minecraft` from the list of options.

![](./assets/new-project-paper.png)

You will be asked to provide some information about your project.

| Field                 | Explanation                                                                                                                                                                                                                                                          |
|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name**              | The name of your project.                                                                                                                                                                                                                                            |
| **Location**          | The location of your project. This is where the project files will be stored.                                                                                                                                                                                        |
| **Platform Type**     | The platform type you are developing for. This should be `Plugin`.                                                                                                                                                                                                   |
| **Platform**          | The platform you are developing for. This should be `Paper`.                                                                                                                                                                                                         |
| **Minecraft Version** | The version of Minecraft you are developing for.                                                                                                                                                                                                                     |
| **Plugin Name**       | The name of your plugin.                                                                                                                                                                                                                                             |
| **Main Class**        | The main class of your plugin. This should be the class that extends `JavaPlugin`.                                                                                                                                                                                   |
| **Optional Settings** | Here you can define things like authors, website, description, etc. These are optional and not required for the plugin to work.                                                                                                                                      |
| **Build System**      | The build system you want to use. Paper recommends using Gradle but you can use Maven if you prefer.                                                                                                                                                                 |
| **Paper Manifest**    | Whether you want to use the new Paper plugins or not. For now this is not recommended as it is still in development.                                                                                                                                                 |
| **Group ID**          | The group ID of your project. This is used for Maven and Gradle. This is usually your domain name in reverse. If you don't know what you should put here, you can use something like `io.github.<yourname>` or if you don't have GitHub you can use `me.<yourname>`. |
| **Artifact ID**       | The artifact ID of your project. This is used for Maven and Gradle. This is usually the name of your project. This is usually the same as the `Name` field.                                                                                                          |
| **Version**           | The version of your project. This is used for Maven and Gradle. This is usually `1.0-SNAPSHOT` and does not really matter for now.                                                                                                                                   |
| **JDK**               | The JDK you want to use. This can be anything from Java 21 and above.                                                                                                                                                                                                |

Now you can click on the `Create` button and IntelliJ will create the project for you.
If everything went well, you should see something like this:

![](./assets/paper-plugin-overview.png)

## Plugin remapping

As of 1.20.5, Paper ships with a Mojang-mapped runtime instead of reobfuscating the server to Spigot mappings.
If you are using Spigot/Bukkit plugins, your plugin will be assumed to be Spigot-mapped.
This means that the server will have to deobfuscate and remap the plugin JAR when it's loaded for the first time.

:::note

`paperweight-userdev` already sets this attribute automatically. For more information see the [userdev](/paper/dev/userdev) documentation.

:::

### Mojang mappings

To tell the server that your plugin is Mojang-mapped, you need to add the following code to your build script:

:::note[Paper plugins]

If you are using Paper plugins, this step is not needed as plugins will be assumed to be Mojang-mapped.

:::

<Tabs>
  <TabItem label="Gradle Kotlin DSL">
    ```kotlin
    tasks.jar {
      manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
      }
    }
    // if you have shadowJar configured
    tasks.shadowJar {
      manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
      }
    }
    ```
  </TabItem>
  <TabItem label="Maven POM">
    ```xml
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-jar-plugin</artifactId>
      <version>3.4.1</version>
      <configuration>
        <archive>
          <manifestEntries>
            <paperweight-mappings-namespace>mojang</paperweight-mappings-namespace>
          </manifestEntries>
        </archive>
      </configuration>
    </plugin>
    ```
  </TabItem>
</Tabs>

### Spigot mappings

If you explicitly want to tell the server that your plugin is Spigot-mapped, you need to add the following code to your build script:

<Tabs>
  <TabItem label="Gradle Kotlin DSL">
    ```kotlin
    tasks.jar {
      manifest {
        attributes["paperweight-mappings-namespace"] = "spigot"
      }
    }
    // if you have shadowJar configured
    tasks.shadowJar {
      manifest {
        attributes["paperweight-mappings-namespace"] = "spigot"
      }
    }
    ```
  </TabItem>
  <TabItem label="Maven POM">
    ```xml
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-jar-plugin</artifactId>
      <version>3.4.1</version>
      <configuration>
        <archive>
          <manifestEntries>
            <paperweight-mappings-namespace>spigot</paperweight-mappings-namespace>
          </manifestEntries>
        </archive>
      </configuration>
    </plugin>
    ```
  </TabItem>
</Tabs>

## Conclusion

You should now have a project set up with Paper as a dependency.
All you have left to do now is to compile your plugin and run it on a Paper server.

:::note

If you want to streamline the process of testing a plugin, you can use the [Run-Task](https://github.com/jpenilla/run-task) Gradle plugin.
It will automatically download a Paper server and run it for you.

:::

:::note

If you are using IntelliJ, you can use the Gradle GUI `Build` menu to compile your plugin - found on the top right of your IDE.
The output JAR of your plugin will be in the `build/libs` directory.

:::


# How plugins work

Plugins are a way to extend the functionality of a Minecraft server. They are written in JVM-based languages such as
Java, Kotlin, Groovy or Scala. Plugins are loaded from the `plugins` folder in the server directory. Plugins will be
loaded from a `.jar` file.  Each plugin has a main class that is specified in the plugin's `plugin.yml` file. This
class must extend JavaPlugin, and is the entry point for the plugin and is where the plugin's lifecycle methods are
defined.

:::caution

We do not recommend writing code inside your main class's constructor as there are no guarantees about what
API is available at that point. Instead, you should use the `onLoad` method to initialize your plugin. Also,
do not call your plugin's constructor directly. This will cause issues with your plugin.

:::

## Plugin lifecycle

Plugins are loaded and unloaded at runtime. When a plugin is loaded, it is initialized and enabled. When a plugin is
unloaded, it is disabled and finalized.

### Initialization

When a plugin is loaded, it is initialized. This means that the plugin is loaded into memory and its `onLoad`
method is called. This method is used to initialize the plugin and set up any resources that it needs. Most of the
Bukkit API is not available at this point, so it is not safe to interact with it.

### Enabling

When a plugin is enabled, its `onEnable` method is called. This method is used to set up any resources that the plugin
needs to run. This method is called when the plugin is initialized but before the server has started ticking, so it is
safe to register event listeners and other resources that the plugin needs to run, however often not safe to interact
with a lot of APIs.

This is when you can also open database connections, start threads, and other things that are not safe to do in the
`onLoad` method.

### Disabling

When a plugin is disabled, its `onDisable` method is called. This method is used to clean up any resources that the
plugin has allocated. This method is called before all plugins are unloaded, and is meant for any cleanup that needs to
be done before the plugin is unloaded. This may include saving data to disk or closing connections to databases.

## Event listeners

Events are a way for plugins to listen to things that happen in the server and run code when they are fired. For
example, [`PlayerJoinEvent`](jd:paper:org.bukkit.event.player.PlayerJoinEvent) is fired when a player
joins the server. This is a more performant way to run code when something happens, as opposed to constantly checking.
See our [event listener page](/paper/dev/event-listeners) for more.

Some events are cancellable. This means that when the event is fired, it can be cancelled which negates or stops the
effect of the event. For example, [`PlayerMoveEvent`](jd:paper:org.bukkit.event.player.PlayerMoveEvent)
is cancellable. This means that when it is cancelled, the player will not move. This is useful for things like anti-cheat,
where you want to cancel the event if the player is moving too fast.

It is important to think about how "hot" an event is when writing event listeners. A "hot" event is an event that is fired
very often. For example, `PlayerMoveEvent` is fired every time a player moves. This means that if you have a lot of
expensive code in your event listener, it will be run every time a player moves. This can cause a lot of lag. It is
important to keep event listeners as lightweight as possible. One possible way is to quickly check if the event should
be handled, and if not, return. For example, if you only want to handle the event if the player is moving from one block
to another, you can check if the player's location has changed blocks. If it hasn't, you can return from the listener.

## Commands

Commands are a way for players, the console, RCON and command blocks to run code on the server. Commands are registered
by plugins and can be run by command senders. For example, the `/help` command is registered by the server and can be
run by players. Commands can be run by players by typing them in the chat or by running them from a command block.

Commands can have arguments. For example, the `/give` command takes an argument for the player to give the item to and
an argument for the item to give. Arguments are separated by spaces. For example, the command `/give Notch diamond` will
give the player named Notch a diamond. Note here that the arguments are `["Notch", "diamond"]`.

### Permissions

Permissions are a way to control who can run commands and who can listen to events. Permissions
are registered by plugins and can be checked by other plugins. Permissions can be granted to players and groups.
Permissions can have a hierarchical nature, if defined so by the plugin in their `plugin.yml`. For example, a
plugin can define `example.command.help` as a sub-permission of `example.command`. This means that if a player
has the `example.command` permission, they will also have the `example.command.help` permission.

:::note

Permission plugins can allow the usage of wildcard permissions using the `*` character to grant any permission
or sub-permission available, allowing hierarchical permissions even if not set by the plugin itself. For example,
granting `example.command.*` through a permission plugin with wildcard support will grant access to all permissions
starting with `example.command.` itself.

It is **not** recommended to use wildcard permissions, especially `*` (All permissions), as it can be a huge
security risk, as well as potentially causing unwanted side effects to a player. Use with caution.

:::

## Configuration

Plugins can have configuration files. These files are used to store data that the plugin needs to run. For example, a
plugin that adds a new block to the game might have a configuration file that stores the block's ID. Configuration files
should be stored in the plugin's data folder, within the `plugins` folder. The server offers a YAML configuration API
that can be used to read and write configuration files. See [here](/paper/dev/plugin-configurations) for more information.

## Scheduling tasks

Plugins can schedule tasks to run at a later time. This is useful for things like running code after a certain amount
of time has passed. For example, a plugin might want to run code after 5 seconds. This can be done by scheduling a task
to run after 100 ticks - one second is 20 ticks during normal operation. It is important to note that tasks might be
delayed if the server is lagging. For example, if the server is only running at 10 ticks per second, a task that is
scheduled to run after 100 ticks will take 10 seconds.

In Java, typically you could use [`Thread#sleep()`](jd:java:java.lang.Thread#sleep(long)) to delay
the execution of code. However, if the code is running on the main thread, this will cause the server to pause for the
delay. Instead, you should use the `Scheduler` API to schedule tasks to run later.
Learn more about the `Scheduler` API [here](/paper/dev/scheduler).

## Components

Since Minecraft 1.7 and the introduction of "components", plugins can now send messages to players that contain
rich text. This means that plugins can send messages that contain things like colors, bold text, and clickable links.
Colors were always possible, but only through the use of legacy color codes.

Paper implements a library called `Adventure` that makes it easy to create and send messages to players. Learn more
about the `Adventure` API [here](https://docs.advntr.dev/) from their docs or our docs
[here](/paper/dev/component-api/introduction).


# Paper plugins

Paper plugins allow developers to take advantage of more modern concepts introduced by Mojang, such as datapacks, to
expand the field of what the Paper API is able to introduce.

:::danger[Experimental]

This is experimental and may be subject to change.

:::

- [Bootstrapper](#bootstrapper)
- [Loader](#loaders)
- [Differences](#differences)

## How do I use them?
Similarly to Bukkit plugins, you have to introduce a `paper-plugin.yml` file into your JAR resources folder.
This will not act as a drop-in replacement for `plugin.yml`, as some things, as outlined in this guide, need to be declared differently.

It should be noted that you still have the ability to include both `paper-plugin.yml` and `plugin.yml` in the same JAR.

Here is an example configuration:
```yaml replace
name: Paper-Test-Plugin
version: '1.0'
main: io.papermc.testplugin.TestPlugin
description: Paper Test Plugin
api-version: '\{LATEST_PAPER_RELEASE}'
bootstrapper: io.papermc.testplugin.TestPluginBootstrap
loader: io.papermc.testplugin.TestPluginLoader
```

### Dependency declaration

Paper plugins change how to declare dependencies in your `paper-plugin.yml`:

```yml
dependencies:
  bootstrap:
    # Let's say that RegistryPlugin registers some data that your plugin needs to use
    # We don't need this during runtime, so it's not required in the server section.
    # However, can be added to both if needed
    RegistryPlugin:
      load: BEFORE
      required: true
      join-classpath: true # Defaults to true
  server:
    # Add a required "RequiredPlugin" dependency, which will load AFTER your plugin.
    RequiredPlugin:
      load: AFTER
      required: true
      # This means that your plugin will not have access to their classpath
      join-classpath: false
```

With Paper plugins, dependencies are split into two sections:
- `bootstrap` - These are dependencies that you will be using in the [bootstrap](#bootstrapper).
- `server` - These are dependencies that are used for the core functionality of your plugin, whilst the server is running.

Let's take a look at a dependency:
```yml
RegistryPlugin:
  load: BEFORE # Defaults to OMIT
  required: true # Defaults to true
  join-classpath: true # Defaults to true
```

- `load` (`BEFORE`|`AFTER`|`OMIT`): Whether this plugin should load before or after **your** plugin. Note: `OMIT` has undefined ordering behavior.
- `required`: Whether this plugin is required for your plugin to load.
- `join-classpath`: Whether your plugin should have access to their classpath. This is used for plugins that need to access other plugins internals directly.

:::note[Cyclic Loading]

Note that in certain cases, plugins may be able to introduce cyclic loading loops, which will prevent the server from starting.
Please read the [cyclic loading guide](#cyclic-plugin-loading) for more information.

:::

Here are a couple of examples:
```yml
# Suppose we require ProtocolLib to be loaded for our plugin
ProtocolLib:
  load: BEFORE
  required: true

# Now, we are going to register some details for a shop plugin
# So the shop plugin should load after our plugin
SuperShopsXUnlimited:
  load: AFTER
  required: false

# Now, we are going to need to access a plugins classpath
# So that we can properly interact with it.
SuperDuperTacoParty:
  required: true
  join-classpath: true
```

## What is it used for?
Paper plugins lay down the framework for some future API. Our goals are to open more modern API that better aligns
with Vanilla. Paper plugins allow us to do just that by making a new way to load plugin resources before the server
has started by using [bootstrappers](#bootstrapper).

## Bootstrapper
Paper plugins are able to identify their own bootstrapper by implementing
[`PluginBootstrap`](jd:paper:io.papermc.paper.plugin.bootstrap.PluginBootstrap)
and adding the class of your implementation to the bootstrapper field in the `paper-plugin.yml`.
```java title="TestPluginBootstrap.java"
public class TestPluginBootstrap implements PluginBootstrap {

  @Override
  public void bootstrap(BootstrapContext context) {

  }

  @Override
  public JavaPlugin createPlugin(PluginProviderContext context) {
    return new TestPlugin("My custom parameter");
  }

}
```
A bootstrapper also allows you to change the way your plugin is initialized, allowing you to pass values into your plugin constructor.
Currently, bootstrappers do not offer much new API and are highly experimental. This may be subject to change once more API is introduced.

## Loaders
Paper plugins are able to identify their own plugin loader by implementing
[`PluginLoader`](jd:paper:io.papermc.paper.plugin.loader.PluginLoader)
and adding the class of your implementation to the loader field in the `paper-plugin.yml`.

The goal of the plugin loader is the creation of an expected/dynamic environment for the plugin to load into.
This, as of right now, only applies to creating the expected classpath for the plugin, e.g. supplying external libraries to the plugin.
```java title="TestPluginLoader.java"
public class TestPluginLoader implements PluginLoader {

  @Override
  public void classloader(PluginClasspathBuilder classpathBuilder) {
    classpathBuilder.addLibrary(new JarLibrary(Path.of("dependency.jar")));

    MavenLibraryResolver resolver = new MavenLibraryResolver();
    resolver.addDependency(new Dependency(new DefaultArtifact("com.example:example:version"), null));
    resolver.addRepository(new RemoteRepository.Builder("paper", "default", "https://repo.papermc.io/repository/maven-public/").build());

    classpathBuilder.addLibrary(resolver);
  }
}
```
Currently, you are able to add two different library types:
[`JarLibrary`](jd:paper:io.papermc.paper.plugin.loader.library.impl.JarLibrary) and
[`MavenLibraryResolver`](jd:paper:io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver).

:::danger

If you wish to resolve libraries from Maven Central, use a mirror, as using Maven Central directly as a CDN is against the Maven Central Terms of Service, and users of your plugin may hit rate limits.

You should use Paper's default mirror, configured by the [`PAPER_DEFAULT_CENTRAL_REPOSITORY`](/paper/reference/system-properties#paper_default_central_repository) environment variable and [`org.bukkit.plugin.java.LibraryLoader.centralURL`](/paper/reference/system-properties#orgbukkitpluginjavalibraryloadercentralurl) system property:
```java
resolver.addRepository(new RemoteRepository.Builder("central", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR).build());
```

Using the Maven Central repository (i.e. `*.maven.org` or `*.maven.apache.org`) will cause a warning to be shown in the console.

:::

## Differences

### Bukkit serialization system
Paper plugins still support the serialization system (`org.bukkit.configuration.serialization`) that Bukkit uses. However, custom classes will not be
automatically registered for serialization. In order to use [`ConfigurationSection#getObject`](jd:paper:org.bukkit.configuration.ConfigurationSection#getObject(java.lang.String,java.lang.Class)),
you **must** call [`ConfigurationSerialization#registerClass(Class)`](jd:paper:org.bukkit.configuration.serialization.ConfigurationSerialization#registerClass(java.lang.Class))
before you attempt to fetch objects from configurations.

### Classloading isolation
Paper plugins are not able to access each other unless given explicit access by depending on another plugin, etc. This
helps prevent Paper plugins from accidentally accessing each other's dependencies, and in general helps ensure that
plugins are only able to access what they explicitly depend on.

Paper plugins have the ability to bypass this, being able to access OTHER plugins' classloaders by adding a `join-classpath` option to their `paper-plugin.yml`.

```yml
Plugin:
  join-classpath: true # Means you have access to their classpath
```

Note, other Paper plugins will still be unable to access your classloader.

### Load order logic split
In order to better take advantage of classloading isolation, Paper plugins do **not** use the `dependencies` field to determine load order.
This was done for a variety of reasons, mostly to allow better control and allow plugins to properly share classloaders.

See [declaring dependencies](#dependency-declaration) for more information on how to declare the load order of your plugin.

### Commands
Paper plugins do not use the `commands` field to register commands. This means that you do not need to include all
of your commands in the `paper-plugin.yml` file. Instead, you can register commands using the
[Brigadier Command API](/paper/dev/command-api/basics/introduction).

### Cyclic plugin loading

Cyclic loading describes the phenomenon when a plugin loading causes a loop that eventually cycles back to the original plugin.
Unlike Bukkit plugins, Paper plugins will not attempt to resolve cyclic loading issues.

```d2
style.fill: transparent
direction: right

A -> B
B -> C
C -> D
D -> A
```

However, if Paper detects a loop that cannot be resolved, you will get an error that looks like this:
```
[ERROR]: [LoadOrderTree] =================================
[ERROR]: [LoadOrderTree] Circular plugin loading detected:
[ERROR]: [LoadOrderTree] 1) Paper-Test-Plugin1 -> Paper-Test-Plugin -> Paper-Test-Plugin1
[ERROR]: [LoadOrderTree]    Paper-Test-Plugin1 loadbefore: [Paper-Test-Plugin]
[ERROR]: [LoadOrderTree]    Paper-Test-Plugin loadbefore: [Paper-Test-Plugin1]
[ERROR]: [LoadOrderTree] Please report this to the plugin authors of the first plugin of each loop or join the PaperMC Discord server for further help.
[ERROR]: [LoadOrderTree] =================================
```

It is up to you to resolve these cyclical loading issues.


# plugin.yml

import { FileTree } from "@astrojs/starlight/components";
import { LATEST_PAPER_RELEASE } from "/src/utils/versions";

The `plugin.yml` file is the main configuration file for your plugin.
It contains information about your plugin, such as its name, version, and description.
It also contains information about the plugin's dependencies, permissions, and commands.

The `plugin.yml` file is located in the `resources` directory of your project.

<FileTree>
  - example-plugin/
    - build.gradle.kts
    - settings.gradle.kts
    - src/
      - main/
        - java/
        - resources/
          - **plugin.yml**
</FileTree>

## Example

Here is an example of a `plugin.yml` file:

```yaml replace
name: ExamplePlugin
version: 1.0.0
main: io.papermc.testplugin.ExamplePlugin
description: An example plugin
author: PaperMC
website: https://papermc.io
api-version: '\{LATEST_PAPER_RELEASE}'
```

## Fields

:::note

The fields in this section are not in any particular order.
If they have an asterisk (\*) next to them, that means they are required.

:::

### name*

The name of your plugin. This is what will be displayed in the plugin list and log messages.
This will be overridden in the logs if the prefix is set.
- `name: ExamplePlugin`

### version*

The current version of the plugin. This is shown in plugin info messages and server logs.
- `version: 1.0.0`

### main*

The main class of your plugin. This is the class that extends `JavaPlugin` and is the entry point to your plugin.
- `main: io.papermc.testplugin.ExamplePlugin`

This is the package path and class name of your main class.

### description

A short description of your plugin and what it does. This will be shown in the plugin info commands.
- `description: An example plugin`

### author / authors

The author(s) of the plugin. This can be a single author or a list of authors.
- `author: PaperMC`
- `authors: [PaperMC, SpigotMC, Bukkit]`
These will be shown in the plugin info commands.

### contributors

The contributors to the plugin that aren't the managing author(s).
- `contributors: [PaperMC, SpigotMC, Bukkit]`
These will be shown in the plugin info commands.

### website

The website of the plugin. This is useful for linking to a GitHub repository or a plugin page.
- `website: https://papermc.io`
This will be shown in the plugin info commands.

### api-version

The version of the Paper API that your plugin is using. This doesn't include the minor version until 1.20.5. From 1.20.5 and onward, a minor version is supported.
Servers with a version lower than the version specified here will refuse to load the plugin.
The valid versions are 1.13 - {LATEST_PAPER_RELEASE}.
- `api-version: '\{LATEST_PAPER_RELEASE}'`

:::note

If this is not specified, the plugin will be loaded as a legacy plugin and a warning will be printed to the console.

:::

### load

This tells the server when to load the plugin. This can be `STARTUP` or `POSTWORLD`. Will default to `POSTWORLD` if not specified.
- `load: STARTUP`

### prefix

The prefix of the plugin. This is what will be displayed in the log instead of the plugin name.
- `prefix: EpicPaperMCHypePlugin`

### libraries

This is a list of libraries that your plugin depends on. These libraries will be downloaded from the Maven Central repository and added to the classpath.
This removes the need to shade and relocate the libraries.

```yaml
libraries:
  - com.google.guava:guava:30.1.1-jre
  - com.google.code.gson:gson:2.8.6
```

:::note

The central repository is configurable using the [`PAPER_DEFAULT_CENTRAL_REPOSITORY`](/paper/reference/system-properties#paper_default_central_repository) environment variable and [`org.bukkit.plugin.java.LibraryLoader.centralURL`](/paper/reference/system-properties#orgbukkitpluginjavalibraryloadercentralurl) system property.

:::

### permissions

This is a list of permissions that your plugin uses. This is useful for plugins that use permissions to restrict access to certain features.
```yaml
permissions:
  permission.node:
    description: "This is a permission node"
    default: op
    children:
      permission.node.child: true
  another.permission.node:
    description: "This is another permission node"
    default: notop
```

The description is the description of the permission node. This is what will be displayed in the permissions list.
The default is the default value of the permission node. This can be `op`/`notop` or `true`/`false`.
This defaults to the value of `default-permission` if not specified, which in turn defaults to `op`.
Each permission node can have children. When set to `true`, it will inherit the parent permission.

### default-permission

The default value that permissions that don't have a `default` specified will have. This can be `op`/`notop` or `true`/`false`.
- `default-permission: true`

### paper-plugin-loader

A fully qualified class name of a Paper plugin [loader](/paper/dev/getting-started/paper-plugins#loaders) class, if you want to use one.
- `paper-plugin-loader: com.example.paperplugin.MyPluginLoader`

:::danger[Experimental]

[Paper plugins](/paper/dev/getting-started/paper-plugins) (and therefore plugin loaders) are experimental and may be subject to change.

:::

### paper-skip-libraries

If `true`, Paper will skip resolution of libraries defined in the [`libraries` section](#libraries).
This is useful for delegating library loading to a Paper plugin [loader](/paper/dev/getting-started/paper-plugins#loaders).
- `paper-skip-libraries: false`

:::danger[Experimental]

[Paper plugins](/paper/dev/getting-started/paper-plugins) (and therefore plugin loaders) are experimental and may be subject to change.

:::

## Commands

This is a list of commands that your plugin uses. This is useful for plugins that use commands to provide features.
```yaml
commands:
  command:
    description: "This is a command"
    usage: "/command <arg>"
    aliases: [cmd, command]
    permission: permission.node
    permission-message: "You do not have permission to use this command"
```

- `description` is the description of the command. This gives a brief description of what the command does.
- `usage` is the usage of the command. This is what will be displayed when the player uses `/help <command>`.
- `aliases` are a list of aliases that the command can be used with. This is useful for shortening the command.
- `permission` is the permission node that the player needs to use the command. Note: Players will only see commands they have permission to use.
- `permission-message` is the message that will be displayed when the player does not have permission to use the command.

## Dependencies

:::caution[Dependency Loops]

If a plugin is specified as a dependency, it will be loaded before your plugin.
Be careful as these can cause plugin load issues if cyclical dependencies appear. A cyclical dependency can be illustrated as follows:

```d2
style.fill: transparent
direction: right

"Plugin A" -> "Plugin B"
"Plugin B" -> "Plugin C"
"Plugin C" -> "Plugin D"
"Plugin D" -> "Plugin A"
```

Where `Plugin A` and `Plugin B` are plugins that depend on each other.

:::

### depend

A list of plugins that your plugin depends on to __load__. They are specified by their plugin name.

:::note

If the plugin is not found, your plugin will not load.

:::

- `depend: [Vault, WorldEdit]`

### softdepend

A list of plugins that your plugin depends on to have __full functionality__. They are specified by their plugin name.

- `softdepend: [Vault, WorldEdit]`

### loadbefore

A list of plugins that your plugin should be loaded __before__. They are specified by their plugin name.
This is useful if you want to load your plugin before another plugin for the other plugin to use your plugin's API.

- `loadbefore: [Vault, FactionsUUID]`

### provides

This can be used to tell the server that this plugin will provide the functionality of some library or other plugin (like an alias system).
Plugins that (soft)depend on the other plugin will treat your plugin as if the other plugin exists when resolving dependencies or using
[`PluginManager#getPlugin(String)`](jd:paper:org.bukkit.plugin.PluginManager#getPlugin(java.lang.String)).
- `provides: [SomeOtherPlugin]`


# paperweight-userdev

**paperweight** is the name of Paper's custom build tooling. The **paperweight-userdev** Gradle plugin part of that
provides access to internal code (also known as NMS) during development.

:::note

This guide is written using the Gradle Kotlin DSL and assumes you have some basic knowledge of Gradle.
If you want to see a fully-functioning plugin that uses **paperweight-userdev**,
check out this [example plugin](https://github.com/PaperMC/paperweight-test-plugin).

:::

## Why this is useful
This is the only supported way of accessing server internals, as redistributing the server JAR is against the
Minecraft EULA and general license assumption. Even if you manually depended on the patched server, you would be
hindering other people working on your project and would be missing deployed API javadocs/sources in your IDE.

On top of that, Spigot and pre-1.20.5 Paper versions still use Spigot mappings, which are a mix of obfuscated fields/methods
and mapped as well as custom named classes. This can make it hard to work with in a development environment. This plugin lets you use
fully deobfuscated types, names, and fields during development, and then remaps your plugin, so it can still be used with the obfuscated
server. However, this does not apply to reflection. Look at something like [this library](https://github.com/jpenilla/reflection-remapper) to be able to
use non-obfuscated names in reflection if you want to support obfuscated servers.

:::note[1.20.5 Mojang-mapped runtime]

As of Minecraft version 1.20.5, Paper ships with a Mojang-mapped runtime instead of re-obfuscating the server to Spigot mappings.
See [here](#1205-and-beyond) for more details.

:::

## Adding the plugin
Add the plugin to your `build.gradle.kts` file.

```kts title="build.gradle.kts" replace
plugins {
  id("io.papermc.paperweight.userdev") version "\{LATEST_USERDEV_RELEASE}"
}
```

:::note[Gradle Version]

Please make sure you are using the latest stable version of Gradle.
For more information on upgrading Gradle, check out the [Gradle Wrapper documentation](https://docs.gradle.org/current/userguide/gradle_wrapper.html).

:::

The latest version of `paperweight-userdev` supports dev bundles for Minecraft 1.17.1 and newer, so it's best practice to keep it up to date!
Only the latest version of `paperweight-userdev` is officially supported, and we will ask you to update first if you are having problems with old versions.
Furthermore, if you are having issues with `paperweight-userdev`, you should ask in the
[`#build-tooling-help`](https://discord.com/channels/289587909051416579/1078993196924813372) channel in our dedicated [Discord server](https://discord.gg/PaperMC)!

:::note[Snapshots]

**paperweight-userdev** SNAPSHOT (pre-release) versions are only available through Paper's Maven repository.
```kotlin title="settings.gradle.kts"
pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
  }
}
```

:::

## Adding the dev bundle dependency
If you try to load your Gradle project now, you will receive an error saying you have to declare
a dev bundle dependency. You can do that by adding to your `dependencies` block in your `build.gradle.kts`
file.

```kotlin title="build.gradle.kts" replace
dependencies {
  // Other Dependencies
  paperweight.paperDevBundle("\{LATEST_PAPER_RELEASE}-R0.1-SNAPSHOT")
}
```

:::tip

You should remove any dependency on the Paper API, as the dev bundle includes that.

:::

## Gradle tasks

### `reobfJar`

This task creates a plugin JAR that is re-obfuscated to Spigot's runtime mappings.
This means it will work on standard Paper servers.

The output will be inside the `build/libs` folder. The JAR whose filename includes `-dev`
is Mojang-mapped (not re-obfuscated) and will not work on most servers.

:::note[Shadow]

If you have the shadow Gradle plugin applied in your build script, **paperweight-userdev** will
detect that and use the shaded JAR as the input for the `reobfJar` task.

The `-dev-all.jar` file in `build/libs` is the shaded, but not re-obfuscated JAR.

:::

You can make the `reobfJar` task run on the default `build` task with:
```kotlin
tasks.assemble {
  dependsOn(tasks.reobfJar)
}
```

## 1.20.5 and beyond

As of 1.20.5, Paper ships with a Mojang-mapped runtime instead of re-obfuscating the server to Spigot mappings.
Additionally, CraftBukkit classes will no longer be relocated into a versioned package.
This requires plugins to be deobfuscated before loading when necessary.

Most of this process is done automatically by paperweight, but there are some important things to know when using server internals (or "NMS") from now on.

### Default mappings assumption
* By default, all Spigot/Bukkit plugins will be assumed to be Spigot-mapped if they do not specify their mappings namespace in the manifest.
  The other way around, all Paper plugins will be assumed to be Mojang-mapped if they do not specify their mappings namespace in the manifest.
* Spigot-mapped plugins will need to be deobfuscated on first load, Mojang-mapped plugins will not.

### Compiling to Mojang mappings

:::note

This is the preferred option, as the one-time plugin remapping process during server startup will be skipped and it
may allow you to keep version compatibility across smaller updates without changes or additional modules.
However, this makes your plugin incompatible with Spigot servers.

:::

If you want your main output to use Mojang mappings, you need to remove all `dependsOn(reobfJar)` lines and add the following code to your build script:

```kotlin title="build.gradle.kts"
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
```

### Compiling to Spigot mappings

If you want your main output to use Spigot mappings, add the following code to your build script:

```kotlin title="build.gradle.kts"
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION
```

This is useful for plugins that have loaders for both Spigot and Paper and want to keep compatibility with both.

:::note

If you are using Gradle with the Groovy DSL, you should instead access the fields via static methods like `getMOJANG_PRODUCTION()`.

:::


# documentação completa em:
https://github.com/PaperMC/docs/tree/main/src/content/docs/paper/dev