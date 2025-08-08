# MinecraftForge Documentation
============================

This is the official documentation for [MinecraftForge], the Minecraft modding API.

This documentation is _only_ for Forge, **this is not a Java tutorial**.

[MinecraftForge]: http://minecraftforge.net


Getting Started with Forge
==========================

If you have never made a Forge mod before, this section will provide the minimum amount of information needed to setup a Forge development environment. The rest of the documentation is about where to go from here.

# Prerequisites
-------------

* An installation of the Java 17 Development Kit (JDK) and 64-bit Java Virtual Machine (JVM). Forge recommends and officially supports [Eclipse Temurin][jdk].

    !!! warning
        Make sure you are using a 64-bit JVM. One way of checking is to run `java -version` in a terminal. Using a 32-bit JVM will cause some problems when using [ForgeGradle].

* Familiarity with an Integrated Development Environment (IDE).
    * It is recommended to use an IDE with Gradle integration.

From Zero to Modding
--------------------

1. Download the Mod Developer Kit (MDK) from the [Forge file site][files] by clicking 'Mdk' followed by the 'Skip' button in the top right after waiting for a period of time. It is recommended to download the latest version of Forge whenever possible.
1. Extract the downloaded MDK into an empty directory. This will be your mod's directory, which should now contain some gradle files and a `src` subdirectory containing the example mod.

    !!! note
        A number of files can be reused across different mods. These files are:

        * the `gradle` subdirectory
        * `build.gradle`
        * `gradlew`
        * `gradlew.bat`
        * `settings.gradle`

        The `src` subdirectory does not need to be copied across workspaces; however, you may need to refresh the Gradle project if the java (`src/main/java`) and resource (`src/main/resources`) are created later.

1. Open your selected IDE:
    * Forge only explicitly supports development on Eclipse and IntelliJ IDEA, but there are additional run configurations for Visual Studio Code. Regardless, any environment, from Apache NetBeans to Vim / Emacs, can be used.
    * Eclipse and IntelliJ IDEA's Gradle integration, both installed and enabled by default, will handle the rest of the initial workspace setup on import or open. This includes downloading the necessary packages from Mojang, MinecraftForge, etc. The 'Gradle for Java' plugin is needed for Visual Studio Code to do the same.
    * Gradle will need to be invoked to re-evaluate the project for almost all changes to its associated files (e.g., `build.gradle`, `settings.gradle`, etc.). Some IDEs come with 'Refresh' buttons to do this; however, it can be done through the terminal via `gradlew`.
1. Generate run configurations for your selected IDE:
    * **Eclipse**: Run the `genEclipseRuns` task.
    * **IntelliJ IDEA**: Run the `genIntellijRuns` task. If a "module not specified" error occurs, set the [`ideaModule` property][config] to your 'main' module (typically `${project.name}.main`).
    * **Visual Studio Code**: Run the `genVSCodeRuns` task.
    * **Other IDEs**: You can run the configurations directly using `gradle run*` (e.g., `runClient`, `runServer`, `runData`, `runGameTestServer`). These can also be used with the supported IDEs.

Customizing Your Mod Information
--------------------------------

Edit the `build.gradle` file to customize how your mod is built (e.g., file name, artifact version, etc.).

!!! important
    Do **not** edit the `settings.gradle` unless you know what you are doing. The file specifies the repository that [ForgeGradle] is uploaded to.

### Recommended `build.gradle` Customizations

#### Mod Id Replacement

Replace all occurrences of `examplemod`, including [`mods.toml` and the main mod file][modfiles] with the mod id of your mod. This also includes changing the name of the file you build by setting `base.archivesName` (this is typically set to your mod id).

```gradle
// In some build.gradle
base.archivesName = 'mymod'
```

#### Group Id

The `group` property should be set to your [top-level package][packaging], which should either be a domain you own or your email address:

Type      | Value             | Top-Level Package
:---:     | :---:             | :---
Domain    | example.com       | `com.example`
Subdomain | example.github.io | `io.github.example`
Email     | example@gmail.com | `com.gmail.example`

```gradle
// In some build.gradle
group = 'com.example'
```

The packages within your java source (`src/main/java`) should also now conform to this structure, with an inner package representing the mod id:

```text
com
- example (top-level package specified in group property)
  - mymod (the mod id)
    - MyMod.java (renamed ExampleMod.java)
```

#### Version

Set the `version` property to the current version of your mod. We recommend using a [variation of Maven versioning][mvnver].

```gradle
// In some build.gradle
version = '1.20-1.0.0.0'
```

### Additional Configurations

Additional configurations can be found on the [ForgeGradle] docs.

Building and Testing Your Mod
-----------------------------

1. To build your mod, run `gradlew build`. This will output a file in `build/libs` with the name `[archivesBaseName]-[version].jar`, by default. This file can be placed in the `mods` folder of a Forge-enabled Minecraft setup or distributed.
1. To run your mod in a test environment, you can either use the generated run configurations or use the associated tasks (e.g. `gradlew runClient`). This will launch Minecraft from the run directory (default 'run') along with any source sets specified. The default MDK includes the `main` source set, so any code written in `src/main/java` will be applied.
1. If you are running a dedicated server, whether through the run configuration or `gradlew runServer`, the server will initially shut down immediately. You will need to accept the Minecraft EULA by editing the `eula.txt` file in the run directory. Once accepted, the server will load, which can then be accessed via a direct connect to `localhost`.

!!! note
    You should always test your mod in a dedicated server environment. This includes [client-only mods][client] as they should not do anything when loaded on the server.

[jdk]: https://adoptium.net/temurin/releases?version=17 "Eclipse Temurin 17 Prebuilt Binaries"
[ForgeGradle]: https://docs.minecraftforge.net/en/fg-6.x

[files]: https://files.minecraftforge.net "Forge Files distribution site"
[config]: https://docs.minecraftforge.net/en/fg-6.x/configuration/runs/

[modfiles]: ./modfiles.md
[packaging]: ./structuring.md#packaging
[mvnver]: ./versioning.md
[client]: ../concepts/sides.md#writing-one-sided-mods


# Mod Files
=========

The mod files are responsible for determining what mods are packaged into your JAR, what information to display within the 'Mods' menu, and how your mod should be loaded in the game.

mods.toml
---------

The `mods.toml` file defines the metadata of your mod(s). It also contains additional information that is displayed within the 'Mods' menu and how your mod(s) should be loaded into the game.

The file uses the [Tom's Obvious Minimal Language, or TOML][toml], format. The file must be stored under the `META-INF` folder in the resource directory of the source set you are using (`src/main/resources/META-INF/mods.toml` for the `main` source set). A `mods.toml` file may look something like this:

```toml
modLoader="javafml"
loaderVersion="[46,)"

license="All Rights Reserved"
issueTrackerURL="https://github.com/MinecraftForge/MinecraftForge/issues"
showAsResourcePack=false

[[mods]]
  modId="examplemod"
  version="1.0.0.0"
  displayName="Example Mod"
  updateJSONURL="https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json"
  displayURL="https://minecraftforge.net"
  logoFile="logo.png"
  credits="I'd like to thank my mother and father."
  authors="Author"
  description='''
  Lets you craft dirt into diamonds. This is a traditional mod that has existed for eons. It is ancient. The holy Notch created it. Jeb rainbowfied it. Dinnerbone made it upside down. Etc.
  '''
  displayTest="MATCH_VERSION"

[[dependencies.examplemod]]
  modId="forge"
  mandatory=true
  versionRange="[46,)"
  ordering="NONE"
  side="BOTH"

[[dependencies.examplemod]]
  modId="minecraft"
  mandatory=true
  versionRange="[1.20]"
  ordering="NONE"
  side="BOTH"
```

`mods.toml` is broken into three parts: the non-mod-specific properties, which are linked to the mod file; the mod properties, with a section for each mod; and the dependency configurations, with a section for each mod's or mods' dependencies. Each of the properties associated with the `mods.toml` file will be explained below, where `required` means that a value must be specified or an exception will be thrown.

### Non-Mod-Specific Properties

Non-mod-specific properties are properties associated with the JAR itself, indicating how to load the mod(s) and any additional global metadata.

Property             | Type    | Default       | Description | Example
:---                 | :---:   | :---:         | :---:       | :---
`modLoader`          | string  | **mandatory** | The language loader used by the mod(s). Can be used to support alternative language structures, such as Kotlin objects for the main file, or different methods of determining the entrypoint, such as an interface or method. Forge provides the Java loader `"javafml"` and low/no code loader `"lowcodefml"`. | `"javafml"`
`loaderVersion`      | string  | **mandatory** | The acceptable version range of the language loader, expressed as a [Maven Version Range][mvr]. For `javafml` and `lowcodefml`, the version is the major version of the Forge version. | `"[46,)"`
`license`            | string  | **mandatory** | The license the mod(s) in this JAR are provided under. It is suggested that this is set to the [SPDX identifier][spdx] you are using and/or a link to the license. You can visit https://choosealicense.com/ to help pick the license you want to use. | `"MIT"`
`showAsResourcePack` | boolean | `false`       | When `true`, the mod(s)'s resources will be displayed as a separate resource pack on the 'Resource Packs' menu, rather than being combined with the 'Mod resources' pack. | `true`
`services`           | array   | `[]`          | An array of services your mod **uses**. This is consumed as part of the created module for the mod from Forge's implementation of the Java Platform Module System. | `["net.minecraftforge.forgespi.language.IModLanguageProvider"]`
`properties`         | table   | `{}`          | A table of substitution properties. This is used by `StringSubstitutor` to replace `${file.<key>}` with its corresponding value. This is currently only used to replace the `version` in the [mod-specific properties][modsp]. | `{ "example" = "1.2.3" }` referenced by `${file.example}`
`issueTrackerURL`    | string  | *nothing*     | A URL representing the place to report and track issues with the mod(s). | `"https://forums.minecraftforge.net/"`

!!! important
    The `services` property is functionally equivalent to specifying the [`uses` directive in a module][uses], which allows [*loading*][serviceload] a service of a given type.

### Mod-Specific Properties

Mod-specific properties are tied to the specified mod using the `[[mods]]` header. This is an [array of tables][array]; all key/value properties will be attached to that mod until the next header.

```toml
# Properties for examplemod1
[[mods]]
modId = "examplemod1"

# Properties for examplemod2
[[mods]]
modId = "examplemod2"
```

Property        | Type    | Default                 | Description | Example
:---            | :---:   | :---:                   | :---:       | :---
`modId`         | string  | **mandatory**           | The unique identifier representing this mod. The id must match `^[a-z][a-z0-9_]{1,63}$` (a string 2-64 characters; starts with a lowercase letter; made up of lowercase letters, numbers, or underscores). | `"examplemod"`
`namespace`     | string  | value of `modId`        | An override namespace for the mod. The namespace much match `^[a-z][a-z0-9_.-]{1,63}$` (a string 2-64 characters; starts with a lowercase letter; made up of lowercase letters, numbers, underscores, dots, or dashes). Currently unused. | `"example"`
`version`       | string  | `"1"`                   | The version of the mod, preferably in a [variation of Maven versioning][mvnver]. When set to `${file.jarVersion}`, it will be replaced with the value of the `Implementation-Version` property in the JAR's manifest (displays as `0.0NONE` in a development environment). | `"1.20-1.0.0.0"`
`displayName`   | string  | value of `modId`        | The pretty name of the mod. Used when representing the mod on a screen (e.g., mod list, mod mismatch). | `"Example Mod"`
`description`   | string  | `"MISSING DESCRIPTION"` | The description of the mod shown in the mod list screen. It is recommended to use a [multiline literal string][multiline]. | `"This is an example."`
`logoFile`      | string  | *nothing*               | The name and extension of an image file used on the mods list screen. The logo must be in the root of the JAR or directly in the root of the source set (e.g., `src/main/resources` for the main source set). | `"example_logo.png"`
`logoBlur`      | boolean | `true`                  | Whether to use `GL_LINEAR*` (true) or `GL_NEAREST*` (false) to render the `logoFile`. | `false`
`updateJSONURL` | string  | *nothing*               | A URL to a JSON used by the [update checker][update] to make sure the mod you are playing is the latest version. | `"https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json"`
`features`      | table   | `{}`                    | See '[features]'. | `{ java_version = "17" }`
`modproperties` | table   | `{}`                    | A table of key/values associated with this mod. Currently unused by Forge, but is mainly for use by mods. | `{ example = "value" }` 
`modUrl`        | string  | *nothing*               | A URL to the download page of the mod. Currently unused. | `"https://files.minecraftforge.net/"`
`credits`       | string  | *nothing*               | Credits and acknowledges for the mod shown on the mod list screen. | `"The person over here and there."`
`authors`       | string  | *nothing*               | The authors of the mod shown on the mod list screen. | `"Example Person"`
`displayURL`    | string  | *nothing*               | A URL to the display page of the mod shown on the mod list screen. | `"https://minecraftforge.net/"`
`displayTest`   | string  | `"MATCH_VERSION"`       | See '[sides]'. | `"NONE"`

#### Features

The features system allows mods to demand that certain settings, software, or hardware are available when loading the system. When a feature is not satisfied, mod loading will fail, informing the user about the requirement. Currently, Forge provides the following features:

Feature        | Description | Example
:---:          | :---:       | :---
`java_version` | The acceptable version range of the Java version, expressed as a [Maven Version Range][mvr]. This should be the supported version used by Minecraft. | `"[17,)"`

### Dependency Configurations

Mods can specify their dependencies, which are checked by Forge before loading the mods. These configurations are created using the [array of tables][array] `[[dependencies.<modid>]]` where `modid` is the identifier of the mod the dependency is for.

Property       | Type    | Default       | Description | Example
:---           | :---:   | :---:         | :---:       | :---
`modId`        | string  | **mandatory** | The identifier of the mod added as a dependency. | `"example_library"`
`mandatory`    | boolean | **mandatory** | Whether the game should crash when this dependency is not met. | `true`
`versionRange` | string  | `""`          | The acceptable version range of the language loader, expressed as a [Maven Version Range][mvr]. An empty string matches any version. | `"[1, 2)"`
`ordering`     | string  | `"NONE"`      | Defines if the mod must load before (`"BEFORE"`) or after (`"AFTER"`) this dependency. If the ordering does not matter, return `"NONE"` | `"AFTER"`
`side`         | string  | `"BOTH"`      | The [physical side][dist] the dependency must be present on: `"CLIENT"`, `"SERVER"`, or `"BOTH"`.| `"CLIENT"`
`referralUrl`  | string  | *nothing*     | A URL to the download page of the dependency. Currently unused. | `"https://library.example.com/"`

!!! warning
    The `ordering` of two mods may cause a crash due to a cyclic dependency: for example, mod A must load `"BEFORE"` mod B and mod B `"BEFORE"` mod A.

Mod Entrypoints
---------------

Now that the `mods.toml` is filled out, we need to provide an entrypoint to being programming the mod. Entrypoints are essentially the starting point for executing the mod. The entrypoint itself is determined by the language loader used in the `mods.toml`.

### `javafml` and `@Mod`

`javafml` is a language loader provided by Forge for the Java programming language. The entrypoint is defined using a public class with the `@Mod` annotation. The value of `@Mod` must contain one of the mod ids specified within the `mods.toml`. From there, all initialization logic (e.g., [registering events][events], [adding `DeferredRegister`s][registration]) can be specified within the constructor of the class. The mod bus can be obtained from `FMLJavaModLoadingContext`.

```java
@Mod("examplemod") // Must match mods.toml
public class Example {

  public Example() {
    // Initialize logic here
    var modBus = FMLJavaModLoadingContext.get().getModEventBus();

    // ...
  }
}
```

### `lowcodefml`

`lowcodefml` is a language loader used as a way to distribute datapacks and resource packs as mods without the need of an in-code entrypoint. It is specified as `lowcodefml` rather than `nocodefml` for minor additions in the future that might require minimal coding.

[toml]: https://toml.io/
[mvr]: https://maven.apache.org/enforcer/enforcer-rules/versionRanges.html
[spdx]: https://spdx.org/licenses/
[modsp]: #mod-specific-properties
[uses]: https://docs.oracle.com/javase/specs/jls/se17/html/jls-7.html#jls-7.7.3
[serviceload]: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ServiceLoader.html#load(java.lang.Class)
[array]: https://toml.io/en/v1.0.0#array-of-tables
[mvnver]: ./versioning.md
[multiline]: https://toml.io/en/v1.0.0#string
[update]: ../misc/updatechecker.md
[features]: #features
[sides]: ../concepts/sides.md#writing-one-sided-mods
[dist]: ../concepts/sides.md#different-kinds-of-sides
[events]: ../concepts/events.md
[registration]: ../concepts/registries.md#deferredregister


# Structuring Your Mod
====================

Structured mods are beneficial for maintenance, making contributions, and providing a clearer understanding of the underlying codebase. Some of the recommendations from Java, Minecraft, and Forge are listed below.

!!! note
    You do not have to follow the advice below; you can structure your mod any way you see fit. However, it is still highly recommended to do so.

Packaging
---------

When structuring your mod, pick a unique, top-level package structure. Many programmers will use the same name for different classes, interfaces, etc. Java allows classes to have the same name as long as they are in different packages. As such, if two classes have the same package with the same name, only one would be loaded, most likely causing the game to crash.

```
a.jar
  - com.example.ExampleClass
b.jar
  - com.example.ExampleClass // This class will not normally be loaded
```

This is even more relevant when it comes to loading modules. If there are class files in two packages under the same name in separate modules, this will cause the mod loader **to crash on startup** since mod modules are exported to the game and other mods.

```
module A
  - package X
    - class I
    - class J
module B
  - package X // This package will cause the mod loader to crash, as there already is a module with package X being exported
    - class R
    - class S
    - class T
```

As such, your top level package should be something that you own: a domain, email address, a subdomain of where your website, etc. It can even be your name or username as long as you can guarantee that it will be uniquely identifiable within the expected target.

Type      | Value             | Top-Level Package
:---:     | :---:             | :---
Domain    | example.com       | `com.example`
Subdomain | example.github.io | `io.github.example`
Email     | example@gmail.com | `com.gmail.example`

The next level package should then be your mod's id (e.g. `com.example.examplemod` where `examplemod` is the mod id). This will guarantee that, unless you have two mods with the same id (which should never be the case), your packages should not have any issues loading.

You can find some additional naming conventions on [Oracle's tutorial page][naming].

### Sub-package Organization

In addition to the top-level package, it is highly recommend to break your mod's classes between subpackages. There are two major methods on how to do so:

* **Group By Function**: Make subpackages for classes with a common purpose. For example, blocks can be under `block` or `blocks`, entities under `entity` or `entities`, etc. Mojang uses this structure with the singular version of the word.
* **Group By Logic**: Make subpackages for classes with a common logic. For example, if you were creating a new type of crafting table, you would put its block, menu, item, and more under `feature.crafting_table`.

#### Client, Server, and Data Packages

In general, code only for a given side or runtime should be isolated from the other classes in a separate subpackage. For example, code related to [data generation][datagen] should go in a `data` package while code only on the dedicated server should go in a `server` package.

However, it is highly recommended that [client-only code][sides] should be isolated in a `client` subpackage. This is because dedicated servers have no access to any of the client-only packages in Minecraft. As such, having a dedicated package would provide a decent sanity check to verify you are not reaching across sides within your mod.

Class Naming Schemes
--------------------

A common class naming scheme makes it easier to decipher the purpose of the class or to easily locate specific classes.

Classes are commonly suffixed with its type, for example:

* An `Item` called `PowerRing` -> `PowerRingItem`.
* A `Block` called `NotDirt` -> `NotDirtBlock`.
* A menu for an `Oven` -> `OvenMenu`.

!!! note
    Mojang typically follows a similar structure for all classes except entities. Those are represented by just their names (e.g. `Pig`, `Zombie`, etc.).

Choose One Method from Many
---------------------------

There are many methods for performing a certain task: registering an object, listening for events, etc. It's generally recommended to be consistent by using a single method to accomplish a given task. While this does improve code formatting, it also avoid any weird interactions or redundancies that may occur (e.g. your event listener executing twice).

[naming]: https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html
[datagen]: ../datagen/index.md
[sides]: ../concepts/sides.md


# Versioning
==========

In general projects, [semantic versioning][semver] is often used (which has the format `MAJOR.MINOR.PATCH`). However, in the case of modding it may be more beneficial to use the format `MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH` to be able to differentiate between world-breaking and API-breaking changes of a mod.

!!! important
    Forge uses [Maven version ranges][cmpver] to compare version strings, which is not fully compatible with the Semantic Versioning 2.0.0 spec, such as the 'prerelease' tag.

Examples
--------

Here is a list of examples that can increment the various variables.

* `MCVERSION`
  * Always matches the Minecraft version the mod is for.
* `MAJORMOD`
  * Removing items, blocks, block entities, etc.
  * Changing or removing previously existing mechanics.
  * Updating to a new Minecraft version.
* `MAJORAPI`
  * Changing the order or variables of enums.
  * Changing return types of methods.
  * Removing public methods altogether.
* `MINOR`
  * Adding items, blocks, block entities, etc.
  * Adding new mechanics.
  * Deprecating public methods. (This is not a `MAJORAPI` increment since it doesn't break an API.)
* `PATCH`
  * Bugfixes.

When incrementing any variable, all lesser variables should reset to `0`. For instance, if `MINOR` would increment, `PATCH` would become `0`. If `MAJORMOD` would increment, all other variables would become `0`.

### Work In Progress

If you are in the initial development stage of your mod (before any official releases), the `MAJORMOD` and `MAJORAPI` should always be `0`. Only `MINOR` and `PATCH` should be updated every time you build your mod. Once you build an official release (most of the time with a stable API), you should increment `MAJORMOD` to version `1.0.0.0`. For any further development stages, refer to the [Prereleases][pre] and [Release candidates][rc] section of this document.

### Multiple Minecraft Versions

If the mod upgrades to a new version of Minecraft, and the old version will only receive bug fixes, the `PATCH` variable should be updated based on the version before the upgrade. If the mod is still in active development in both the old and the new version of Minecraft, it is advised to append the version to **both** build numbers. For example, if the mod is upgraded to version `3.0.0.0` due to a Minecraft version change, the old mod should also be updated to `3.0.0.0`. The old version will become, for example, version `1.7.10-3.0.0.0`, while the new version will become `1.8-3.0.0.0`. If there are no changes at all when building for a newer Minecraft version, all variables except for the Minecraft version should stay the same.

### Final Release

When dropping support for a Minecraft version, the last build for that version should get the `-final` suffix. This denotes that the mod will no longer be supported for the denoted `MCVERSION` and that players should upgrade to a newer version of the mod to continue receiving updates and bug fixes.

### Pre-releases

It is also possible to prerelease work-in-progress features, which means new features are released that are not quite done yet. These can be seen as a sort of "beta". These versions should be appended with `-betaX`, where `X` is the number of the prerelease. (This guide does not use `-pre` since, at the time of writing, it is not a valid alias for `-beta`.) Note that already released versions and versions before the initial release can not go into prerelease; variables (mostly `MINOR`, but `MAJORAPI` and `MAJORMOD` can also prerelease) should be updated accordingly before adding the `-beta` suffix. Versions before the initial release are simply work-in-progress builds.

### Release Candidates

Release candidates act as prereleases before an actual version change. These versions should be appended with `-rcX`, where `X` is the number of the release candidate which should, in theory, only be increased for bugfixes. Already released versions can not receive release candidates; variables (mostly `MINOR`, but `MAJORAPI` and `MAJORMOD` can also prerelease)  should be updated accordingly before adding the `-rc` suffix. When releasing a release candidate as stable build, it can either be exactly the same as the last release candidate or have a few more bug fixes.

[semver]: https://semver.org/
[cmpver]: https://maven.apache.org/ref/3.5.2/maven-artifact/apidocs/org/apache/maven/artifact/versioning/ComparableVersion.html
[pre]: #pre-releases
[rc]: #release-candidates


# Registries
==========

Registration is the process of taking the objects of a mod (such as items, blocks, sounds, etc.) and making them known to the game. Registering things is important, as without registration the game will simply not know about these objects, which will cause unexplainable behaviors and crashes. 

Most things that require registration in the game are handled by the Forge registries. A registry is an object similar to a map that assigns values to keys. Forge uses registries with [`ResourceLocation`][ResourceLocation] keys to register objects. This allows the `ResourceLocation` to act as the "registry name" for objects.

Every type of registrable object has its own registry. To see all registries wrapped by Forge, see the `ForgeRegistries` class. All registry names within a registry must be unique. However, names in different registries will not collide. For example, there's a `Block` registry, and an `Item` registry. A `Block` and an `Item` may be registered with the same name `example:thing` without colliding; however, if two different `Block`s or `Item`s were registered with the same exact name, the second object will override the first.

Methods for Registering
------------------

There are two proper ways to register objects: the `DeferredRegister` class, and the `RegisterEvent` lifecycle event.

### DeferredRegister

`DeferredRegister` is the recommended way to register objects. It allows the use and convenience of static initializers while avoiding the issues associated with it. It simply maintains a list of suppliers for entries and registers the objects from those suppliers during `RegisterEvent`.

An example of a mod registering a custom block:

```java
private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

public static final RegistryObject<Block> ROCK_BLOCK = BLOCKS.register("rock", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));

public ExampleMod() {
  BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
}
```

### `RegisterEvent`

`RegisterEvent` is the second way to register objects. This [event] is fired for each registry after the mod constructors and before the loading of configs. Objects are registered using `#register` by passing in the registry key, the name of the registry object, and the object itself. There is an additional `#register` overload which takes in a consumed helper to register an object with a given name. It is recommended to use this method to avoid unnecessary object creation.

Here is an example: (the event handler is registered on the *mod event bus*)

```java
@SubscribeEvent
public void register(RegisterEvent event) {
  event.register(ForgeRegistries.Keys.BLOCKS,
    helper -> {
      helper.register(new ResourceLocation(MODID, "example_block_1"), new Block(...));
      helper.register(new ResourceLocation(MODID, "example_block_2"), new Block(...));
      helper.register(new ResourceLocation(MODID, "example_block_3"), new Block(...));
      // ...
    }
  );
}
```

### Registries that aren't Forge Registries

Not all registries are wrapped by Forge. These can be static registries, like `LootItemConditionType`, which are safe to use. There are also dynamic registries, like `ConfiguredFeature` and some other worldgen registries, which are typically represented in JSON. `DeferredRegister#create` has an overload which allows modders to specify the registry key of which vanilla registry to create a `RegistryObject` for. The registry method and attaching to the mod event bus is the same as other `DeferredRegister`s.

!!! important
    Dynamic registry objects can **only** be registered through data files (e.g. JSON). They **cannot** be registered in-code.

```java
private static final DeferredRegister<LootItemConditionType> REGISTER = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, "examplemod");

public static final RegistryObject<LootItemConditionType> EXAMPLE_LOOT_ITEM_CONDITION_TYPE = REGISTER.register("example_loot_item_condition_type", () -> new LootItemConditionType(...));
```

!!! note
    Some classes cannot by themselves be registered. Instead, `*Type` classes are registered, and used in the formers' constructors. For example, [`BlockEntity`][blockentity] has `BlockEntityType`, and `Entity` has `EntityType`. These `*Type` classes are factories that simply create the containing type on demand. 
    
    These factories are created through the use of their `*Type$Builder` classes. An example: (`REGISTER` refers to a `DeferredRegister<BlockEntityType>`)
    ```java
    public static final RegistryObject<BlockEntityType<ExampleBlockEntity>> EXAMPLE_BLOCK_ENTITY = REGISTER.register(
      "example_block_entity", () -> BlockEntityType.Builder.of(ExampleBlockEntity::new, EXAMPLE_BLOCK.get()).build(null)
    );
    ```

Referencing Registered Objects
------------------------------

Registered objects should not be stored in fields when they are created and registered. They are to be always newly created and registered whenever `RegisterEvent` is fired for that registry. This is to allow dynamic loading and unloading of mods in a future version of Forge.

Registered objects must always be referenced through a `RegistryObject` or a field with `@ObjectHolder`.

### Using RegistryObjects

`RegistryObject`s can be used to retrieve references to registered objects once they are available. These are used by `DeferredRegister` to return a reference to the registered objects. Their references are updated after `RegisterEvent` is called for their registry, along with the `@ObjectHolder` annotations.

To get a `RegistryObject`, call `RegistryObject#create` with a `ResourceLocation` and the `IForgeRegistry` of the registrable object. Custom registries can also be used by supplying the registry name instead. Store the `RegistryObject` in a `public static final` field, and call `#get` whenever you need the registered object.

An example of using `RegistryObject`:

```java
public static final RegistryObject<Item> BOW = RegistryObject.create(new ResourceLocation("minecraft:bow"), ForgeRegistries.ITEMS);

// assume that 'neomagicae:mana_type' is a valid registry, and 'neomagicae:coffeinum' is a valid object within that registry
public static final RegistryObject<ManaType> COFFEINUM = RegistryObject.create(new ResourceLocation("neomagicae", "coffeinum"), new ResourceLocation("neomagicae", "mana_type"), "neomagicae"); 
```

### Using @ObjectHolder

Registered objects from registries can be injected into the `public static` fields by annotating classes or fields with `@ObjectHolder` and supplying enough information to construct a `ResourceLocation` to identify a specific object in a specific registry.

The rules for `@ObjectHolder` are as follows:

* If the class is annotated with `@ObjectHolder`, its value will be the default namespace for all fields within if not explicitly defined
* If the class is annotated with `@Mod`, the modid will be the default namespace for all annotated fields within if not explicitly defined
* A field is considered for injection if:
  * it has at least the modifiers `public static`;
  * the **field** is annotated with `@ObjectHolder`, and:
    * the name value is explicitly defined; and
    * the registry name value is explicitly defined
  * _A compile-time exception is thrown if a field does not have a corresponding registry or name._
* _An exception is thrown if the resulting `ResourceLocation` is incomplete or invalid (non-valid characters in path)_
* If no other errors or exceptions occur, the field will be injected
* If all of the above rules do not apply, no action will be taken (and a message may be logged)

`@ObjectHolder`-annotated fields are injected with their values after `RegisterEvent` is fired for their registry, along with the `RegistryObject`s.

!!! note
    If the object does not exist in the registry when it is to be injected, a debug message will be logged and no value will be injected.

As these rules are rather complicated, here are some examples:

```java
class Holder {
  @ObjectHolder(registryName = "minecraft:enchantment", value = "minecraft:flame")
  public static final Enchantment flame = null;     // Annotation present. [public static] is required. [final] is optional.
                                                    // Registry name is explicitly defined: "minecraft:enchantment"
                                                    // Resource location is explicitly defined: "minecraft:flame"
                                                    // To inject: "minecraft:flame" from the [Enchantment] registry

  public static final Biome ice_flat = null;        // No annotation on the field.
                                                    // Therefore, the field is ignored.

  @ObjectHolder("minecraft:creeper")
  public static Entity creeper = null;              // Annotation present. [public static] is required.
                                                    // The registry has not been specified on the field.
                                                    // Therefore, THIS WILL PRODUCE A COMPILE-TIME EXCEPTION.

  @ObjectHolder(registryName = "potion")
  public static final Potion levitation = null;     // Annotation present. [public static] is required. [final] is optional.
                                                    // Registry name is explicitly defined: "minecraft:potion"
                                                    // Resource location is not specified on the field
                                                    // Therefore, THIS WILL PRODUCE A COMPILE-TIME EXCEPTION.
}
```

Creating Custom Forge Registries
--------------------------------

Custom registries can usually just be a simple map of key to value. This is a common style; however, it forces a hard dependency on the registry being present. It also requires that any data that needs to be synced between sides must be done manually. Custom Forge Registries provide a simple alternative for creating soft dependents along with better management and automatic syncing between sides (unless told otherwise). Since the objects also use a Forge registry, registration becomes standardized in the same way.

Custom Forge Registries are created with the help of a `RegistryBuilder`, through either `NewRegistryEvent` or the `DeferredRegister`. The `RegistryBuilder` class takes various parameters (such as the registry's name, id range, and various callbacks for different events happening on the registry). New registries are registered to the `RegistryManager` after `NewRegistryEvent` finishes firing.

Any newly created registry should use its associated [registration method][registration] to register the associated objects.

### Using NewRegistryEvent

When using `NewRegistryEvent`, calling `#create` with a `RegistryBuilder` will return a supplier-wrapped registry. The supplied registry can be accessed after `NewRegistryEvent` has finished posting to the mod event bus. Getting the custom registry from the supplier before `NewRegistryEvent` finishes firing will result in a `null` value.

#### New Datapack Registries

New datapack registries can be added using the `DataPackRegistryEvent$NewRegistry` event on the mod event bus. The registry is created via `#dataPackRegistry` by passing in the `ResourceKey` representing the registry name and the `Codec` used to encode and decode the data from JSON. An optional `Codec` can be provided to sync the datapack registry to the client.

!!! important
    Datapack Registries cannot be created with `DeferredRegister`. They can only be created through the event.

### With DeferredRegister

The `DeferredRegister` method is once again another wrapper around the above event. Once a `DeferredRegister` is created in a constant field using the `#create` overload which takes in the registry name and the mod id, the registry can be constructed via `DeferredRegister#makeRegistry`. This takes in  a supplied `RegistryBuilder` containing any additional configurations. The method already populates `#setName` by default. Since this method can be returned at any time, a supplied version of an `IForgeRegistry` is returned instead. Getting the custom registry from the supplier before `NewRegistryEvent` is fired will result in a `null` value.

!!! important
    `DeferredRegister#makeRegistry` must be called before the `DeferredRegister` is added to the mod event bus via `#register`. `#makeRegistry` also uses the `#register` method to create the registry during `NewRegistryEvent`.

Handling Missing Entries
------------------------

There are cases where certain registry objects will cease to exist whenever a mod is updated or, more likely, removed. It is possible to specify actions to handle the missing mapping through the third of the registry events: `MissingMappingsEvent`. Within this event, a list of missing mappings can be obtained either by `#getMappings` given a registry key and mod id or all mappings via `#getAllMappings` given a registry key.

!!! important
    `MissingMappingsEvent` is fired on the **Forge** event bus.

For each `Mapping`, one of four mapping types can be selected to handle the missing entry:

| Action | Description |
| :---:  |     :---    |
| IGNORE | Ignores the missing entry and abandons the mapping. |
|  WARN  | Generates a warning in the log. |
|  FAIL  | Prevents the world from loading. |
| REMAP  | Remaps the entry to an already registered, non-null object. |

If no action is specified, then the default action will occur by notifying the user about the missing entry and whether they still would like to load the world. All actions besides remapping will prevent any other registry object from taking the place of the existing id in case the associated entry ever gets added back into the game.

[ResourceLocation]: ./resources.md#resourcelocation
[registration]: #methods-for-registering
[event]: ./events.md
[blockentity]: ../blockentities/index.md


# Sides in Minecraft
===================

A very important concept to understand when modding Minecraft are the two sides: *client* and *server*. There are many, many common misconceptions and mistakes regarding siding, which can lead to bugs that might not crash the game, but can rather have unintended and often unpredictable effects.

Different Kinds of Sides
------------------------

When we say "client" or "server", it usually follows with a fairly intuitive understanding of what part of the game we are talking about. After all, a client is what the user interacts with, and a server is where the user connects for a multiplayer game. Easy, right?

As it turns out, there can be some ambiguity even with two such terms. Here we disambiguate the four possible meanings of "client" and "server":

* Physical client - The *physical client* is the entire program that runs whenever you launch Minecraft from the launcher. All threads, processes, and services that run during the game's graphical, interactable lifetime are part of the physical client.
* Physical server - Often known as the dedicated server, the *physical server* is the entire program that runs whenever you launch any sort of `minecraft_server.jar` that does not bring up a playable GUI.
* Logical server - The *logical server* is what runs game logic: mob spawning, weather, updating inventories, health, AI, and all other game mechanics. The logical server is present within a physical server, but it also can run inside a physical client together with a logical client, as a single player world. The logical server always runs in a thread named the `Server Thread`.
* Logical client - The *logical client* is what accepts input from the player and relays it to the logical server. In addition, it also receives information from the logical server and makes it available graphically to the player. The logical client runs in the `Render Thread`, though often several other threads are spawned to handle things like audio and chunk render batching.

In the MinecraftForge codebase, the physical side is represented by an enum called `Dist`, while the logical side is represented by an enum called `LogicalSide`.

Performing Side-Specific Operations
-----------------------------------

### `Level#isClientSide`

This boolean check will be your most used way to check sides. Querying this field on a `Level` object establishes the  **logical** side the level belongs to. That is, if this field is `true`, the level is currently running on the logical client. If the field is `false`, the level is running on the logical server. It follows that the physical server will always contain `false` in this field, but we cannot assume that `false` implies a physical server, since this field can also be `false` for the logical server inside a physical client (in other words, a single player world).

Use this check whenever you need to determine if game logic and other mechanics should be run. For example, if you want to damage the player every time they click your block, or have your machine process dirt into diamonds, you should only do so after ensuring `#isClientSide` is `false`. Applying game logic to the logical client can cause desynchronization (ghost entities, desynchronized stats, etc.) in the best case, and crashes in the worst case.

This check should be used as your go-to default. Aside from `DistExecutor`, rarely will you need the other ways of determining side and adjusting behavior.

### `DistExecutor`

Considering the use of a single "universal" jar for client and server mods, and the separation of the physical sides into two jars, an important question comes to mind: How do we use code that is only present on one physical side? All code in `net.minecraft.client` is only present on the physical client. If any class you write references those names in any way, they will crash the game when that respective class is loaded in an environment where those names do not exist. A very common mistake in beginners is to call `Minecraft.getInstance().<doStuff>()` in block or block entity classes, which will crash any physical server as soon as the class is loaded.

How do we resolve this? Luckily, FML has `DistExecutor`, which provides various methods to run different methods on different physical sides, or a single method only on one side.

!!! note
    It is important to understand that FML checks based on the **physical** side. A single player world (logical server + logical client within a physical client) will always use `Dist.CLIENT`!

`DistExecutor` works by taking in a supplied supplier executing a method, effectively preventing classloading by taking advantage of the [`invokedynamic` JVM instruction][invokedynamic]. The executed method should be static and within a different class. Additionally, if no parameters are present for the static method, a method reference should be used instead of a supplier executing a method.

There are two main methods within `DistExecutor`: `#runWhenOn` and `#callWhenOn`. The methods take in the physical side the executing method should run on and the supplied executing method which either runs or returns a result respectively.

These two methods are subdivided further into `#safe*` and `#unsafe*` variants. Safe and unsafe variants are misnomers for their purposes. The main difference is that when in a development environment, the `#safe*` methods will validate that the supplied executing method is a lambda returning a method reference to another class with an error being thrown otherwise. Within the production environment, `#safe*` and `#unsafe*` are functionally the same.

```java
// In a client class: ExampleClass
public static void unsafeRunMethodExample(Object param1, Object param2) {
  // ...
}

public static Object safeCallMethodExample() {
  // ...
}

// In some common class
DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ExampleClass.unsafeRunMethodExample(var1, var2));

DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ExampleClass::safeCallMethodExample);

```

!!! warning
    Due to a change in how `invokedynamic` works in Java 9+, all `#safe*` variants of the `DistExecutor` methods throw the original exception wrapped within a `BootstrapMethodError` in the development environment. `#unsafe*` variants or a check to [`FMLEnvironment#dist`][dist] should be used instead.

### Thread Groups

If `Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER` is true, it is likely the current thread is on the logical server. Otherwise, it is likely on the logical client. This is useful to retrieve the **logical** side when you do not have access to a `Level` object to check `isClientSide`. It *guesses* which logical side you are on by looking at the group of the currently running thread. Because it is a guess, this method should only be used when other options have been exhausted. In nearly every case, you should prefer checking `Level#isClientSide`.

### `FMLEnvironment#dist` and `@OnlyIn`

`FMLEnvironment#dist` holds the **physical** side your code is running on. Since it is determined at startup, it does not rely on guessing to return its result. The number of use cases for this is limited, however.

Annotating a method or field with the `@OnlyIn(Dist)` annotation indicates to the loader that the respective member should be completely stripped out of the definition not on the specified **physical** side. Usually, these are only seen when browsing through the decompiled Minecraft code, indicating methods that the Mojang obfuscator stripped out. There is **NO** reason for using this annotation directly. Use `DistExecutor` or a check on `FMLEnvironment#dist` instead.

Common Mistakes
---------------

### Reaching Across Logical Sides

Whenever you want to send information from one logical side to another, you must **always** use network packets. It is incredibly tempting, when in a single player scenario, to directly transfer data from the logical server to the logical client.

This is actually very commonly inadvertently done through static fields. Since the logical client and logical server share the same JVM in a single player scenario, both threads writing to and reading from static fields will cause all sorts of race conditions and the classical issues associated with threading.

This mistake can also be made explicitly by accessing physical client-only classes such as `Minecraft` from common code that runs or can run on the logical server. This mistake is easy to miss for beginners who debug in a physical client. The code will work there, but it will immediately crash on a physical server.


Writing One-Sided Mods
----------------------

In recent versions, Minecraft Forge has removed a "sidedness" attribute from the mods.toml. This means that your mods are expected to work whether they are loaded on the physical client or the physical server. So for one-sided mods, you would typically register your event handlers inside a `DistExecutor#safeRunWhenOn` or `DistExecutor#unsafeRunWhenOn` instead of directly calling the relevant registration methods in your mod constructor. Basically, if your mod is loaded on the wrong side, it should simply do nothing, listen to no events, and so on. A one-sided mod by nature should not register blocks, items, ... since they would need to be available on the other side, too.

Additionally, if your mod is one-sided, it typically does not forbid the user from joining a server that is lacking that mod. Therefore, you should set the `displayTest` property in your [mods.toml][structuring] to whatever value is necessary.

```toml
[[mods]]
  # ...

  # MATCH_VERSION means that your mod will cause a red X if the versions on client and server differ. This is the default behaviour and should be what you choose if you have server and client elements to your mod.
  # IGNORE_SERVER_VERSION means that your mod will not cause a red X if it's present on the server but not on the client. This is what you should use if you're a server only mod.
  # IGNORE_ALL_VERSION means that your mod will not cause a red X if it's present on the client or the server. This is a special case and should only be used if your mod has no server component.
  # NONE means that no display test is set on your mod. You need to do this yourself, see IExtensionPoint.DisplayTest for more information. You can define any scheme you wish with this value.
  # IMPORTANT NOTE: this is NOT an instruction as to which environments (CLIENT or DEDICATED SERVER) your mod loads on. Your mod should load (and maybe do nothing!) wherever it finds itself.
  displayTest="IGNORE_ALL_VERSION" # MATCH_VERSION is the default if nothing is specified (#optional)
```

If a custom display test is to be used, then the `displayTest` option should be set to `NONE`, and an `IExtensionPoint$DisplayTest` extension should be registered:

```java
//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
```

This tells the client that it should ignore the server version being absent, and the server that it should not tell the client this mod should be present. So this snippet works both for client- and server-only-sided mods.


[invokedynamic]: https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-6.html#jvms-6.5.invokedynamic
[dist]: #fmlenvironmentdist-and-onlyin
[structuring]: ../gettingstarted/modfiles.md#modstoml


# Events
======

Forge uses an event bus that allows mods to intercept events from various Vanilla and mod behaviors.

Example: An event can be used to perform an action when a Vanilla stick is right clicked.

The main event bus used for most events is located at `MinecraftForge#EVENT_BUS`. There is another event bus for mod specific events located at `FMLJavaModLoadingContext#getModEventBus` that you should only use in specific cases. More information about this bus can be found below.

Every event is fired on one of these buses: most events are fired on the main forge event bus, but some are fired on the mod specific event buses.

An event handler is some method that has been registered to an event bus.

Creating an Event Handler
-------------------------

Event handlers methods have a single parameter and do not return a result. The method could be static or instance depending on implementation.

Event handlers can be directly registered using `IEventBus#addListener` for or `IEventBus#addGenericListener` for generic events (as denoted by subclassing `GenericEvent<T>`). Either listener adder takes in a consumer representing the method reference. Generic event handlers need to specify the class of the generic as well. Event handlers must be registered within the constructor of the main mod class.

```java
// In the main mod class ExampleMod

// This event is on the mod bus
private void modEventHandler(RegisterEvent event) {
	// Do things here
}

// This event is on the forge bus
private static void forgeEventHandler(AttachCapabilitiesEvent<Entity> event) {
	// ...
}

// In the mod constructor
modEventBus.addListener(this::modEventHandler);
forgeEventBus.addGenericListener(Entity.class, ExampleMod::forgeEventHandler);
```

### Instance Annotated Event Handlers

This event handler listens for the `EntityItemPickupEvent`, which is, as the name states, posted to the event bus whenever an `Entity` picks up an item.

```java
public class MyForgeEventHandler {
	@SubscribeEvent
	public void pickupItem(EntityItemPickupEvent event) {
		System.out.println("Item picked up!");
	}
}
```

To register this event handler, use `MinecraftForge.EVENT_BUS.register(...)` and pass it an instance of the class the event handler is within. If you want to register this handler to the mod specific event bus, you should use `FMLJavaModLoadingContext.get().getModEventBus().register(...)` instead.

### Static Annotated Event Handlers

An event handler may also be static. The handling method is still annotated with `@SubscribeEvent`. The only difference from an instance handler is that it is also marked `static`. In order to register a static event handler, an instance of the class won't do. The `Class` itself has to be passed in. An example:

```java
public class MyStaticForgeEventHandler {
	@SubscribeEvent
	public static void arrowNocked(ArrowNockEvent event) {
		System.out.println("Arrow nocked!");
	}
}
```

which must be registered like this: `MinecraftForge.EVENT_BUS.register(MyStaticForgeEventHandler.class)`.

### Automatically Registering Static Event Handlers

A class may be annotated with the `@Mod$EventBusSubscriber` annotation. Such a class is automatically registered to `MinecraftForge#EVENT_BUS` when the `@Mod` class itself is constructed. This is essentially equivalent to adding `MinecraftForge.EVENT_BUS.register(AnnotatedClass.class);` at the end of the `@Mod` class's constructor.

You can pass the bus you want to listen to the `@Mod$EventBusSubscriber` annotation. It is recommended you also specify the mod id, since the annotation process may not be able to figure it out, and the bus you are registering to, since it serves as a reminder to make sure you are on the correct one. You can also specify the `Dist`s or physical sides to load this event subscriber on. This can be used to not load client specific event subscribers on the dedicated server.

An example for a static event listener listening to `RenderLevelStageEvent` which will only be called on the client:

```java
@Mod.EventBusSubscriber(modid = "mymod", bus = Bus.FORGE, value = Dist.CLIENT)
public class MyStaticClientOnlyEventHandler {
	@SubscribeEvent
	public static void drawLast(RenderLevelStageEvent event) {
		System.out.println("Drawing!");
	}
}
```

!!! note
    This does not register an instance of the class; it registers the class itself (i.e. the event handling methods must be static).

Canceling
---------

If an event can be canceled, it will be marked with the `@Cancelable` annotation, and the method `Event#isCancelable()` will return `true`. The cancel state of a cancelable event may be modified by calling `Event#setCanceled(boolean canceled)`, wherein passing the boolean value `true` is interpreted as canceling the event, and passing the boolean value `false` is interpreted as "un-canceling" the event. However, if the event cannot be canceled (as defined by `Event#isCancelable()`), an `UnsupportedOperationException` will be thrown regardless of the passed boolean value, since the cancel state of a non-cancelable event event is considered immutable.

!!! important
    Not all events can be canceled! Attempting to cancel an event that is not cancelable will result in an unchecked `UnsupportedOperationException` being thrown, which is expected to result in the game crashing! Always check that an event can be canceled using `Event#isCancelable()` before attempting to cancel it!

Results
-------

Some events have an `Event$Result`. A result can be one of three things: `DENY` which stops the event, `DEFAULT` which uses the Vanilla behavior, and `ALLOW` which forces the action to take place, regardless if it would have originally. The result of an event can be set by calling `#setResult` with an `Event$Result` on the event. Not all events have results; an event with a result will be annotated with `@HasResult`.

!!! important
    Different events may use results in different ways, refer to the event's JavaDoc before using the result.

Priority
--------

Event handler methods (marked with `@SubscribeEvent`) have a priority. You can set the priority of an event handler method by setting the `priority` value of the annotation. The priority can be any value of the `EventPriority` enum (`HIGHEST`, `HIGH`, `NORMAL`, `LOW`, and `LOWEST`). Event handlers with priority `HIGHEST` are executed first and from there in descending order until `LOWEST` events which are executed last.

Sub Events
----------

Many events have different variations of themselves. These can be different but all based around one common factor (e.g. `PlayerEvent`) or can be an event that has multiple phases (e.g. `PotionBrewEvent`). Take note that if you listen to the parent event class, you will receive calls to your method for *all* subclasses.

Mod Event Bus
-------------

The mod event bus is primarily used for listening to lifecycle events in which mods should initialize. Each event on the mod bus is required to implement `IModBusEvent`. Many of these events are also ran in parallel so mods can be initialized at the same time. This does mean you can't directly execute code from other mods in these events. Use the `InterModComms` system for that.

These are the four most commonly used lifecycle events that are called during mod initialization on the mod event bus:

* `FMLCommonSetupEvent`
* `FMLClientSetupEvent` & `FMLDedicatedServerSetupEvent`
* `InterModEnqueueEvent`
* `InterModProcessEvent`

!!! note
    The `FMLClientSetupEvent` and `FMLDedicatedServerSetupEvent` are only called on their respective distribution.

These four lifecycle events are all ran in parallel since they all are a subclass of `ParallelDispatchEvent`. If you want to run run code on the main thread during any `ParallelDispatchEvent`, you can use the `#enqueueWork` to do so.

Next to the lifecycle events, there are a few miscellaneous events that are fired on the mod event bus where you can register, set up, or initialize various things. Most of these events are not ran in parallel in contrast to the lifecycle events. A few examples:

* `RegisterColorHandlersEvent`
* `ModelEvent$BakingCompleted`
* `TextureStitchEvent`
* `RegisterEvent`

A good rule of thumb: events are fired on the mod event bus when they should be handled during initialization of a mod.


# Mod Lifecycle
==============

During the mod loading process, the various lifecycle events are fired on the mod-specific event bus. Many actions are performed during these events, such as [registering objects][registering], preparing for [data generation][datagen], or [communicating with other mods][imc].

Event listeners should be registered either using `@EventBusSubscriber(bus = Bus.MOD)` or in the mod constructor:

```Java
@Mod.EventBusSubscriber(modid = "mymod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class MyModEventSubscriber {
  @SubscribeEvent
  static void onCommonSetup(FMLCommonSetupEvent event) { ... }
}

@Mod("mymod")
public class MyMod {
  public MyMod() {
    FMLModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
  } 

  private void onCommonSetup(FMLCommonSetupEvent event) { ... }
}
```

!!! warning
    Most of the lifecycle events are fired in parallel: all mods will concurrently receive the same event.
    
    Mods *must* take care to be thread-safe, like when calling other mods' APIs or accessing vanilla systems. Defer code for later execution via `ParallelDispatchEvent#enqueueWork`.

Registry Events
---------------

The registry events are fired after the mod instance construction. There are three: `NewRegistryEvent`, `DataPackRegistryEvent$NewRegistry` and `RegisterEvent`. These events are fired synchronously during mod loading.

`NewRegistryEvent` allows modders to register their own custom registries, using the `RegistryBuilder` class.

`DataPackRegistryEvent$NewRegistry` allows modders to register custom datapack registries by providing a `Codec` to encode and decode the object from JSON.

`RegisterEvent` is for [registering objects][registering] into the registries. The event is fired for each registry. 

Data Generation
---------------

If the game is setup to run [data generators][datagen], then the `GatherDataEvent` will be the last event to fire. This event is for registering mods' data providers to their associated data generator. This event is also fired synchronously.

Common Setup
------------

`FMLCommonSetupEvent` is for actions that are common to both physical client and server, such as registering [capabilities][capabilities].

Sided Setup
-----------

The sided-setup events are fired on their respective [physical sides][sides]: `FMLClientSetupEvent` on the physical client, and `FMLDedicatedServerSetupEvent` for the dedicated server. This is where physical side-specific initialization should occur, such as registering client-side key bindings.

InterModComms
-------------

This is where messages can be sent to mods for cross-mod compatibility. There are two events: `InterModEnqueueEvent` and `InterModProcessEvent`.

`InterModComms` is the class responsible for holding messages for mods. The methods are safe to call during the lifecycle events, as it is backed by a `ConcurrentMap`.

During the `InterModEnqueueEvent`, use `InterModComms#sendTo` to send messages to different mods. These methods take in the mod id that will be sent the message, the key associated with the message data, and a supplier holding the message data. Additionally, the sender of the message can also be specified, but by default it will be the mod id of the caller.

Then during the `InterModProcessEvent`, use `InterModComms#getMessages` to get a stream of all received messages. The mod id supplied will almost always be the mod id of the mod the method is called on. Additionally, a predicate can be specified to filter out the message keys. This will return a stream of `IMCMessage`s which hold the sender of the data, the receiver of the data, the data key, and the supplied data itself.

!!! note
    There are two other lifecycle events: `FMLConstructModEvent`, fired directly after mod instance construction but before the `RegisterEvent`, and `FMLLoadCompleteEvent`, fired after the `InterModComms` events, for when the mod loading process is complete.

[registering]: ./registries.md#methods-for-registering
[capabilities]: ../datastorage/capabilities.md
[datagen]: ../datagen/index.md
[imc]: ./lifecycle.md#intermodcomms
[sides]: ./sides.mdS


# Resources
=========

A resource is extra data used by the game, and is stored in a data file, instead of being in the code. 
Minecraft has two primary resource systems active: one on the logical client used for visuals such as models, textures, and localization called `assets`, and one on the logical server used for gameplay such as recipes and loot tables called `data`.
[Resource packs][respack] control the former, while [Datapacks][datapack] control the latter.

In the default mod development kit, assets and data directories are located under the `src/main/resources` directory of the project. 

When multiple resource packs or data packs are enabled, they are merged. Generally, files from packs at the top of the stack override those below; however, for certain files, such as localization files and tags, data is actually merged contentwise. Mods define resource and data packs in their `resources` directories, but they are seen as subsets of the "Mod Resources" pack. Mod resource packs cannot be disabled, but they can be overridden by other resource packs. Mod datapacks can be disabled with the vanilla `/datapack` command.

All resources should have snake case paths and filenames (lowercase, using "_" for word boundaries), which is enforced in 1.11 and above.

`ResourceLocation`
------------------

Minecraft identifies resources using `ResourceLocation`s. A `ResourceLocation` contains two parts: a namespace and a path. It generally points to the resource at `assets/<namespace>/<ctx>/<path>`, where `ctx` is a context-specific path fragment that depends on how the `ResourceLocation` is being used. When a `ResourceLocation` is written/read as from a string, it is seen as `<namespace>:<path>`. If the namespace and the colon are left out, then when the string is read into an `ResourceLocation` the namespace will always default to `"minecraft"`. A mod should put its resources into a namespace with the same name as its mod id (e.g. a mod with the id `examplemod` should place its resources in `assets/examplemod` and `data/examplemod` respectively, and `ResourceLocation`s pointing to those files would look like `examplemod:<path>`.). This is not a requirement, and in some cases it can be desirable to use a different (or even more than one) namespace. `ResourceLocation`s are used outside the resource system, too, as they happen to be a great way to uniquely identify objects (e.g. [registries][]).

[respack]: ../resources/client/index.md
[datapack]: ../resources/server/index.md
[registries]: ./registries.md

# Internationalization and Localization
=====================================

Internationalization, i18n for short, is a way of designing code so that it requires no changes to be adapted for various languages. Localization is the process of adapting displayed text to the user's language.

I18n is implemented using _translation keys_. A translation key is a string that identifies a piece of displayable text in no specific language. For example, `block.minecraft.dirt` is the translation key referring to the name of the Dirt block. This way, displayable text may be referenced with no concern for a specific language. The code requires no changes to be adapted in a new language.

Localization will happen in the game's locale. In a Minecraft client the locale is specified by the language settings. On a dedicated server, the only supported locale is `en_us`. A list of available locales can be found on the [Minecraft Wiki][langs].

Language files
--------------

Language files are located by `assets/[namespace]/lang/[locale].json` (e.g. all US English translations provided by `examplemod` would be within `assets/examplemod/lang/en_us.json`). The file format is simply a json map from translation keys to values. The file must be encoded in UTF-8. Old .lang files can be converted to json using a [converter][converter].

```js
{
  "item.examplemod.example_item": "Example Item Name",
  "block.examplemod.example_block": "Example Block Name",
  "commands.examplemod.examplecommand.error": "Example Command Errored!"
}
```

Usage with Blocks and Items
---------------------------

Block, Item and a few other Minecraft classes have built-in translation keys used to display their names. These translation keys are specified by overriding `#getDescriptionId`. Item also has `#getDescriptionId(ItemStack)` which can be overridden to provide different translation keys depending on ItemStack NBT.

By default, `#getDescriptionId` will return `block.` or `item.` prepended to the registry name of the block or item, with the colon replaced by a dot. `BlockItem`s override this method to take their corresponding `Block`'s translation key by default. For example, an item with ID `examplemod:example_item` effectively requires the following line in a language file:

```js
{
  "item.examplemod.example_item": "Example Item Name"
}
```

!!! note
    The only purpose of a translation key is internationalization. Do not use them for logic. Use registry names instead.


Localization methods
--------------------

!!! warning
    A common issue is having the server localize for clients. The server can only localize in its own locale, which does not necessarily match the locale of connected clients.
    
    To respect the language settings of clients, the server should have clients localize text in their own locale using `TranslatableComponent` or other methods preserving the language neutral translation keys.

### `net.minecraft.client.resources.language.I18n` (client only)

**This I18n class can only be found on a Minecraft client!** It is intended to be used by code that only runs on the client. Attempts to use this on a server will throw exceptions and crash.

- `get(String, Object...)` localizes in the client's locale with formatting. The first parameter is a translation key, and the rest are formatting arguments for `String.format(String, Object...)`.

### `TranslatableContents`

`TranslatableContents` is a `ComponentContents` that is localized and formatted lazily. It is very useful when sending messages to players because it will be automatically localized in their own locale.

The first parameter of the `TranslatableContents(String, Object...)` constructor is a translation key, and the rest are used for formatting. The only supported format specifiers are `%s` and `%1$s`, `%2$s`, `%3$s` etc. Formatting arguments may be `Component`s that will be inserted into the resulting formatted text with all their attributes preserved.

A `MutableComponent` can be created using `Component#translatable` by passing in the `TranslatableContents`'s parameters. It can also be created using `MutableComponent#create` by passing in the `ComponentContents` itself.

### `TextComponentHelper`

- `createComponentTranslation(CommandSource, String, Object...)` creates a localized and formatted `MutableComponent` depending on a receiver. The localization and formatting is done eagerly if the receiver is a vanilla client. If not, the localization and formatting is done lazily with a `Component` containing `TranslatableContents`. This is only useful if the server should allow vanilla clients to connect.

[langs]: https://minecraft.wiki/w/Language#Languages
[converter]: https://tterrag.com/lang2json/

# # Menus

Menus are one type of backend for Graphical User Interfaces, or GUIs; they handle the logic involved in interacting with some represented data holder. Menus themselves are not data holders. They are views which allow to user to indirectly modify the internal data holder state. As such, a data holder should not be directly coupled to any menu, instead passing in the data references to invoke and modify.

## `MenuType`

Menus are created and removed dynamically and as such are not registry objects. As such, another factory object is registered instead to easily create and refer to the *type* of the menu. For a menu, these are `MenuType`s.

`MenuType`s must be [registered].

### `MenuSupplier`

A `MenuType` is created by passing in a `MenuSupplier` and a `FeatureFlagSet` to its constructor. A `MenuSupplier` represents a function which takes in the id of the container and the inventory of the player viewing the menu, and returns a newly created [`AbstractContainerMenu`][acm].

```java
// For some DeferredRegister<MenuType<?>> REGISTER
public static final RegistryObject<MenuType<MyMenu>> MY_MENU = REGISTER.register("my_menu", () -> new MenuType(MyMenu::new, FeatureFlags.DEFAULT_FLAGS));

// In MyMenu, an AbstractContainerMenu subclass
public MyMenu(int containerId, Inventory playerInv) {
  super(MY_MENU.get(), containerId);
  // ...
}
```

!!! note
    The container identifier is unique for an individual player. This means that the same container id on two different players will represent two different menus, even if they are viewing the same data holder.

The `MenuSupplier` is usually responsible for creating a menu on the client with dummy data references used to store and interact with the synced information from the server data holder.

### `IContainerFactory`

If additional information is needed on the client (e.g. the position of the data holder in the world), then the subclass `IContainerFactory` can be used instead. In addition to the container id and the player inventory, this also provides a `FriendlyByteBuf` which can store additional information that was sent from the server. A `MenuType` can be created using an `IContainerFactory` via `IForgeMenuType#create`.

```java
// For some DeferredRegister<MenuType<?>> REGISTER
public static final RegistryObject<MenuType<MyMenuExtra>> MY_MENU_EXTRA = REGISTER.register("my_menu_extra", () -> IForgeMenuType.create(MyMenu::new));

// In MyMenuExtra, an AbstractContainerMenu subclass
public MyMenuExtra(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
  super(MY_MENU_EXTRA.get(), containerId);
  // Store extra data from buffer
  // ...
}
```

## `AbstractContainerMenu`

All menus are extended from `AbstractContainerMenu`. A menu takes in two parameters, the [`MenuType`][mt], which represents the type of the menu itself, and the container id, which represents the unique identifier of the menu for the current accessor.

!!! important
    The player can only have 100 unique menus open at once.

Each menu should contain two constructors: one used to initialize the menu on the server and one used to initialize the menu on the client. The constructor used to initialize the menu on the client is the one supplied to the `MenuType`. Any fields that the server menu constructor contains should have some default for the client menu constructor.

```java
// Client menu constructor
public MyMenu(int containerId, Inventory playerInventory) {
  this(containerId, playerInventory);
}

// Server menu constructor
public MyMenu(int containerId, Inventory playerInventory) {
  // ...
}
```

Each menu implementation must implement two methods: `#stillValid` and [`#quickMoveStack`][qms].

### `#stillValid` and `ContainerLevelAccess`

`#stillValid` determines whether the menu should remain open for a given player. This is typically directed to the static `#stillValid` which takes in a `ContainerLevelAccess`, the player, and the `Block` this menu is attached to. The client menu must always return `true` for this method, which the static `#stillValid` does default to. This implementation checks whether the player is within eight blocks of where the data storage object is located.

A `ContainerLevelAccess` supplies the current level and location of the block within an enclosed scope. When constructing the menu on the server, a new access can be created by calling `ContainerLevelAccess#create`. The client menu constructor can pass in `ContainerLevelAccess#NULL`, which will do nothing.

```java
// Client menu constructor
public MyMenuAccess(int containerId, Inventory playerInventory) {
  this(containerId, playerInventory, ContainerLevelAccess.NULL);
}

// Server menu constructor
public MyMenuAccess(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
  // ...
}

// Assume this menu is attached to RegistryObject<Block> MY_BLOCK
@Override
public boolean stillValid(Player player) {
  return AbstractContainerMenu.stillValid(this.access, player, MY_BLOCK.get());
}
```

### Data Synchronization

Some data needs to be present on both the server and the client to display to the player. To do this, the menu implements a basic layer of data synchronization such that whenever the current data does not match the data last synced to the client. For players, this is checked every tick.

Minecraft supports two forms of data synchronization by default: `ItemStack`s via `Slot`s and integers via `DataSlot`s. `Slot`s and `DataSlot`s are views which hold references to data storages that can be be modified by the player in a screen, assuming the action is valid. These can be added to a menu within the constructor through `#addSlot` and `#addDataSlot`.

!!! note
    Since `Container`s used by `Slot`s are deprecated by Forge in favor of using the [`IItemHandler` capability][cap], the rest of the explanation will revolve around using the capability variant: `SlotItemHandler`.

A `SlotItemHandler` contains four parameters: the `IItemHandler` representing the inventory the stacks are within, the index of the stack this slot is specifically representing, and the x and y position of where the top-left position of the slot will render on the screen relative to `AbstractContainerScreen#leftPos` and `#topPos`. The client menu constructor should always supply an empty instance of an inventory of the same size.

In most cases, any slots the menu contains is first added, followed by the player's inventory, and finally concluded with the player's hotbar. To access any individual `Slot` from the menu, the index must be calculated based upon the order of which slots were added.

A `DataSlot` is an abstract class which should implement a getter and setter to reference the data stored in the data storage object. The client menu constructor should always supply a new instance via `DataSlot#standalone`.

These, along with slots, should be recreated every time a new menu is initialized.

!!! warning
    Although a `DataSlot` stores an integer, it is effectively limited to a **short** (-32768 to 32767) because of how it sends the value across the network. The 16 high-order bits of the integer are ignored.

```java
// Assume we have an inventory from a data object of size 5
// Assume we have a DataSlot constructed on each initialization of the server menu

// Client menu constructor
public MyMenuAccess(int containerId, Inventory playerInventory) {
  this(containerId, playerInventory, new ItemStackHandler(5), DataSlot.standalone());
}

// Server menu constructor
public MyMenuAccess(int containerId, Inventory playerInventory, IItemHandler dataInventory, DataSlot dataSingle) {
  // Check if the data inventory size is some fixed value
  // Then, add slots for data inventory
  this.addSlot(new SlotItemHandler(dataInventory, /*...*/));

  // Add slots for player inventory
  this.addSlot(new Slot(playerInventory, /*...*/));

  // Add data slots for handled integers
  this.addDataSlot(dataSingle);

  // ...
}
```

#### `ContainerData`

If multiple integers need to be synced to the client, a `ContainerData` can be used to reference the integers instead. This interface functions as an index lookup such that each index represents a different integer. `ContainerData`s can also be constructed in the data object itself if the `ContainerData` is added to the menu through `#addDataSlots`. The method creates a new `DataSlot` for the amount of data specified by the interface. The client menu constructor should always supply a new instance via `SimpleContainerData`.

```java
// Assume we have a ContainerData of size 3

// Client menu constructor
public MyMenuAccess(int containerId, Inventory playerInventory) {
  this(containerId, playerInventory, new SimpleContainerData(3));
}

// Server menu constructor
public MyMenuAccess(int containerId, Inventory playerInventory, ContainerData dataMultiple) {
  // Check if the ContainerData size is some fixed value
  checkContainerDataCount(dataMultiple, 3);

  // Add data slots for handled integers
  this.addDataSlots(dataMultiple);

  // ...
}
```

!!! warning
    As `ContainerData` delegates to `DataSlot`s, these are also limited to a **short** (-32768 to 32767).

#### `#quickMoveStack`

`#quickMoveStack` is the second method that must be implemented by any menu. This method is called whenever a stack has been shift-clicked, or quick moved, out of its current slot until the stack has been fully moved out of its previous slot or there is no other place for the stack to go. The method returns a copy of the stack in the slot being quick moved.

Stacks are typically moved between slots using `#moveItemStackTo`, which moves the stack into the first available slot. It takes in the stack to be moved, the first slot index (inclusive) to try and move the stack to, the last slot index (exclusive), and whether to check the slots from first to last (when `false`) or from last to first (when `true`).

Across Minecraft implementations, this method is fairly consistent in its logic:

```java
// Assume we have a data inventory of size 5
// The inventory has 4 inputs (index 1 - 4) which outputs to a result slot (index 0)
// We also have the 27 player inventory slots and the 9 hotbar slots
// As such, the actual slots are indexed like so:
//   - Data Inventory: Result (0), Inputs (1 - 4)
//   - Player Inventory (5 - 31)
//   - Player Hotbar (32 - 40)
@Override
public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
  // The quick moved slot stack
  ItemStack quickMovedStack = ItemStack.EMPTY;
  // The quick moved slot
  Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex) 
  
   // If the slot is in the valid range and the slot is not empty
  if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
    // Get the raw stack to move
    ItemStack rawStack = quickMovedSlot.getItem(); 
    // Set the slot stack to a copy of the raw stack
    quickMovedStack = rawStack.copy();

    /*
    The following quick move logic can be simplified to if in data inventory,
    try to move to player inventory/hotbar and vice versa for containers
    that cannot transform data (e.g. chests).
    */

    // If the quick move was performed on the data inventory result slot
    if (quickMovedSlotIndex == 0) {
      // Try to move the result slot into the player inventory/hotbar
      if (!this.moveItemStackTo(rawStack, 5, 41, true)) {
        // If cannot move, no longer quick move
        return ItemStack.EMPTY;
      }

      // Perform logic on result slot quick move
      slot.onQuickCraft(rawStack, quickMovedStack);
    }
    // Else if the quick move was performed on the player inventory or hotbar slot
    else if (quickMovedSlotIndex >= 5 && quickMovedSlotIndex < 41) {
      // Try to move the inventory/hotbar slot into the data inventory input slots
      if (!this.moveItemStackTo(rawStack, 1, 5, false)) {
        // If cannot move and in player inventory slot, try to move to hotbar
        if (quickMovedSlotIndex < 32) {
          if (!this.moveItemStackTo(rawStack, 32, 41, false)) {
            // If cannot move, no longer quick move
            return ItemStack.EMPTY;
          }
        }
        // Else try to move hotbar into player inventory slot
        else if (!this.moveItemStackTo(rawStack, 5, 32, false)) {
          // If cannot move, no longer quick move
          return ItemStack.EMPTY;
        }
      }
    }
    // Else if the quick move was performed on the data inventory input slots, try to move to player inventory/hotbar
    else if (!this.moveItemStackTo(rawStack, 5, 41, false)) {
      // If cannot move, no longer quick move
      return ItemStack.EMPTY;
    }

    if (rawStack.isEmpty()) {
      // If the raw stack has completely moved out of the slot, set the slot to the empty stack
      quickMovedSlot.set(ItemStack.EMPTY);
    } else {
      // Otherwise, notify the slot that that the stack count has changed
      quickMovedSlot.setChanged();
    }

    /*
    The following if statement and Slot#onTake call can be removed if the
    menu does not represent a container that can transform stacks (e.g.
    chests).
    */
    if (rawStack.getCount() == quickMovedStack.getCount()) {
      // If the raw stack was not able to be moved to another slot, no longer quick move
      return ItemStack.EMPTY;
    }
    // Execute logic on what to do post move with the remaining stack
    quickMovedSlot.onTake(player, rawStack);
  }

  return quickMovedStack; // Return the slot stack
}
```

## Opening a Menu

Once a menu type has been registered, the menu itself has been finished, and a [screen] has been attached, a menu can then be opened by the player. Menus can be opened by calling `ServerPlayer#openMenu` on the logical server. The method takes in the `MenuProvider` of the server side menu, and optionally a `FriendlyByteBuf` if extra data needs to be synced to the client.

!!! note
    `ServerPlayer#openMenu` with the `FriendlyByteBuf` parameter should only be used if a menu type was created using an [`IContainerFactory`][icf].

#### `MenuProvider`

A `MenuProvider` is an interface that contains two methods: `#createMenu`, which creates the server instance of the menu, and `#getDisplayName`, which returns a component containing the title of the menu to pass to the [screen]. The `#createMenu` method contains three parameter: the container id of the menu, the inventory of the player who opened the menu, and the player who opened the menu.

A `MenuProvider` can easily be created using `SimpleMenuProvider`, which takes in a method reference to create the server menu and the title of the menu.

```java
// In some implementation
serverPlayer.openMenu(new SimpleMenuProvider(
  (containerId, playerInventory, player) -> new MyMenu(containerId, playerInventory),
  Component.translatable("menu.title.examplemod.mymenu")
));
```

### Common Implementations

Menus are typically opened on a player interaction of some kind (e.g. when a block or entity is right-clicked).

#### Block Implementation

Blocks typically implement a menu by overriding `BlockBehaviour#use`. If on the logical client, the interaction returns `InteractionResult#SUCCESS`. Otherwise, it opens the menu and returns `InteractionResult#CONSUME`.

The `MenuProvider` should be implemented by overriding `BlockBehaviour#getMenuProvider`. Vanilla methods use this to view the menu in spectator mode.

```java
// In some Block subclass
@Override
public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
  return new SimpleMenuProvider(/* ... */);
}

@Override
public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
  if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
    serverPlayer.openMenu(state.getMenuProvider(level, pos));
  }
  return InteractionResult.sidedSuccess(level.isClientSide);
}
```

!!! note
    This is the simplest way to implement the logic, not the only way. If you want the block to only open the menu under certain conditions, then some data will need to be synced to the client beforehand to return `InteractionResult#PASS` or `#FAIL` if the conditions are not met.

#### Mob Implementation

Mobs typically implement a menu by overriding `Mob#mobInteract`. This is done similarly to the block implementation with the only difference being that the `Mob` itself should implement `MenuProvider` to support spectator mode viewing.

```java
public class MyMob extends Mob implements MenuProvider {
  // ...

  @Override
  public InteractionResult mobInteract(Player player, InteractionHand hand) {
    if (!this.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      serverPlayer.openMenu(this);
    }
    return InteractionResult.sidedSuccess(this.level.isClientSide);
  }
}
```

!!! note
    Once again, this is the simplest way to implement the logic, not the only way.

[registered]: ../concepts/registries.md#methods-for-registering
[acm]: #abstractcontainermenu
[mt]: #menutype
[qms]: #quickmovestack
[cap]: ../datastorage/capabilities.md#forge-provided-capabilities
[screen]: ./screens.md
[icf]: #icontainerfactory


# # Screens

Screens are typically the base of all Graphical User Interfaces (GUIs) in Minecraft: taking in user input, verifying it on the server, and syncing the resulting action back to the client. They can be combined with [menus] to create an communication network for inventory-like views, or they can be standalone which modders can handle through their own [network] implementations.

Screens are made up of numerous parts, making it difficult to fully understand what a 'screen' actually is in Minecraft. As such, this document will go over each of the screen's components and how it is applied before discussing the screen itself.

## Relative Coordinates

Whenever anything is rendered, there needs to be some identifier which specifies where it will appear. With numerous abstractions, most of Minecraft's rendering calls takes in an x, y, and z value in a coordinate plane. X values increase from left to right, y from top to bottom, and z from far to near. However, the coordinates are not fixed to a specified range. They can change depending on the size of the screen and the scale at which is specified within the options. As such, extra care must be taken to make sure the values of the coordinates while rendering scale properly to the changeable screen size.

Information on how to relativize your coordinates will be within the [screen] section.

!!! important
    If you choose to use fixed coordinates or incorrectly scale the screen, the rendered objects may look strange or misplaced. An easy way to check if you relativized your coordinates correctly is to click the 'Gui Scale' button in your video settings. This value is used as the divisor to the width and height of your display when determining the scale at which a GUI should render.

## Gui Graphics

Any GUI rendered by Minecraft is typically done using `GuiGraphics`. `GuiGraphics` is the first parameter to almost all rendering methods; it contains basic methods to render commonly used objects. These fall into five categories: colored rectangles, strings, and textures, items, and tooltips. There is also an additional method for rendering a snippet of a component (`#enableScissor` / `#disableScissor`). `GuiGraphics` also exposes the `PoseStack` which applies the transformations necessary to properly render where the component should be rendered. Additionally, colors are in the [ARGB][argb] format.

### Colored Rectangles

Colored rectangles are drawn through a position color shader. There are three types of colored rectangles that can be drawn.

First, there is a colored horizontal and vertical one-pixel wide line, `#hLine` and `#vLine` respectively. `#hLine` takes in two x coordinates defining the left and right (inclusively), the top y coordinate, and the color. `#vLine` takes in the left x coordinate, two y coordinates defining the top and bottom (inclusively), and the color.

Second, there is the `#fill` method, which draws a rectangle to the screen. The line methods internally call this method. This takes in the left x coordinate, the top y coordinate, the right x coordinate, the bottom y coordinate, and the color.

Finally, there is the `#fillGradient` method, which draws a rectangle with a vertical gradient. This takes in the right x coordinate, the bottom y coordinate, the left x coordinate, the top y coordinate, the z coordinate, and the bottom and top colors.

### Strings

Strings are drawn through its `Font`, typically consisting of their own shaders for normal, see through, and offset mode. There are two alignment of strings that can be rendered, each with a back shadow: a left-aligned string (`#drawString`) and a center-aligned string (`#drawCenteredString`). These both take in the font the string will be rendered in, the string to draw, the x coordinate representing the left or center of the string respectively, the top y coordinate, and the color.

!!! note
    Strings should typically be passed in as [`Component`s][component] as they handle a variety of usecases, including the two other overloads of the method.

### Textures

Textures are drawn through blitting, hence the method name `#blit`, which, for this purpose, copies the bits of an image and draws them directly to the screen. These are drawn through a position texture shader. While there are many different `#blit` overloads, we will only discuss two static `#blit`s.

The first static `#blit` takes in six integers and assumes the texture being rendered is on a 256 x 256 PNG file. It takes in the left x and top y screen coordinate, the left x and top y coordinate within the PNG, and the width and height of the image to render.

!!! note
    The size of the PNG file must be specified so that the coordinates can be normalized to obtain the associated UV values.

The static `#blit` which the first calls expands this to nine integers, only assuming the image is on a PNG file. It takes in the left x and top y screen coordinate, the z coordinate (referred to as the blit offset), the left x and top y coordinate within the PNG, the width and height of the image to render, and the width and height of the PNG file.

#### Blit Offset

The z coordinate when rendering a texture is typically set to the blit offset. The offset is responsible for properly layering renders when viewing a screen. Renders with a smaller z coordinate are rendered in the background and vice versa where renders with a larger z coordinate are rendered in the foreground. The z offset can be set directly on the `PoseStack` itself via `#translate`. Some basic offset logic is applied internally in some methods of `GuiGraphics` (e.g. item rendering).

!!! important
    When setting the blit offset, you must reset it after rendering your object. Otherwise, other objects within the screen may be rendered in an incorrect layer causing graphical issues. It is recommended to push the current pose before translating and then popping after all rendering at the offset is completed.

## Renderable

`Renderable`s are essentially objects that are rendered. These include screens, buttons, chat boxes, lists, etc. `Renderable`s only have one method: `#render`. This takes in the `GuiGraphics` used to render things to the screen, the x and y positions of the mouse scaled to the relative screen size, and the tick delta (how many ticks have passed since the last frame).

Some common renderables are screens and 'widgets': interactable elements which typically render on the screen such as `Button`, its subtype `ImageButton`, and `EditBox` which is used to input text on the screen.

## GuiEventListener

Any screen rendered in Minecraft implements `GuiEventListener`. `GuiEventListener`s are responsible for handling user interaction with the screen. These include inputs from the mouse (movement, clicked, released, dragged, scrolled, mouseover) and keyboard (pressed, released, typed). Each method returns whether the associated action affected the screen successfully. Widgets like buttons, chat boxes, lists, etc. also implement this interface.

### ContainerEventHandler

Almost synonymous with `GuiEventListener`s are their subtype: `ContainerEventHandler`s. These are responsible for handling user interaction on screens which contain widgets, managing which is currently focused and how the associated interactions are applied. `ContainerEventHandler`s add three additional features: interactable children, dragging, and focusing.

Event handlers hold children which are used to determine the interaction order of elements. During the mouse event handlers (excluding dragging), the first child in the list that the mouse hovers over has their logic executed.

Dragging an element with the mouse, implemented via `#mouseClicked` and `#mouseReleased`, provides more precisely executed logic.

Focusing allows for a specific child to be checked first and handled during an event's execution, such as during keyboard events or dragging the mouse. Focus is typically set through `#setFocused`. In addition, interactable children can be cycled using `#nextFocusPath`, selecting the child based upon the `FocusNavigationEvent` passed in.

!!! note
    Screens implement `ContainerEventHandler` through `AbstractContainerEventHandler`, which adds in the setter and getter logic for dragging and focusing children.

## NarratableEntry

`NarratableEntry`s are elements which can be spoken about through Minecraft's accessibility narration feature. Each element can provide different narration depending on what is hovered or selected, prioritized typically by focus, hovering, and then all other cases.

`NarratableEntry`s have three methods: one which determines the priority of the element (`#narrationPriority`), one which determines whether to speak the narration (`#isActive`), and finally one which supplies the narration to its associated output, spoken or read (`#updateNarration`). 

!!! note
    All widgets from Minecraft are `NarratableEntry`s, so it typically does not need to be manually implemented if using an available subtype.

## The Screen Subtype

With all of the above knowledge, a basic screen can be constructed. To make it easier to understand, the components of a screen will be mentioned in the order they are typically encountered.

First, all screens take in a `Component` which represents the title of the screen. This component is typically drawn to the screen by one of its subtypes. It is only used in the base screen for the narration message.

```java
// In some Screen subclass
public MyScreen(Component title) {
    super(title);
}
```

### Initialization

Once a screen has been initialized, the `#init` method is called. The `#init` method sets the initial settings inside the screen from the `ItemRenderer` and `Minecraft` instance to the relative width and height as scaled by the game. Any setup such as adding widgets or precomputing relative coordinates should be done in this method. If the game window is resized, the screen will be reinitialized by calling the `#init` method.

There are three ways to add a widget to a screen, each serving a separate purpose:

Method                 | Description
:---:                  | :---
`#addWidget`           | Adds a widget that is interactable and narrated, but not rendered.
`#addRenderableOnly`   | Adds a widget that will only be rendered; it is not interactable or narrated.
`#addRenderableWidget` | Adds a widget that is interactable, narrated, and rendered.

Typically, `#addRenderableWidget` will be used most often.

```java
// In some Screen subclass
@Override
protected void init() {
    super.init();

    // Add widgets and precomputed values
    this.addRenderableWidget(new EditBox(/* ... */));
}
```

### Ticking Screens

Screens also tick using the `#tick` method to perform some level of client side logic for rendering purposes. The most common example is the `EditBox` for the blinking cursor.

```java
// In some Screen subclass
@Override
public void tick() {
    super.tick();

    // Add ticking logic for EditBox in editBox
    this.editBox.tick();
}
```

### Input Handling

Since screens are subtypes of `GuiEventListener`s, the input handlers can also be overridden, such as for handling logic on a specific [key press][keymapping].

### Rendering the Screen

Finally, screens are rendered through the `#render` method provided by being a `Renderable` subtype. As mentioned, the `#render` method draws the everything the screen has to render every frame, such as the background, widgets, tooltips, etc. By default, the `#render` method only renders the widgets to the screen.

The two most common things rendered within a screen that is typically not handled by a subtype is the background and the tooltips.

The background can be rendered using `#renderBackground`, with one method taking in a v Offset for the options background whenever a screen is rendered when the level behind it cannot be.

Tooltips are rendered through `GuiGraphics#renderTooltip` or `GuiGraphics#renderComponentTooltip` which can take in the text components being rendered, an optional custom tooltip component, and the x / y relative coordinates on where the tooltip should be rendered on the screen.

```java
// In some Screen subclass

// mouseX and mouseY indicate the scaled coordinates of where the cursor is in on the screen
@Override
public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    // Background is typically rendered first
    this.renderBackground(graphics);

    // Render things here before widgets (background textures)

    // Then the widgets if this is a direct child of the Screen
    super.render(graphics, mouseX, mouseY, partialTick);

    // Render things after widgets (tooltips)
}
```

### Closing the Screen

When a screen is closed, two methods handle the teardown: `#onClose` and `#removed`.

`#onClose` is called whenever the user makes an input to close the current screen. This method is typically used as a callback to destroy and save any internal processes in the screen itself. This includes sending packets to the server.

`#removed` is called just before the screen changes and is released to the garbage collector. This handles anything that hasn't been reset back to its initial state before the screen was opened.

```java
// In some Screen subclass

@Override
public void onClose() {
    // Stop any handlers here

    // Call last in case it interferes with the override
    super.onClose();
}

@Override
public void removed() {
    // Reset initial states here

    // Call last in case it interferes with the override
    super.removed()
;}
```

## `AbstractContainerScreen`

If a screen is directly attached to a [menu][menus], then an `AbstractContainerScreen` should be subclassed instead. An `AbstractContainerScreen` acts as the renderer and input handler of a menu and contains logic for syncing and interacting with slots. As such, only two methods typically need to be overridden or implemented to have a working container screen. Once again, to make it easier to understand, the components of a container screen will be mentioned in the order they are typically encountered.

An `AbstractContainerScreen` typically requires three parameters: the container menu being opened (represented by the generic `T`), the player inventory (only for the display name), and the title of the screen itself. Within here, a number of positioning fields can be set:

Field             | Description
:---:             | :---
`imageWidth`      | The width of the texture used for the background. This is typically inside a PNG of 256 x 256 and defaults to 176.
`imageHeight`     | The width of the texture used for the background. This is typically inside a PNG of 256 x 256 and defaults to 166.
`titleLabelX`     | The relative x coordinate of where the screen title will be rendered.
`titleLabelY`     | The relative y coordinate of where the screen title will be rendered.
`inventoryLabelX` | The relative x coordinate of where the player inventory name will be rendered.
`inventoryLabelY` | The relative y coordinate of where the player inventory name will be rendered.

!!! important
    In a previous section, it mentioned that precomputed relative coordinates should be set in the `#init` method. This still remains true, as the values mentioned here are not precomputed coordinates but static values and relativized coordinates.

    The image values are static and non changing as they represent the background texture size. To make things easier when rendering, two additional values (`leftPos` and `topPos`) are precomputed in the `#init` method which marks the top left corner of where the background will be rendered. The label coordinates are relative to these values.

    The `leftPos` and `topPos` is also used as a convenient way to render the background as they already represent the position to pass into the `#blit` method.

```java
// In some AbstractContainerScreen subclass
public MyContainerScreen(MyMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);

    this.titleLabelX = 10;
    this.inventoryLabelX = 10;

    /*
     * If the 'imageHeight' is changed, 'inventoryLabelY' must also be
     * changed as the value depends on the 'imageHeight' value.
     */
}
```

### Menu Access

As the menu is passed into the screen, any values that were within the menu and synced (either through slots, data slots, or a custom system) can now be accessed through the `menu` field.

### Container Tick

Container screens tick within the `#tick` method when the player is alive and looking at the screen via `#containerTick`. This essentially takes the place of `#tick` within container screens, with its most common usage being to tick the recipe book.

```java
// In some AbstractContainerScreen subclass
@Override
protected void containerTick() {
    super.containerTick();

    // Tick things here
}
```

### Rendering the Container Screen

The container screen is rendered across three methods: `#renderBg`, which renders the background textures, `#renderLabels`, which renders any text on top of the background, and `#render` which encompass the previous two methods in addition to providing a grayed out background and tooltips.

Starting with `#render`, the most common override (and typically the only case) adds the background, calls the super to render the container screen, and finally renders the tooltips on top of it.

```java
// In some AbstractContainerScreen subclass
@Override
public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    this.renderBackground(graphics);
    super.render(graphics, mouseX, mouseY, partialTick);

    /*
     * This method is added by the container screen to render
     * the tooltip of the hovered slot.
     */
    this.renderTooltip(graphics, mouseX, mouseY);
}
```

Within the super, `#renderBg` is called to render the background of the screen. The most standard representation uses three method calls: two for setup and one to draw the background texture.

```java
// In some AbstractContainerScreen subclass

// The location of the background texture (assets/<namespace>/<path>)
private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(MOD_ID, "textures/gui/container/my_container_screen.png");

@Override
protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    /*
     * Renders the background texture to the screen. 'leftPos' and
     * 'topPos' should already represent the top left corner of where
     * the texture should be rendered as it was precomputed from the
     * 'imageWidth' and 'imageHeight'. The two zeros represent the
     * integer u/v coordinates inside the 256 x 256 PNG file.
     */
    graphics.blit(BACKGROUND_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
}
```

Finally, `#renderLabels` is called to render any text above the background, but below the tooltips. This simply calls uses the font to draw the associated components.

```java
// In some AbstractContainerScreen subclass
@Override
protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    super.renderLabels(graphics, mouseX, mouseY);

    // Assume we have some Component 'label'
    // 'label' is drawn at 'labelX' and 'labelY'
    graphics.drawString(this.font, this.label, this.labelX, this.labelY, 0x404040);
}
```

!!! note
    When rendering the label, you do **not** need to specify the `leftPos` and `topPos` offset. Those have already been translated within the `PoseStack` so everything within this method is drawn relative to those coordinates.

## Registering an AbstractContainerScreen

To use an `AbstractContainerScreen` with a menu, it needs to be registered. This can be done by calling `MenuScreens#register` within the `FMLClientSetupEvent` on the [**mod event bus**][modbus].

```java
// Event is listened to on the mod event bus
private void clientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(
        // Assume RegistryObject<MenuType<MyMenu>> MY_MENU
        // Assume MyContainerScreen<MyMenu> which takes in three parameters
        () -> MenuScreens.register(MY_MENU.get(), MyContainerScreen::new)
    );
}
```

!!! warning
    `MenuScreens#register` is not thread-safe, so it needs to be called inside `#enqueueWork` provided by the parallel dispatch event.

[menus]: ./menus.md
[network]: ../networking/index.md
[screen]: #the-screen-subtype
[argb]: https://en.wikipedia.org/wiki/RGBA_color_model#ARGB32
[component]: ../concepts/internationalization.md#translatablecontents
[keymapping]: ../misc/keymappings.md#inside-a-gui
[modbus]: ../concepts/events.md#mod-event-bus

# Para mais consulte:
https://github.com/MinecraftForge/Documentation/tree/1.19.x/docs
https://github.com/MinecraftForge/Documentation/tree/1.20.x/docs
https://github.com/MinecraftForge/Documentation/tree/1.21.x/docs