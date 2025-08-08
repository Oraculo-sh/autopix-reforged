# Forge Modding Tutorials 

This site will teach you how to make Minecraft mods for the [Forge](https://github.com/MinecraftForge/MinecraftForge) mod loader.

I make these because I was frustrated by the lack of documentation and useable tutorials for Forge. 
All the tutorials I could find were long videos of someone talking extremely slowly. 
I hope to offer an alternative: text tutorials for those who don't need a visual to follow along. 
Hopefully my tutorials will be less painful to refer to if you happen to forget something.

- If this is your first time modding you should probably follow the tutorials in order. 
- New tutorials will be created for 1.16.5, 1.18.2, 1.19.2, and 1.19.3. 
- It's entirely possible that there will be small mistakes in porting the tutorial text between versions. If you notice any, please [let me know](/discord)! 
- The source code for the tutorial mod is at [LukeGrahamLandry/modding-tutorials](/github). 
- Any of my code in the tutorials is public domain. Feel free to use it in your own projects.
- If you find these helpful, consider donating on [Patreon](/patreon).

# Introduction to Modding 

Minecraft is a fun game with a world made of cubes. Over time, dedicated players decided there were not enough types of cubes so they started writing code that modified the base game (mods) to add new content. This website will teach you how to write your own mods to implement any new features you can imagine. 

## Versions

Over time, Mojang/Microsoft releases updates to Minecraft that add new content. Unfortunately, they also change lots of things about how Minecraft's code works internally each update as well. This means that any given mod is only compatible with the version of minecraft it was made for. Porting mods between versions is sometimes trivial but often quite difficult, depending on how much has changed in the vanilla code that the mod must interact with.

This site has tutorials for versions 1.19.2, 1.18.2, and 1.16.5. You can see them all in the sidebar. 

## Mod Loaders

Minecraft does not natively allow you to load code that modifies the game. Helpfully, some very clever people wrote some complicated code that provides a framework that can load user created chunks of code (called mods) that are run along side minecraft's existing code. The programs that do this for you are called mod loaders. They generally provide an additional layer of abstraction that helps you interact with minecraft's code to change the game more easily (called an api). 

Important to note is that players can only use one mod loader at a time so mods written for different mod loaders are incompatible. 

Each mod loader will distribute an installer program that players can run to add that mod loader to their minecraft installation. It will generally create a new profile in the vanilla launcher so you can still easily choose between different versions or mod loaders. You can also use third party launchers, like CurseForge or MultiMC, to make this process easier. 

### Forge

> Exists because a long time ago, mods did hacky things that were incompatible with each other so it was hard to make big mod packs. 

Forge was one of the first mod loaders and is still by far the most popular. It provides a large api that makes it easier to change lots of things. It patches minecraft's code in many places to add event hooks that will call your mod code to make it easy to change lots vanilla behavior while maintaining compatibility with other mods that try to change similar things (or at least not crashing). There are also helpful things like capabilities (fancy abstract data storage) like Forge Energy (RF) for power, a fluid api, additional attributes (swim speed, gravity), and abstractions over object registration. However, since this api is so large and interacts so much with vanilla's code, forge often takes a long time to update to new Minecraft versions. 

Forge has downloads available all the way back to Minecraft version 1.1.

### Fabric 

> Exists because the dude runs forge's discord server is mean to people who don't learn java and that's rude. 

Fabric started when minecraft was at 1.14 as a competitor to forge, mostly because forge took a long time to update. Fabric has a more modular pilosity than forge, they keep the mod loader as light as possible so it can be updated to new versions quickly. They also provide the fabric api that makes it easier to implement lots of thing as a mod that can be downloaded severalty. Technically, that means that you could write fabric mods without depending on their api but thats rarely done. Fabric's api is still less all encompassing than forge's so you have to do more things for yourself. Fabric also supports jar-in-jar mods more elegantly than forge which lets you easily bundle dependencies within your own mod file. This two factors combined lead to a culture of obsessively depending on random libraries. 

### Quilt

> Exists because the dude that runs fabric's discord server is mean to trans people and that's rude. 

Quilt is a fork of fabric made because a group of people disagreed with how the fabric community was run. They have their own api (the quilt-standard-library or QSL) and for the time being, maintain a fork of the fabric api that reimplements everything so that fabric mods can run on quilt with minimal changes. Which means that in practice, there's no reason for players to use the fabric loader at all as quilt can load fabric mods and more. If this lures players over, then the quilt team can break compatibility with fabric and perhaps keep most of the players using quilt. They have a lot of cool technical promises as well but thus far there only exists a beta whose selling point is being just as good as fabric and maybe having a more democratic governance system for the project.  

> I'd say that quilt's team has quite the duplicitous scheme going on but I've got to agree transphobia is unpleasant. Plus, if quilt breaks compatibility with fabric then i get to have lots of commissions porting things between them so who am I to complain. 

### Plugins

Plugins are a special type of mod for servers only. Vanilla clients can connect to these modded servers but the types of changes they can make are much more limited. There are many loaders for these that all seem to be forks of each other ([Spigot](https://www.spigotmc.org/), [Bukkit](https://dev.bukkit.org/), [Sponge](https://www.spongepowered.org/), [Paper](https://papermc.io/)). They tend to provide a very abstract api layer over Mojang's code so that developers using them don't have to worry too much about what's really going on. However, this makes them extra difficult to update to new minecraft versions.

I feel that there's really no point in using these plugin apis because the other mod loaders can also make server side only mods (such that vanilla clients can connect to modded servers). However, if you make your server side mod with forge/fabric/quilt, then they have the ability to be used with modded clients as well. The only reason people use plugins is they want to use other plugins that don't have mods that can replace their functionality yet. 

## Distribution

Once you finish writing the code for your mod, you package it into a nice little jar file that people can add to their game by just dropping it in their `minecraft/mods` folder. 

### CurseForge 

CurseForge is the most popular website where people distribute their mods. Developers can write descriptions (with the shittiest text editor in the universe), mark other mods as dependencies, and receive revenue from ads on the site in the form of "curse points" that can be redeemed via PayPal. Players can easily browse through lots of mods and modpacks (with the shittiest search interface in the universe) to collect those they want to play with. CurseForge also has their own Minecraft launcher that automatically installs different versions mod loaders and mod packs, automatically installing any dependencies for the mods you want to play. 

#### Modrinth

Modrinth is an open source competitor to CurseForge that is several orders of magnitude less popular.

### Github 

Github is a website for sharing source code with other developers. It makes it easy to put the code for your mod online to let other people learn from it. Almost all minecraft mods have their code freely available on github for the world to enjoy. 

Github uses a version control system called Git. Git lets you easily track changes in your code as you update your mod and add new features, as well as roll back any mistakes easily. This is a ridiculously important tool to learn how to use! It shines most if you end up working with multiple other people on the same project and need to merge your changes together. You should really install the Github Desktop app at some point and play around with it. It may seem strange at first but it will make life so much easier in the long run. 

### Licensing

When you release your mod, you generally include a license file that tells people what they're allowed to do with your work. For example, 

- can they redistribute your mod? 
- can they make addon mods?
- can they make derivatives like updating your mod to new versions?
- can they use parts of your code in their own mods?
- can they reuse your assets (textures, models, etc) for their own projects?

By default, having no license file makes your work All Rights Reserved, you own the copyright so nobody's allowed to touch it. You can read more about your options at [choosealicense.org](https://choosealicense.org) but the most popular choices are,  

- Public Domain / [CC0](https://creativecommons.org/publicdomain/zero/1.0/) / [The Unlicense](https://unlicense.org/) / [WTFPL](http://www.wtfpl.net/about/): you renounce your copyright on your work. anyone can do anything they want with your code.
- MIT, which lets anyone do anything they want with your code as long as they include your copyright notice. 
- LGPL, GNU Lesser General Public License, which lets anyone do anything with your code as long as any modified versions they create are also open source under the LGPL.

Note that you're able to release your code and art assets under different licenses. 

## Your IDE

The Integrated Development Environment (IDE) is the program you use to write your code. The most popular Java IDEs are Intellij and Eclipse. We'll install Intellij in the [Environment Setup Tutorial](environment-setup) but all the code would work exactly the same with Eclipse or even VS Code.  

These programs are basically text editors but with extra features like code completion (basically really good context aware spell check for code), error detection and utilities for debugging your code. The most important ability of your IDE is being able to look through minecraft's base code to see what you're interacting with and how they implement things. In Intellij, you can just command/control click on a class or method to "go to definition" and see what code it corresponds to. The second most important thing is Hot Swapping. This lets you swap in changes to your code without needing to restart the entirety of minecraft so you can test out changes more quickly (this can be a bit tricky to setup).  

### Gradle

Gradle is the build system used for making minecraft mods. You write a special file called `build.gradle` that tells your computer how to download minecraft's base code and dependencies and then how to build your mod against that into one cohesive program. Whatever IDE you use should include support for gradle so it knows exactly how to interpret that file to do everything it needs to. Gradle is a very powerful tool that can seem a bit confusing at first so generally you'll just stick with the template `build.gradle` provided by your mod loader's example mod. 

# Modding Concepts 

This is an overview of common concepts that come up while modding. I won't go into any specific code here but a having a general understanding of these will probably make the whole process easier. See the links in each section for resources with additional information. Feel free to skip this and move on to the specific tutorials for your version. 

## Sides

- https://docs.minecraftforge.net/en/1.19.x/concepts/sides/

## Events

- https://forge.gemwire.uk/wiki/Events
- https://docs.minecraftforge.net/en/1.19.x/concepts/events/

When Forge doesn't provide an event for whatever you're trying to do, you will have to use [Mixins](/mixins) instead. They are more complicated to set up but allow you to directly modify Minecraft's bytecode at runtime. 

## Registries 

- https://forge.gemwire.uk/wiki/Registration
- https://docs.minecraftforge.net/en/1.19.x/concepts/registries/

## Resources 

- https://docs.minecraftforge.net/en/1.19.x/concepts/resources/

## Internationalization

- https://docs.minecraftforge.net/en/1.19.x/concepts/internationalization/

## Mappings

- http://blog.minecraftforge.net/personal/sciwhiz12/what-are-mappings/

## mods.toml

- https://forge.gemwire.uk/wiki/Mods.toml

## Multi-platform Mods 

- https://github.com/jaredlll08/MultiLoader-Template
- https://docs.architectury.dev/start

## Hot-swapping

- https://forge.gemwire.uk/wiki/Hotswap

# Environment Setup

How to setup a forge development environment for 1.19.2 with the official mappings. We download java 17, forge and IntelliJ. We also rename our main package and class and update the mods.toml file.

## Downloading

First, download the JDK (java 17 development kit). Go to [adoptium.net](https://adoptium.net/temurin/releases/) and select your operating system. Choose installer for easier start as it configures system path for you, or install zip and configure java path later in your IDE (IntelliJ). Adoptium Temurin is a free implementation of JDK, you do not need any account to download and use it.

Next you need the Forge 1.19 MDK (mod development kit) from [files.minecraftforge.net](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.19.html). Get the recommended version cause its the most likely to work.
![forge mdk download page](/img/download-forge.png)
When you click the button to download the MDK it will send you to a page with ads. Very important not to click any of them (even if they look like pop ups from your OS), just wait a few seconds until the skip button appears in the top right and click that to download.

Then you need an IDE to write your code. Download intellij from [jetbrains.com](https://www.jetbrains.com/idea/download) and get the community addition because it's free.

## Installing

Double click the JDK download to open it. Just go though the installer and agree to everything. It will probably need an administrator password and take a long time to install.

Then unzip the forge MDK and rename the folder to the name of your mod. You can remove the license, readme and credits files.

Finally, launch intellijj. The first screen should let you choose some settings. You probably want dark theme but for everything else the defaults are fine. Then you want to click 'open a project'. Select the forge folder you just renamed and give it a while to do the indexing (there should be a little loading bar at the bottom of the screen).

## Setup

In the project explorer on the left open `src/main/java` and right click `com.example.examplemod` Choose refactor > rename to change your package name to something unique so you don't conflict with other mods. The convention is to named it based on a domain you own, reversed like `tld.website.modid`, so I did `ca.lukegrahamlandry.firstmod`. Make sure there's no spaces or capital letters. Open ExampleMod.java and right click the name of the class to rename it to ModNameMain. This is your mod's main class. Some of these functions can be removed but it's fine if you leave them.

Make a variable that holds your mod id. This is how the forge mod loader will recognize your mod. It's generally based on your mod's name, unique and all lowercase with no special characters. You will use this often, don't forget it. It is also very important to change the value in the `@Mod` annotation at the top of the class to reference your mod id. I took out some of the unnecessary methods from this base class just to clean it up a bit. Here's what it looks like now:

```java
// imports up here // 

@Mod(FirstModMain.MOD_ID)
public class FirstModMain {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "firstmod";

    public FirstModMain() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        
    }
}
```   

Open `src/main/resources/META-INF/mods.toml` It has a bunch of key value pairs that mostly set the information shown on the mods list in game. The only one you have to change is the modId (to whatever you had in your main class). You must keep the modLoader and loaderVersion the same but the fields lower down like display name can be whatever you want, they'll be displayed in the mods list in game. You should also choose a license, go to https://choosealicense.com for more information. 

```toml
modLoader="javafml"
loaderVersion="[41,)"

license="ARR"

[[mods]]

modId="firstmod"

# ... more fields down here
```

The `build.gradle` file tells it what dependancies to download (like Minecraft and Forge). Set the group to whatever you named your package (and click the elephant icon in intellij to update these settings).

```java
group = "ca.lukegrahamlandry.firstmod"
```    

Close intellij, open the terminal, navigate to your mod folder and run the command below (on windows use CMD and you don't need the ./ prefix). It will take a while to run.

```
cd /path/to/mod/folder
./gradlew genIntellijRuns
```

## Run the game

You can open intellij again and run the game by clicking the little green play button in the top right. If you have any problems with that you can also run it with the command below.

```
./gradlew runClient
```

## Info Files

In the top level of your mod folder you'll find a few extra files about forge. I suggest taking out `changelog.txt` and `credits.txt`. You should replace `license.txt` with a license that has information about how people are allowed to use your code (learn more about license options at [choosealicense.com](https://choosealicense.com/)). Finally replace `readme.txt` with `README.md` so you can use [markdown](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet) and GitHub will render it properly. This file should contain information about your mod's features, supported versions and perhaps a link to your CurseForge page once you're ready to release your mod. 

## Alternative Setup

- If you are using an Apple Silicon (m1) computer, read the [Apple Silicon tutorial](/m1)
- If you have an existing 1.18, 1.17, or 1.16 modding environment that you would like to update to 1.19, follow [my updating tutorial](updating).

## Later versions

> TODO: update this page with the right version numbers for 1.19.2. I think the rest of the tutorials are already correct. 

This tutorial is for 1.19.2, NOT 1.19.3 or 1.19.4. Mojang changed how they deal with breaking changes in minor versions so some stuff will be different. I'll probably update it at some point but for now see these resources for an overview of changes.

- 1.19.3: https://gist.github.com/ChampionAsh5357/c21724bafbc630da2ed8899fe0c1d226
- 1.19.4: https://gist.github.com/ChampionAsh5357/163a75e87599d19ee6b4b879821953e8

# Basic Items

In this tutorial we will make a simple item with a name and a texture. We will also make a new creative tab to put items in.

## Concepts 

Each type of item in Minecraft is represented by an instance of the `Item` class. This means that variables on your item class are shared by that type of item in general, not specific to each individual item in your inventory. Each stack of items can store unique data (like its durability) on the `ItemStack` (more later).

Many things in Minecraft (like items, blocks, biomes, etc) must be registered to let the game know that they exist. There are two ways to do this. You could use an object holder that starts out as a null reference and manually register it on the correct registry event (we'll talk about events later). Alternatively, you can use a deferred register to tell the game to automatically create the item at the right time. A deferred register acts as a layer of abstraction over the basic registry system. You just set it up, tell it about your items (or blocks or whatever), and Forge will automatically register them for you at the right time. If you're interested in the details, read [the registry system tutorial](/concepts#registries).

## New Item

Make a new package (in `src/main/java/com/name/modname`, same place as your main class) called `init` and in that make a new class class called `ItemInit`. This is where we will register all our items. I recommend doing this with deferred registers. First, we have to get the item register so we can tell the game about our items.

```java
public static final DeferredRegister<Item> ITEMS = 
            DeferredRegister.create(ForgeRegistries.ITEMS, FirstModMain.MOD_ID);
``` 

Make sure you import all the classes you need. In intellij unimported classes will be written in red and you can press option enter on them to import them. Very important to import the version of `Item` from `net.minecraft.world.item`.

Then you can register your first item. It's going to be both static and final, the convention is to name it in all uppercase. Call the register function and the first argument is the name which must be all lowercase (this can be used to give it to yourself in game with /give Dev modid:item_name) and the second is a supplier for a new Item which takes in a new Item.Properties. Later, if you want to access the item from your code you can do ItemInit.ITEM_NAME.get()

```java
public static final RegistryObject<Item> SMILE = ITEMS.register("smile",
            () -> new Item(new Item.Properties()));
```

The `Item.Properties` parameter sets certain data about like item such as durability, `stacksTo` (max stack size), `fireResistant` (survives lava like netherite equipment), if it works as a food. We will do more with this in later.

## Creative Tab

If you want to make a new tab in the creative menu for your item to show up in, you can make an inner class that extends `CreativeModeTab`. Let your IDE autofill the default constructor and then override the method called `makeIcon` which returns an `ItemStack` to use as the icon in the GUI. You can reference you're own item here (with a `.get()` on the end) or a vanilla item (ex. `Items.DIAMOND_SWORD`, with no `.get()`).

```java
public static class ModCreativeTab extends CreativeModeTab {
    private ModCreativeTab(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(SMILE.get());
    }
}
```   

Then (still in the inner class), you can make a static instance of this class to actually use. The index is just its place in the list of item groups (so the current length of the list) and the name can be used in the lang file to set the text displayed when the logo is hovered over (all lowercase).

```java
public static final ModCreativeTab instance = new ModCreativeTab(CreativeModeTab.TABS.length, "firstmod");
``` 

Then you can update the Item.Properties used when you create your item to reference your group. If you want it to show up in a Vanilla creative tab, just use something like CreativeModeTab.TAB_FOOD (your IDE should let you auto fill the others)

```java
new Item.Properties().tab(ModCreativeTab.instance)
```

> NOTE: this replaces **only** the Item.Properties passed into the item constructor above. All you're changing is calling that extra `tab` method. 

## Main Class

In the constructor of your main class call the register method of your DeferredRegister. This tells the game about any items you make. So that constructor should look something like this.

```java
public FirstMod() {
    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    modEventBus.addListener(this::setup);
    ItemInit.ITEMS.register(modEventBus);
    
    MinecraftForge.EVENT_BUS.register(this);
}
```   

## Assets

In your project folder go to `src/main/resources` and make a new folder called assets, in that make one called your modid and in that ones called lang, models, and textures. In the textures folder make a folder called items and in that put a png image to use for your item in game. In the models folder make a folder called item.

In the lang folder make a new file called en_us.json This is where we tell it how to display the names for our item and item group in game. Make sure to change firstmod to your modid and smile to your item name.

```json
{
    "itemGroup.firstmod": "First Mod Items",

    "item.firstmod.smile": "Smiley Face"
} 
```  

Then in `models/item` make a file called item_name.json (replace item_name with  whatever string you passed into the Item constructer). In the layer0 item, make sure to change firstmod to your modid and smile to the name of the image file you want to use (do not include the .png extension).

```json
{
    "parent": "item/generated",
    "textures": {
        "layer0": "firstmod:items/smile"
    }
} 
```

Your file structure should look like this:

```
src/main
    - resources/assets/modid/
        - lang/
            - en_us.json
        - models/
            - item
                - item_name.json
        - textures/
            - items/
                - item_name.png
    - java/com/name/modname/
        - ModNameMain.java
        - init/
            - ItemInit.java
```

### Data Generators 

If your mod has a lot of items that just use their own basic texture, it can be tedious (and error prone) to repeatedly copy the model json file, just changing a single line each time. Luckily, Minecraft provides a way to generate these files from code. This will be covered in detail in a future tutorial. Join [the discord server](/discord) to be notified when it is released. 

## Run the game

Now run the game. It will take a few minutes to load but eventually you should be able to see that your item shows up in your creative tab.

![example of creative tab in game](/img/creative-tab.png)

## Later versions

This tutorial is for 1.19.2, NOT 1.19.3 or 1.19.4. Mojang changed how they deal with breaking changes in minor versions so some stuff will be different. I'll probably update it at some point but for now see these resources for an overview of changes.

- 1.19.3: https://gist.github.com/ChampionAsh5357/c21724bafbc630da2ed8899fe0c1d226
- 1.19.4: https://gist.github.com/ChampionAsh5357/163a75e87599d19ee6b4b879821953e8

# Advanced Items

A tutorial on making items with unique custom behaviours. We will make a food that gives a potion effect, a furnace fuel and an item that teleports you forward when right clicked.

## Food

In your ItemInit class copy the code for a basic item and change the names.

```java
public static final RegistryObject<Item> FRUIT = ITEMS.register("fruit",
            () -> new Item(new Item.Properties().tab(ModCreativeTab.instance)));
```

Then on the Item.Properties, call the food function. This takes in a Food created with a Food.Builder. The Food.Builder has a variety of functions that let you set different properties of the food. For example, nutrition lets you set how much hunger it restores. When you're done call build to create the Food object.

```java
new Item.Properties().tab(ModCreativeTab.instance)
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(2).build())
```

To make your food grant a potion effect when eaten, call the `effect` function of your `FoodProperties.Builder` (before you call `build()`). This takes a supplier for an `MobEffectInstance` which takes the effect you want to give, the duration (in ticks so 20 is one second), and the amplifier (0 is level I). The effect method also takes the likelihood your effect will be applied (1.0F is always and 0F is never). So this code will have a 50% chance to give fire resistance I for 10 seconds.

```java
.effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 0.5F)
```

You can also call `.alwaysEat()` if you want to be able to eat the food even when your hunger is full (like golden apples).

## Fuel

Again, start by copying the basic item code and change the names. This time change the supplier from an Item to a new class you will create soon.

```java
public static final RegistryObject<Item> FUEL = ITEMS.register("fuel",
            () -> new FuelItem(new Item.Properties().tab(ModCreativeTab.instance)));
```

Make a new package called items and create the FuelItem class you referenced when making the item. It should extend Item and have a constructor that takes both an Item.Properties to pass on to the super constructor and an int to use as burn time. Don't forget to make a field to save that burn time.

```java
public class FuelItem extends Item {
    private final int burnTicks;
    public FuelItem(Properties properties, int burnTimeInTicks) {
        super(properties);
        this.burnTicks = burnTimeInTicks;
    }
}
``` 

Then override the `getBurnTime` method. As arguments, this takes the `ItemStack` being used as fuel and the recipe type (SMELTING, SMOKING or SMITHING) both of which we will ignore. It returns the burn time in ticks. It will return the int we saved from the constructor. 

```java
@Override
public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
    return this.burnTicks;
}
```   

Now go back to ItemInit, import this class and update the constructor when you create your fuel item to include the burn time. Items generally take 200 ticks to smelt so 3200 should cook 16 items (twice as much as coal).

```java
new FuelItem(<...>, 3200)
```

## Right click behaviour

Start the same way as for a fuel. In ItemInit, copy paste basic item, rename and change the class. Make a new class in your items package that extends item and uses the default constructer.

```java
// ItemInit.java

public static final RegistryObject<Item> TELEPORT_STAFF = ITEMS.register("teleport_staff",
            () -> new TeleportStaff(new Item.Properties().tab(ModCreativeTab.instance)));

// items/TeleportStaff.java

public class TeleportStaff extends Item {
    public TeleportStaff(Properties properties) {
        super(properties);
    }
}
```   

To do something when it's right clicked, override use. It takes in the world which lets you effect blocks and stuff, the player that used it and whether it was main or offhand.

```java
@Override
public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
    return super.use(world, player, hand);
}
```

So in that method we can use a function built into the item class to do a raycast in the direction the player is looking and save the first thing it hits in a variable. Then we can set the player's position to that position. That will teleport the player forward.

```java
BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
BlockPos lookPos = ray.getBlockPos();
player.setPos(lookPos.getX(), lookPos.getY(), lookPos.getZ());
```

If you tried it now you'd notice that when you try to teleport straight down it you get your feet stuck in the block. This is because we told it to literally set your feet to the same place as the block. We can update the line creating lookPos to offset the position based on the direction you're looking at the block.

```java
BlockPos lookPos = ray.getBlockPos().relative(ray.getDirection());
```

Currently the range of the teleport is just the players mining range. To fix that, we can make our own version of this getPlayerPOVHitResult method. If you command click (MacOS Intellij, idk what it is for windows) on the method you can jump to its declaration in the Item class. Its a bunch of confusing math but we can just copy it into our class and try to figure it out. 

You can see its getting the position of the player's eyes, then calculating the direction you're looking and multiplying that by this a float called d0. So if you change that, you change the length of the vector and thus the range. If there's no block close in front of you, you'll just teleport the full distance. Let's just name our new function something different so we can be sure we aren't messing up any other behaviors. Don't forget to switch the use method to be using our new `rayTrace`.

```java
protected static BlockRayTraceResult rayTrace(Level world, Player player, ClipContext.Fluid fluidMode) {
    double range = 15;

    float f = player.getXRot();
    float f1 = player.getYRot();
    Vector3d vector3d = player.getEyePosition(1.0F);
    float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
    float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
    float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
    float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
    float f6 = f3 * f4;
    float f7 = f2 * f4;
    Vector3d vector3d1 = vector3d.add((double)f6 * range, (double)f5 * range, (double)f7 * range);
    return world.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, fluidMode, player));
}
```

If you want to limit how often the item can be used you can add a cool down. The second argument is the time they have to wait before using it again in ticks (1/20 of a second). You can also make teleporting reset how far you've fallen so you can use the item to escape fall damage.

```java
    player.getCooldowns().addCooldown(this, 60);
    
    player.fallDistance = 0F;
```

You can also play a sound. This will play the enderman's teleport sound that the player's position. The last two arguments are volume and pitch. 

```java
world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
```

If we want to give the players some help understanding our mod, we can add a tool tip when they hover over the item in their inventory. So override appendHoverText and add to the tooltip. The Component class lets you create a string that Minecraft can render.

```java
@Override
public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    tooltip.add(Component.literal("teleports you where you're looking"));

    super.appendHoverText(stack, worldIn, tooltip, flagIn);
}
```

If you want less clutter for the player, you can only show this when they're holding shift. So make a new package called util and a class called KeyboardHelper. Everything here will be static, we can get the game window so we can get user input. Then make some functions to check if they're holding shift, space, or control respectively. For shift and control we are checking both the left and right one. Don't forget to import all these classes. 

You must be very careful that you only call these methods in client sided code! You can't use them when making game logic decisions, it will work in single player but crash servers. You must send information with packets to get keybind input on the server (will be covered in a separate tutorial). [Learn More](/concepts#sides).

```java
public class KeyboardHelper {
    private static final long WINDOW = Minecraft.getInstance().getWindow().getWindow();

    public static boolean isHoldingShift() {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isHoldingControl() {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    public static boolean isHoldingSpace() {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_SPACE);
    }
}
```

Then you can change your appendHoverText method to check if the player is holding shift.

```java
if (KeyboardHelper.isHoldingShift()){
    tooltip.add(Component.literal("teleports you where you're looking"));
}
``` 

If you want it to use durability you can get the item stack being used, increase how damaged it is (because its stored as how much damage has been taken not how much durability remains). Note that setting the damage doesn't make it break when it hits 0. So we have to check if its zero and if so make the stack empty.

```java
ItemStack stack = player.getItemInHand(hand);
stack.setDamageValue(stack.getDamageValue() + 1);
if (stack.getDamageValue() >= stack.getMaxDamage()) stack.setCount(0);
``` 

Then you can go back to ItemInit. Don't forget to import your special item class. If you did the durability thing you have to set the durability to something in the `Item.Properties`. Giving it durability automatically makes the max stack size one (normally set by the `stacksTo` method on the properties builder).

```java
new Item.Properties().tab(ModCreativeTab.instance).durability(50)
``` 

## Other Methods

The `Item` class has many other methods you can override for interesting behaviour. Here's just a few of them

- `useOn`: called when right clicked targeting a block
- `interactLivingEntity`: called when right clicked targeting an entity
- `hurtEnemy`: called when the player hits an entity while holding the item
- `onCraftedBy`: called when taken out of a crafting slot. 
- `inventoryTick`: called once every tick (20 times per second) while it is in someone's inventory. 
- `isValidRepairItem`: can the second item be used to repair the first item in an anvil

## Assets

You can setup the textures, models and lang file exactly the same as for basic items. Just remember to change the names of the file and any time you use the name of the item.

## Run the game

If you run the game the items show up in the creative tab and have textures. You can eat the food and sometimes get fire resistance. My fuel coal can be used in a furnace and smelts 16 items. Finally the staff will teleport you and lose durability.

## Related Tutorials 

- [Tools & Armor](tools-armor): allow your item to be used as a tool or worn as armor
- [Arrows](arrows): allow bows to use your item as ammo
- Custom Bow tutorial coming soon. Join [the discord server](/discord) to be notified when it is released. 

## Practice

- Make an item that replaces any block you right click with dirt
    - hint: `world.setBlock(pos, Blocks.VANILLA_NAME.defaultBlockState());`
- Make an item that gives poison to any entity you right click
    - hint: `entity.addEffect(new MobEffectInstance(MobEffects.VANILLA_NAME, duration, amplifier));`
- Make an item that propels the player in the direction they're looking
    - hint: `player.setDeltaMovement(x, y, z);`
- Make an item that places a torch automatically while in the inventory of a player in darkness
    - hint: `Math.max(world.getBrightness(LightType.BLOCK, pos), world.getBrightness(LightType.SKY, pos)) > 7`

## Later versions

This tutorial is for 1.19.2, NOT 1.19.3 or 1.19.4. Mojang changed how they deal with breaking changes in minor versions so some stuff will be different. I'll probably update it at some point but for now see these resources for an overview of changes.

- 1.19.3: https://gist.github.com/ChampionAsh5357/c21724bafbc630da2ed8899fe0c1d226
- 1.19.4: https://gist.github.com/ChampionAsh5357/163a75e87599d19ee6b4b879821953e8

# Basic Blocks

In this tutorial we will register a simple block with a texture and a loot table. It will be a similar process to basic items.

## Concepts 

Blocks are very similar to items. They must be registered so the game knows about them. Each type block is an instance of the `Block` class (not each physical block in the world). Basic traits of your block can be set with a properties object but more complex behavior will require your own class that extends `Block`. 

## New Block

In your init package make a new class called BlockInit. The code here is mostly the same as in ItemInit. Just make sure to say Block instead of Item everywhere. The string you pass the register function is the block's registry name which will be used for naming asset files later.

```java
public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FirstModMain.MOD_ID);

    public static final RegistryObject<Block> SMILE_BLOCK = BLOCKS.register("smile_block",
            () -> new Block());
}
```

The Block constructor in the supplier takes a `Block.Properties` object made by calling the `Block.Properties.of`  method. This takes a Material which sets a few characteristics about your block like whether its flammable, how it reacts to pistons, default sounds, whether it blocks player motion, and what tools can mine it by default. Vanilla has many options to chose from, just let your IDE autocomplete from `Material`.

Then your `Properties` object has many other methods you can call to set different traits, just like we did with items. `strength` lets you pass in how long it takes to break and how resistant to explosions it is. You have to call `requiresCorrectToolForDrops` if it should be like stone and drop nothing without the tool (which tool is set by tags in the next section).  If you want it to be a light source you can use lightLevel with a lambda expression that maps a blockstate to a value from 1 to 16. There are many more like `friction` (used by ice), `speedFactor` (used by soul sand) and `jumpFactor` (used by honey). So that supplier might looks something like this:

```java
() -> new Block(Block.Properties.of(Material.STONE).strength(4f, 1200f).requiresCorrectToolForDrops().lightLevel((state) -> 15))
```

You can also use `Block.Properties.copy(ANOTHER_BLOCK)` to avoid writing things out repeatedly. All vanilla blocks can be accessed with `Blocks.INSERT_NAME_HERE` so you can copy properties from one of them if you feel like it. Or avoid redundancy by referencing `YOUR_BLOCK.get()`

## Tags 

To make your block require a certain type and level of tool, you must add it to a tag. Tags are how minecraft catagories blocks/items behavior in a way accessible to datapacks. For example, there is a tag for `sapplings` and one for `crops`. 

Vanilla adds tags for each tool type: `mineable/pickaxe`, `mineable/axe`, `mineable/shovel`, `mineable/hoe` and for each harvest level: `needs_stone_tool`, `needs_iron_tool`, `needs_diamond_tool`. You can add your block to some of these tags to make certain tools mine it quickly and respect the `requiresCorrectToolForDrops()` call on the `Block.Properties`.  

Create the folders `src/main/resources/data/minecraft/tags/blocks`, in that directory create files for each tag you want to add your block to. They should be named `tag_name.json`. If the tag name has a `/` in it, that part before it is the name of the folder (ie, the `shovel.json` file goes in the `mineable` folder). These files will contain a json object with the `replace` key set to false (so you don't remove vanilla behavior) and the `values` key set to a list of registry names for the blocks you want to behave as described by the tag. 

For example, I'll make the file `needs_iron_tool.json`. The `mineable` folder will contain the file `pickaxe.json`. Both will contain the following text: 

```json
{
    "replace": false,
    "values": [
        "firstmod:smile_block"
    ]
}
```

This will make my block mine quickly with a pickaxe and only drop when using a pickaxe of iron or higher. Remember you must also call `requiresCorrectToolForDrops()` on your block properties object for this to work. 

More info about this system: https://gist.github.com/gigaherz/691f528a61f631af90c9426c076a298a

## Block Item

You need a BlockItem to place your block. You can register it manually like your other items but that's tedious so lets make it automatic. We will use the events system to run some code when the game registers all the other items. You can just copy this code for now or read the [events tutorial](/concepts#events) for a more detailed explanation of how events work.

At the top of your class add this line to allow it to subscribe to events.

```java
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    // ...previous code here...
}
```

Then make a static function with the SubscribeEvent annotation. Its argument will be the RegisterEvent so it fires when things are supposed to be registered. Each registry is processed severalty so we make sure that this event fired for items. 

In that function get the Item registry and loop through all the blocks. For each block, we make an Item.Properties that puts it in our creative tab. Then we make a BlockItem with those properties to place our block. We register the BlockItem with the same registry name as the block.

```java
@SubscribeEvent
public static void onRegisterItems(final RegisterEvent event) {
    if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
        BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
            Block block = blockRegistryObject.get();
            Item.Properties properties = new Item.Properties().tab(ItemInit.ModCreativeTab.instance);
            Supplier<Item> blockItemFactory = () -> new BlockItem(block, properties);
            event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
        });
    }
}
```

Instead of doing it this way you could manually register a BlockItem for each of your blocks in your ItemInit class but that's really tedious so I'd advise doing it this way. There might be some blocks you make later where you'll what a unique block item. All you'd have to do is add an if statement to check for that block and create the new BlockItem differently. 

## Main Class

In the constructor of your main class add this line to call the register method of your DeferredRegister (same as for items).

```java
BlockInit.BLOCKS.register(modEventBus);
```

## Assets

In your project folder go to `src/main/resources/assets/mod_id`. Make a new folder called blockstates and in your models folder make a new folder called block. In textures make a new folder called blocks and put the png image you want to use for your block's texture. Then go out to `src/main/resources/data/mod_id` and make a folder called loot_tables and in that make a folder called blocks.

In blockstates make a file called block_name.json (replace block_name with whatever string you passed in as your registry name). Since this is a simple block, we just need one varient that points to a model. Make sure you change firstmod to your mod id and smile_block to the registry name of your block.

```json
{
    "variants": {
        "": {
            "model": "firstmod:block/smile_block"
        }
    }
} 
```

This is the simplest possible blockstate definition. More complex blocks can have different sides (like grass), rotation (like furnaces and chests), age (like crops or ice) and you can define your own custom properties. The possibilities are endless and will likely be discussed more in depth in another tutorial. 

In models/block make a file called block_name.json. This is the file you'd change if you want different sides of the block to look different (like a grass block) but since I don't, I'll just point to one texture (make sure to change smile_block to the name of your image file).

```json
{
    "parent": "block/cube_all",
    "textures": {
        "all": "firstmod:blocks/smile_block"
    }
} 
```

In models/item make a block_name.json file that just parents off your block model

```json
{
    "parent": "firstmod:block/smile_block"
} 
```

In lang/en_us.json add a line that gives your block a name. Remember to change the mod id and block registry name.

```json
"block.firstmod.smile_block": "Smiley Block"
```

In loot_tables/blocks make block_name.json. This sets what drops when you break the block. This is a simple loot table that just drops the block's item but it could be anything.

```json
{
    "type": "minecraft:block",
    "pools": [
    {
        "rolls": 1.0,
        "entries": [
        {
            "type": "minecraft:item",
            "name": "firstmod:smile_block"
        }
        ]
    }
    ]
} 
```

Loot tables are also how drops from entities are determined and what you find in chests. They deserve a tutorial of their own but in the mean time, take a look at [vanilla's loot tables](https://github.com/InventivetalentDev/minecraft-assets/tree/1.16.5/data/minecraft/loot_tables/blocks) and [the wiki](https://minecraft.fandom.com/wiki/Loot_table) for inspiration.

You should end up with a file structure like this:

```
- src/main/resources/
    - assets/modid/
        - blockstates/  
            - block_name.json  
        - models/block/  
            - block_name.json  
        - textures/blocks/
            - block_name.png  
    - data/modid/  
        - loot_tables/blocks  
            - block_name.json  
```

### Data Generators 

If your mod has a lot of blocks that just use their own basic texture, it can be tedious (and error prone) to repeatedly copy the model/blockstate/loot_table json file, just changing a single line each time. Luckily, Minecraft provides a way to generate these files from code. This will be covered in detail in a future tutorial. Join [the discord server](/discord) to be notified when it is released. 

## Run the game

If we run the game, we can see that the block shows up in our creative tab. We can place it and break it with an iron pickaxe

## Later versions

This tutorial is for 1.19.2, NOT 1.19.3 or 1.19.4. Mojang changed how they deal with breaking changes in minor versions so some stuff will be different. I'll probably update it at some point but for now see these resources for an overview of changes.

- 1.19.3: https://gist.github.com/ChampionAsh5357/c21724bafbc630da2ed8899fe0c1d226
- 1.19.4: https://gist.github.com/ChampionAsh5357/163a75e87599d19ee6b4b879821953e8

# Advanced Blocks

A tutorial on making a block with custom behaviour. It will react to being right clicked, explosions, random ticks and even act as soil for crops!

## Init

Very similar to how we did [advanced items](advanced-items), let's make a block that has some unique behaviour by creating our own version of the block class. Start with the same code for a [basic block](basic-blocks) but reference a new class. 

```java
public static final RegistryObject<Block> SAD_BLOCK = BLOCKS.register("sad_block",
            () -> new SadBlock(Block.Properties.copy(Blocks.DIRT)));
```

Note that I'm copying the `Block.Properties` from the vanilla dirt block. If you were using one of your own blocks you'd have to call `.get()` to access the actual block itself because deferred registers are like that. 

## Block Class

Now create the class for your block. It will extend block and use the default constructor. 

```java
public class SadBlock extends Block {
    public SadBlock(Block.Properties properties) {
        super(properties);
    }
}
```

Just like with the `Item` class, `Block` has many fun methods to override to get interesting behaviour. 

### Right Click Behaviour

The `use` method defines what happens when a player right clicks the block. Note that is is called twice. Once for the off-hand and once for the main hand. You can check which hand it's for by comparing the `hand` argument that's passed in to `InteractionHand.MAIN_HAND` and `InteractionHand.*OFF_HAND*`*.*

You can do anything you want here but I want my block to be explosive so I'll start by getting the `ItemStack` in the player's hand and checking if it is gun powder. The world's explode method requires the entity responsible for the explosion (for death messages, etc. Can be `null`), the position, the size to make the explosion (tnt is 4), a boolean of whether it should set fires, and the explosion mode (`NONE` does not damage terrain, `BREAK` drops items from the blocks it breaks while `DESTROY` deletes them).  I'll also shrink the item stack by one so it consumes the gun powder. 

```java
@Override
public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    ItemStack held = player.getItemInHand(hand);

    if (!world.isClientSide() && held.getItem() == Items.GUNPOWDER){
        world.explode(player, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true, Explosion.Mode.DESTROY);
        held.shrink(1);
    return InteractionResult.CONSUME;
    }

    return super.use(state, world, pos, player, hand, hit);
}
```

Note that explosions won't work if the block's blast resistance is too high (because the explosion starts from inside the block)

**Server vs Client**

You might also notice that I have an extra condition before doing anything: `!world.isClient()`. This checks that we are running on the server side. Minecraft's code is split into two sections. These are easiest to think about when you imagine playing on a server with other people. There is one computer somewhere that thinks about all the logic for the game (the server) and tells the other players what their computer should render (the clients). Some things happen only on the server (like saving world data), some things happen only on the client (like rendering entities), but most things happen on both (like this `Block.use` method). Learn more: [here](/concepts#sides).

This means that you have to manually check which side you're on if you plan to do anything that might behave differently across the sides. Most behaviour you want to run only on the server side and let Minecraft automatically sync to the client. It can get very weird if you accidentally only do something on the client. Important to note that fields set on your objects can have different values on the client and the server. This can lead to some very confusing behaviour if you're not careful. Later when you do more complicated things and need you sync data between the client and the server, you will have to use packets. 

**Interaction Results**

There are four different `InteractionResult`s you can return from this method (also used in several others involving players interacting with things).

- `SUCCESS`: Use this when your item/block/entity has met all the requirements for the interaction, and you have completed everything that needs to be done. This will prevent any further action from being taken with the interaction.
- `CONSUME`: You can use this when you meet the same conditions as SUCCESS, but additionally are consuming an item/block as part of the interaction. This InteractionResult isn't explicitly checked as of 1.15.2, and so using this is mostly optional. 
- `PASS`: Use this when you do not meet any of the requirements for the interaction (not holding the right item, etc.). This signals for the game to attempt to interact with the other hand, and also try to use the item's interaction methods and other similar methods.
- `FAIL`: Use this when you have met the minimum requirements for the interaction (holding the right block, etc.), but fail to meet further criteria. This should be used when you want to indicate that the player intended to interact with your object, but failed to meet further criteria. This will prevent any further action from being taken with the interaction.

### Being Exploded

You can use the `wasExploded` method to react to the block being broken by an explosion. I'll just create an explosion just like before so they can be used in chain reactions like TNT. 

```java
@Override
public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
        world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true, Explosion.BlockInteraction.DESTROY);
    super.wasExploded(world, pos, explosion);
}
```

### Allowing Plants

You can override `canSustainPlant` to allow your block to act as soil for specific plants, simply return true if the plant passed in should be allowed to grow. I'll get the `Block` from the `Iplantable` passed in and allow it to grow if it's a cactus. Otherwise, I'll use the super method to let the vanilla behaviour decide. Which in this case will always be false, preventing any other plants from growing. 

```java
@Override
public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        Block plant = plantable.getPlant(world, pos.relative(facing)).getBlock();

    if (plant == Blocks.CACTUS){
            return true;
    } else {
        return super.canSustainPlant(state, world, pos, facing, plantable);
    }
}
```

### Random Ticks 

Sometimes a block needs to do something occasionally (for example crops need to grow, grass needs to spread). Over time each block in the world randomly have its tick method called. The median time between random ticks is 47 seconds and it's affected by the random tick speed game rule. Learn more on [the wiki](https://minecraft.fandom.com/wiki/Tick#Random_tick).

To allow your block to receive random ticks, you must either call `randomTicks()` on the `Block.Properties` used to create your block **or** override `isRandomlyTicking` in your block class to return true.

Then you can override `randomTick` to do something interesting. I'll make mine check if the block above is air and create a cactus. 

```java
@Override
public boolean isRandomlyTicking(BlockState state) {
    return true;
}

@Override
public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
    BlockState above = world.getBlockState(pos.above());
    if (above.isAir()){
        world.setBlockAndUpdate(pos.above(), Blocks.CACTUS.defaultBlockState());
    }
}
```

You can use the command `/gamerule randomTickSpeed 9999` to increase the tick rate while testing.

### Other Methods 

The `Block` class has lots more methods to play with. Here's a description of a few of them:

- `playerDestroy` (used by beehives to release angry bees when you break them)
- `onRemove`: called when the block is destroyed for any reason 
- `handleRain`: 1/16 chance to be called each tick while its raining (used by cauldrons to fill with water)
- `fallOn`: called when an entity falls on the block. Deals the fall damage by calling `Entity.causeFallDamage(distance, damageMultiplier)`. (used by farm land to break the crop)
- `onPlace`
- `getExpDrop` returns the number of experience points to give when broken (like some vanilla ores)

### Rotation

To make the block rotatable like a furnace, you have to add a property to the block state so you can tell the model file which sides to render where. It will hold a value of `NORTH`, `SOUTH`, `EAST`, or `WEST` which represents the direction it is facing. First create the property as a static field in your block class. 

```java
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
```

Then set a default value of the state in the constructor. We will replace this with the correct direction when a player places the block but it would probably randomly crash sometimes if you don't do this. 

```java
this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
```

You also need to tell the block about the properties it is allowed to use by adding this method:

```java
@Override
protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
}
```

You'll have to override `getStateForPlacement` to have it face the player when placed. This snippet is taken from the vanilla furnace. If you want to to face forwards from the player instead of towards them, take out the `.getOpposite()`.

```java
@Override
public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
}
```

Now you'll need to create a block state json file. This must have the same name as your block's registry name.  `/src/main/resources/assets/modid/blockstates/block_name.json`:

```json
{
    "variants": {
        "facing=east": {
            "model": "firstmod:block/sad_block",
            "y": 90
        },
        "facing=north": {
            "model": "firstmod:block/sad_block"
        },
        "facing=south": {
            "model": "firstmod:block/sad_block",
            "y": 180
        },
        "facing=west": {
            "model": "firstmod:block/sad_block",
            "y": 270
        }
    }
}
```

You can also do more complicated things in your block state like switching out the model file and using multiple block state properties. For example the furnace uses `facing=west,lit=false` and `facing=west,lit=true` to switch to a version of the model where the face is lit up when something is cooking (lit being a `BooleanProperty` instead of a `DirectionProperty`).

The `y` number tells it how much to rotate the model around the up-down axis. Things like dispensers that need to rotate around another axis use `x` as well (to do that you'd have to make some changes above: use `DirectionalBlock.*FACING*`instead of`HorizontalBlock.FACING` and `getNearestLookingDirection()` instead of `getHorizontalDirection` in your `getStateForPlacement` method).

Then you'll need to make the model file (in `assets/models/block/` as before). You can directly set the north, south, east, west, up and down textures (as they should be when your block is facing north, meaning 0 y rotation from the block state) but there are several templates you can use as parents to save some typing. You can look at the [vanilla assets](https://github.com/InventivetalentDev/minecraft-assets/tree/1.16.5/assets/minecraft/models/block) to see some other options. I'm using `orientable` which lets you set the sides and bottom all the same quickly. Note that you can also reference vanilla textures here. 

It can be named anything because a single block state definition might need multiple models. Its name is the string you referenced in the block state file (so `sad_block.json` for me).

```json
{
    "parent": "minecraft:block/orientable",
    "textures": {
        "top": "minecraft:block/sand",
        "front": "firstmod:blocks/sad_block",
        "side": "minecraft:block/tnt_side"
    }
}
```

> If you you're making a block without rotation, you can just make a generic block state and model file similar to the basic block.  Don't forget to update your lang file and make item model file for the block just like we did for the basic one. You should also make a loot table so your block drops something interesting. 

## Run the Game

You should now have a fully functional block with custom functionality! 
![example of advanced block in game](/img/sad-block.png)

## Related Tutorials

- [Tile Entities](tile-entities): Allows your block to save data and react to every tick. I made mine kill nearby mobs!
- You can also make your block a point of interest for a custom villager profession. Tutorial for this coming soon! Join [the discord server](/discord) to be notified when they are released. 

## Practice

- Make a soil that grows crops very quickly while it's raining
  - hint: look at how the vanilla `BoneMealItem` uses `BonemealableBlock`
- Make a directional block that rotates when you right click it

## Later versions

This tutorial is for 1.19.2, NOT 1.19.3 or 1.19.4. Mojang changed how they deal with breaking changes in minor versions so some stuff will be different. I'll probably update it at some point but for now see these resources for an overview of changes.

- 1.19.3: https://gist.github.com/ChampionAsh5357/c21724bafbc630da2ed8899fe0c1d226
- 1.19.4: https://gist.github.com/ChampionAsh5357/163a75e87599d19ee6b4b879821953e8

# Tools and Armor

In this tutorial we make a simple set of tools and armor. We also make an armor piece that reacts to ticks and being attacked.

## Tools

Tools are simply items that use special classes instead of the basic `Item`.

### Material

Start by defining the base stats for your tier of tools. The mining level (0 is wood, 4 is netherite), durability, mining speed, damage, [enchantability](https://minecraft.fandom.com/wiki/Enchanting_mechanics#How_enchantments_are_chosen), and an `Ingredient` which defines which items may be used to repair your tools in an anvil.

You must do this in a class that implements `Tier`. The simplest way to do this is to just copy vanilla's `Tiers` enum and just redefine the tiers. Put this class in your `util` package. You can change the values used to initialize your tier to suit your liking and make multiple by separating them with commas. 

```java
public enum ModItemTier implements Tier {
    PINK(3, 3000, 10.0F, 5.0F, 5, () -> {
        return Ingredient.of(ItemInit.SMILE.get());
    }),
    EXAMPLE(1, 1, 1.0F, 1.0F, 1, () -> {
        return Ingredient.of(Items.STICK);
    });

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModItemTier(int level, int durability, float miningSpeed, float damage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.level = level;
        this.uses = durability;
        this.speed = miningSpeed;
        this.damage = damage;
        this.enchantmentValue = enchantability;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
```

In 1.17, the system for checking if a block can be broken by a tier of tool moved to tags. Creating a tier higher than diamond and having blocks that can only be broken by it, is more complicated now. You must tell forge about it with the `TierSortingRegistry` class. More detailed explanation coming soon! Join [the discord server](/discord) to be notified when it is released. 

### Init

Now you have to register your tools in `ItemInit` just like any other item. Each type of tool has its own class (`SwordItem`, `PickaxeItem`, etc). The item constructor takes a reference to the `Tier` you defined earlier, a damage value to add to the base damage from the tier, an attack speed value which is added to a default of 4 to get the final speed of the item's swings (so should probably be negative, -2 is faster than -1) and finally an `Item.Properties` just like your other items. 

```java
public static final RegistryObject<Item> PINK_SWORD = ITEMS.register("pink_sword",
        () -> new SwordItem(ModItemTier.PINK, 3, -2.4F, new Item.Properties().tab(ModCreativeTab.instance)));

public static final RegistryObject<Item> PINK_PICKAXE = ITEMS.register("pink_pickaxe",
        () -> new PickaxeItem(ModItemTier.PINK,1, -1.0F, new Item.Properties().tab(ModCreativeTab.instance)));

public static final RegistryObject<Item> PINK_AXE = ITEMS.register("pink_axe",
        () -> new AxeItem(ModItemTier.PINK, 6, -3.4F, new Item.Properties().tab(ModCreativeTab.instance)));

public static final RegistryObject<Item> PINK_SHOVEL = ITEMS.register("pink_shovel",
        () -> new ShovelItem(ModItemTier.PINK, 1, -1.0F, new Item.Properties().tab(ModCreativeTab.instance)));

public static final RegistryObject<Item> PINK_HOE = ITEMS.register("pink_hoe",
        () -> new HoeItem(ModItemTier.PINK, 0, -1.0F, new Item.Properties().tab(ModCreativeTab.instance)));
```

### Advanced Tools

Since they are simply items, you can make your own classes that extend the basic tool classes to give them unique behaviour. You can use the same methods discussed in the [advanced items tutorial](/advanced-items). Here are some methods that might be interesting:

- `mineBlock`: called when the player breaks a block with the item. You should make sure to call the super method to reduce durability.
- `isCorrectToolForDrops`
- `getDestroySpeed` (big numbers are faster)
- `hurtEnemy`

### Assets

The assets are the same as any other item (model, texture, lang) except that the model parent should be `item/handheld` instead of `item/generated`. This will make it rotate properly in your hand to look like you're holding a tool. 

## Armor

Similar to tools, a piece armor is simply an item that uses `ArmorItem` instead of the basic `Item`. 

### Material

Start by defining the stats for your armor in an enum that implements `ArmorMaterial`. The code can be copied from vanilla's `ArmorMaterial` enum.

The `name` string you use **must **start with your mod id, then a colon, then anything. The durability number is multiplied by the numbers in the `HEALTH_PER_SLOT` array to get the durability for each piece. `protection` is an array of the protection values of each piece (in the order boots, leggings, chest plate, helmet). A full armor bar is when those numbers add up to 20. It needs an [enchantability](https://minecraft.fandom.com/wiki/Enchanting_mechanics#How_enchantments_are_chosen) just like tools and a `SoundEvent` to play when you equip the item. I'm just using a vanilla sound but later we'll learn how to add a custom one ([join the discord server](/discord) to be notified when the sounds tutorial is released). Then you need a [toughness](https://minecraft.fandom.com/wiki/Armor#Armor_toughness) which increases how much protection it gives against stronger attacks (only used by diamond and netherite in vanilla). Then knockback resistance (only used by netherite, when all pieces add up to 1 that's no knockback) and finally a supplier for a repair ingredient to use in the anvil.  

```java
public enum ModArmorMaterial implements ArmorMaterial {
    PINK(FirstModMain.MOD_ID + ":pink", 20, new int[]{4, 7, 9, 4}, 50, SoundEvents.ARMOR_EQUIP_DIAMOND, 3.0F, 0.1F, () -> { 
        return Ingredient.of(ItemInit.SMILE.get()); 
    });

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModArmorMaterial(String name, int durability, int[] protection, int enchantability, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durability;
        this.slotProtections = protection;
        this.enchantmentValue = enchantability;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    public int getDurabilityForSlot(EquipmentSlot slot) {
        return HEALTH_PER_SLOT[slot.getIndex()] * this.durabilityMultiplier;
    }

    public int getDefenseForSlot(EquipmentSlot slot) {
        return this.slotProtections[slot.getIndex()];
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
```

### Init

Register your armor items like normal using the `ArmorItem` class. It needs a reference to your `ArmorMaterial`, and `EquipmentSlot` (`HEAD`, `CHEST`, `LEGS`, or `FEET`) and an `Item.Properties`. 

```java
public static final RegistryObject<Item> PINK_HELMET = ITEMS.register("pink_helmet",
        () -> new ArmorItem(ModArmorMaterial.PINK, EquipmentSlot.HEAD, new Item.Properties().tab(ModCreativeTab.instance)));

public static final RegistryObject<Item> PINK_CHESTPLATE = ITEMS.register("pink_chestplate",
        () -> new ArmorItem(ModArmorMaterial.PINK, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeTab.instance)));

public static final RegistryObject<Item> PINK_LEGGINGS = ITEMS.register("pink_leggings",
        () -> new ArmorItem(ModArmorMaterial.PINK, EquipmentSlot.LEGS, new Item.Properties().tab(ModCreativeTab.instance)));

public static final RegistryObject<Item> PINK_BOOTS = ITEMS.register("pink_boots",
        () -> new ArmorItem(ModArmorMaterial.PINK, EquipmentSlot.FEET, new Item.Properties().tab(ModCreativeTab.instance)));
```

### Assets

In `/src/main/resources/assets/modid/textures` make a new folder called `models` and in that one called `armor`. Here you will put the texture map for your armor. It's sort of a weird format, they look like this:   
![armor texture template layer one](/img/template_layer_1.png)

![armor texture template layer two](/img/template_layer_2.png)

Use those exact templates because the positioning is important, they should be 512 by 256 (but it's the 1:2 ratio that matters). The first one must be named `name_layer_1.png` and is for the helmet and chest plate. The second must be named `name_layer_2.png` and is leggings and boots. Of course, replace `name` with the string you used in your armor material (without the `modid:` prefix). So for me it's `pink_layer_1.png` and `pink_layer_2.png`. 

The other assets (model json & lang) are the same as for normal items. 
![player wearing custom armor in game](/img/armor.png)

## Advanced Armor

Let's make a fresh piece of armor to experiment with. I'll use the same material as before because I'm lazy but you should make a new one if you want unique stats and appearance. Instead of being a normal `ArmorItem` this will be a new class that extends `ArmorItem`.

```java
public static final RegistryObject<Item> FLAMING_CHESTPLATE = ITEMS.register("flaming_chestplate",
            () -> new FlamingArmorItem(ModArmorMaterial.PINK, EquipmentSlot.CHEST, new Item.Properties().tab(ModCreativeTab.instance)));
```

### Tick

Make a class for your `ArmorItem` and override the `onArmorTick` method. I'll give the wearer 10 seconds of fire resistance (since its done every tick, they'll be immune to fire damage while wearing the armor). It's probably a good idea to make sure you're only doing this on the server side. 

```java
public class FlamingArmorItem extends ArmorItem {
    public FlamingArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide()){
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200));
        }
    }
}
```

If you want to only do something while they're wearing the full set, you can add a condition in your tick method that checks that each piece of armor matches what it should be. 

```java
boolean fullSet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemInit.PINK_HELMET.get() && <CHEST> && <LEGS> && <FEET>;
if (fullSet){
    // do something cool here
}
```

If all the pieces are from your special class keep in mind that this tick method will be called for each piece. You may want to check the `EquipmentSlot` of the item stack that's passed in (by calling `stack.getEquipmentSlot()`) to avoid repeating behaviour depending what you're doing. 

### On Attacked

The `ArmorItem` class doesn't offer a method to override for this but we can use events instead. Events are a way to let forge know that it should call one of your methods when something specific in the game happens. This system is described in more depth in [the events tutorial](/concepts#events).

In your `util` package make an interface called `IDamageHandlingArmor` with a single method called `onDamaged`. This will take the entity being attacked, the armor slot being processed, the damage source (which gives you the type of damage and the attacker if applicable). The default implementation will simply return the same damage amount so nothing will change

```java
public interface IDamageHandlingArmor {
    default float onDamaged(LivingEntity entity, EquipmentSlot slot, DamageSource source, float amount){
        return amount;
    }
}
```

Start by making a package called `events`. Then make a new class called `ArmorHandlers` with the `EventBusSubscriber` annotation. 

```java
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorHandlers {

}
```

Then, in that class make a new method that listens for the `LivingDamageEvent`. This is done by making a public, static, void method with the `@SubscribeEvent` annotation. The parameter type defines which event it will listen for. 

This method will get the attacked entity from the event object and loop through each piece of armor it is wearing. For each piece it will check if the item implements our interface and call the `onDamaged` method if it does. It saves the value returned by that method and sets the damage about on the event. This allows our custom armor to directly effect the damage by changing the return value. 

```java
@SubscribeEvent
public static void armorAttackHandler(LivingDamageEvent event){
    for (ItemStack armor : event.getEntityLiving().getArmorSlots()){
        if (armor.getItem() instanceof IDamageHandlingArmor){
            float newDamage = ((IDamageHandlingArmor)armor.getItem()).onDamaged(event.getEntityLiving(), armor.getEquipmentSlot(), event.getSource(), event.getAmount());
            event.setAmount(newDamage);
        }
    }
}
```

Then make your `FlamingArmorItem` class implement `IDamageHandlingArmor` and override the `onDamaged` method. 

Mine will get the attacker from the damager source. If the attacker is living (which doubles as a null check), I'll deal half the damage I would have taken as fire damage, set it on fire for 4 seconds, and reduce the amount of damage I take by half. Otherwise, if there was no attacker (like if it was fall damage), I'll just take the  damage I would normally. 

```java
public class FlamingArmorItem extends ArmorItem implements IDamageHandlingArmor {
    // ...other code here...

    @Override
    public float onDamaged(LivingEntity entity, EquipmentSlot slot, DamageSource source, float amount) {
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity){
            attacker.hurt(DamageSource.ON_FIRE, amount / 2);
            attacker.setSecondsOnFire(4);
            return amount / 2;
        } else {
            return amount;
        }
    }
}
```

Again, note that `onDamaged` is called for each armor piece you you may want to add a check on the slot to avoid reacting to an attack multiple times.

Note that the `LivingDamageEvent` does not fire for every attack, only those that would deal enough damage to get through your armor. If you want to react to all attacks, you can use the `LivingAttackEvent` instead. However this does not allow you to change the amount of damage to be dealt. You can only completely cancel the attack (by calling `event.setCanceled(true);` from your event handler).

### Piglins 

You can override `makesPiglinsNeutral` to return true if you want your armor to act like gold and have piglins ignore the wearer. 

```java
@Override
public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
    return true;
}
```

### Related Tutorials 

- You can make armor with complex 3d models made in BlockBench. This will be covered in a future tutorial. Join [the discord server](/discord) to be notified when it is released. 

# Block Entities

Make a block entity that kills nearby mobs.  

A block entity is a simplified version of an entity that is bound to a specific block the the world. It knows its position, can react to ticks and saves data when the world is reloaded. Some vanilla examples are chests, furnaces and beacons. 

Before 1.17, block entities were referred to as tile entities. These terms are used interchangeably.

## Init

Start by making a block for your block entity. It will have its own class just like we made for other advanced blocks in the last tutorial. 

```java
public static final RegistryObject<Block> MOB_SLAYER = BLOCKS.register("mob_slayer",
            () -> new MobSlayerBlock(Block.Properties.copy(Blocks.IRON_BLOCK)));
```

Then you will need to register your block entity. Make a new class called `TileEntityInit`. We will start by getting a deferred register just like for blocks and items. 

```java
public class TileEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, FirstModMain.MOD_ID);
}
```

Then register your block entity. The string you pass in is the registry name which can be anything you want (all lower case, no weird characters as usual). Then you need a supplier for a `BlockEntityType` which has a builder to use. The builder takes a supplier to make new instance of your block entity. For this, use a method reference to the constructor of your block entity class (we'll make this class in the next step). Then you need to tell it the types of blocks that your block entity is allowed to be bound to. I'll just have one (the block we made earlier) but you can have multiple, just separate them with commas. Then we call the `build` method of the builder to get the type. 

```java
public static final RegistryObject<BlockEntityType<MobSlayerTile>> MOB_SLAYER = TILE_ENTITY_TYPES.register("mob_slayer",
            () -> BlockEntityType.Builder.of(MobSlayerTile::new, BlockInit.MOB_SLAYER.get()).build(null));
```

The null argument passed to the build method is a "data fixer type". Honestly, I have no idea what that means. It seems to not be used for anything and leaving it null has always worked for me. If you happen to know what it does, please let me know, I'm curious. 

Like with the item and block registries, you have to tell minecraft that you're trying to register things. Add this line to the constructor of your main class:

```java
TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);
```

## Block Class

The actual block for your block entity must have a custom class that implements `EntityBlock`. Then, override `newBlockEntity` to return a new instance of your block entity. I'm using the block entity type's `create` method but you could directly call the constructor once you make your block entity class).

```java
public class MobSlayerBlock extends Block implements EntityBlock {
    public MobSlayerBlock(AbstractBlock.Properties props) {
        super(props);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.MOB_SLAYER.get().create(pos, state);
    }
}
```   

## Tile Class

Now create the block entity class you referenced earlier (make a new `tile` package to keep organized). As you may have guessed, the class will extend `BlockEntity`. Pass your block entity type to the super constructor. 

```java
public class MobSlayerTile extends BlockEntity {
    public MobSlayerTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.MOB_SLAYER.get(), pos, state);
    }
}
```   

### Tick

#### Setup

To react to ticks, your **block** (not tile) class must override `getTicker`. This will just check that the `BlockEntityType` is the same one we just registered and then return a reference to the `tick` method (which we haven't made yet) of our `MobSlayerTile`.

```java
// MobSlayerBlock.java
@Nullable
@Override
public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return type == TileEntityInit.MOB_SLAYER.get() ? MobSlayerTile::tick : null;
}
```

The tick method must follow the `BlockEntityTicker` interface. it should look like this:

```java
// MobSlayerTile.java
public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
    MobSlayerTile tile = (MobSlayerTile) be;

    // your code here
}
```

This method will be called each tick (20 times per second).

#### Behaviour 

You can do anything you want here but I want my block entity to kill nearby monsters. I'll start by writing some logic for when to hurt the mobs. 

I'm going to start by making to variables on my `MobSlayerTile` class. I'll need a `timer` field (because I don't want to deal damage every tick, that would be too op) and an `isActive` field (because I want the player to be able to turn it off sometimes). 

```java
int timer = 0;
boolean isActive = true;
```

Then in my tick method, I'll check that we're on the server side (because that's where logic like dealing damage should be processed) and that the tile should be active. Then, each tick I'll increment the timer and only if 20 ticks (1 second) has passed since last time, I'll reset the timer and call a `hurtMobs` method (that I haven't defined yet). 
    
```java
// MobSlayerTile#tick
if (!level.isClientSide() && tile.isActive){
    tile.timer++;
    if (tile.timer > 20){
        tile.timer = 0;

        // only do this once per second
        tile.hurtMobs();
    }
}
```

I'll start by deciding the top left and bottom right corner of the area I want to target. I'll do this by adding the my chosen range to all the coordinates of the block entity to get the top and subtracting to get the bottom. Then I'll make an `AABB` from these points. This axis aligned bounding box represents a cube centered on my block entity going out `RANGE` blocks in all 3 dimensions. 

Then I'll use the world's `getEntities` method to get all the entities within that area of effect. The first argument is an entity to ignore (arrows use this to not hit themselves) but I want to get everything so I'll leave it null. The second argument is obviously the ``AABB`` to get entities within. Then I'll loop through the list it returns. For each entity, I'll check that it is living (not an arrow, item, etc) and not a player (I don't want my new tile to hurt me!). If it meets my criteria, I'll deal one heart of magic damage. 

```java
final int RANGE = 5;
private void hurtMobs() {
    BlockPos topCorner = this.worldPosition.offset(RANGE, RANGE, RANGE);
    BlockPos bottomCorner = this.worldPosition.offset(-RANGE, -RANGE, -RANGE);
    AABB box = new AABB(topCorner, bottomCorner);

    List<Entity> entities = this.level.getEntities(null, box);
    for (Entity target : entities){
        if (target instanceof LivingEntity && !(target instanceof Player)){
            target.hurt(DamageSource.MAGIC, 2);
        }
    }
}
```

Note: instead of doing that check in the for loop, I could have passed in a `Predicate<Entity>` which would return true only if it was something I want to target. That would probably run faster but not significantly enough to matter and I have a personal preference for how I did it. 

### Processing Block Interactions

I want to be able to toggle my mob slayer on and off by right clicking the block. I'll start by making a function that will preform the toggle. 

```java
public void toggle(){
    this.isActive = !this.isActive;
}
```

Then I need my block to notify my block entity when it is right clicked. I'll override the `use` method as we did in the last tutorial. After checking that I'm on the server side and processing the main hand (so it doesn't get called twice), I'll get the block entity at that position. To be safe, I'll check that it's the right type of block entity but in practice it always will be. I'll cast it to my block entity class and call the `toggle` method. For dramatic effect (and so its easier to tell that our click was processed), I'll make it play a sound as well. 

```java
@Override
public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND){
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof MobSlayerTile){
            ((MobSlayerTile) tile).toggle();

            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
    }

    return super.use(state, world, pos, player, hand, hit);
}
```

### NBT Data

Currently, when I save the world and reload it, the `isActive` variable will default back to true. We can override a few methods to save it with the rest of the world's data. 

The format Minecraft uses for most of the data it saves is called NBT ([Named Binary Tag](https://minecraft.fandom.com/wiki/NBT_format)). This is basically a map of keys and values (just like a more limited version of a `HashMap`) that can easily be serialized to a binary representation to store in files. 

In code, the class that is used for this format is `CompoundTag`. There are many types of data you can store in an `CompoundTag`: `boolean`, `int`, `String`, `double`, `float`, `long`, an array of `long`s, `byte`, a `Tag` (like another `CompoundTag` or a `ListTag`). You've got all the primitives and more so with enough effort you can make an nbt representation for any object. The general methods to use are `CompoundTag.putType(key, value)`, `CompoundTag.getType(key)` and `compundNBT.contains(key)`. The key is always a `String` and the value can be any supported type (just change the method name, ie `getString`, etc).

Tile entities use the `saveAdditional` method to get the data to store as a `CompoundTag` when the world is saved and `load` to read back that nbt into normal variables. I'll use this to simply store my `isActive` field and read it back. Make sure to call the super methods to let Minecraft store the block entity's base data. 

```java
@Override
public void saveAdditional(CompoundTag nbt) {
    super.saveAdditional(nbt);
    nbt.putBoolean("active", this.isActive);
}

@Override
public void load(CompoundTag nbt) {
    super.load(nbt);
    this.isActive = nbt.getBoolean("active");
}
```

Note that the nbt keys `id`, `x`, `y`, `z`, `ForgeData`, `ForgeCaps` and the registry name of the block entity type (for me that's `mob_slayer`) are reserved for the default data Minecraft has to store about your block entity. Do not try to use them. 

### Syncing Data

With my block entity, I've been careful to do everything on the server side. If you're doing something more complex, you may have to sync data between the client and the server. There are a few ways to do this. I will cover examples where you need to use these in a later tutorial on doing custom rendering with a block entity. Join [the discord server](/discord) to be notified when it is released. 

**On chunk load: **`getUpdateTag()` returns the data that the server wants to send as a `CompoundTag`. `handleUpdateTag(CompoundTag tag)` reads it back on the client side.

**On block update:** gotta use a packet! (but you can use one from vanilla). `getUpdatePacket` is sent from the server, `onDataPacket` is read on the client.

```java
@Override
public ClientboundBlockEntityDataPacket getUpdatePacket(){
    CompoundTag nbtTag = new CompoundTag();
    // save data to nbt 
    return new ClientboundBlockEntityDataPacket(this.worldPosition, -1, nbtTag);
}

@Override
public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
    CompoundTag tag = pkt.getTag();
    // read data
}
```

To trigger that you can call `world.notifyBlockUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags)`

**Custom packet:** a more versatile solution would be to make your own packets that you can send whenever you want (not just for block updates). I will cover packets in a future tutorial. Join [the discord server](/discord) to be notified when it is released. 

## Assets

The block to which you bound your block entity is a block like any other. Don't forget to give it a block model, item model, block state definition, loot table and lang entry.

You can also use code to do more complex rendering logic for your block entity. This will be covered in a future tutorial. Join [the discord server](/discord) to be notified when it is released. 

## Related Tutorials

Coming soon, join [the discord server](/discord) to be notified when they're released. 

- You can use your block entity to store items and open a gui to access them, like a chest. 

## Practice

- Make the mob slayer we made in this tutorial directional. That is, instead of the area of effect being centered on the block, have the block rotatable from like the last tutorial and only attack mobs in the forwards direction.
- Make a block entity that teleports nearby mobs on top of it
    - hint: `entity.setPos(x, y, z);`

# Mixins

**this tutorial is incomplete, check out the following resources instead,**

 - [The official mixin docs](https://github.com/SpongePowered/Mixin/wiki)
 - [Mixin Introduction by Darkhax](https://darkhax.net/2020/07/mixins)
 - [Fabric Wiki - tutorial:mixin](https://fabricmc.net/wiki/tutorial:mixin_introduction)

Mixins are a system for directly changing vanilla code. You can inject your own code into vanilla methods to change behaviour for which Forge does not yet have an event. At runtime, your mixins directly modify the bytecode of the classes you target before they are loaded. Although mixins are more versatile than events, they can be a bit tricky to get right and are generally worse for mod compatibility. You should almost always prefer using the Forge event when one exists. 

## build.gradle

There are a few changes that must be made to your build.gradle to let it know to load your mixins. 

## mixins.json

You will define which classes to load as mixins in a special json file. This file will go in your  

## Mixin Class

Create a new package called `mixins` and create a class called 




### Mixin Method


Note that dispite the fact that the target method has a return type, your mixin method returns void. The extra parameter (`CallbackInfo` or `CallbackInfoReturnable`) allows us to effect the return value of the target method. 

### Method Descriptors 

Use the [Minecraft Development Intellij Plugin](https://plugins.jetbrains.com/plugin/8327-minecraft-development) to generate.

#### Remapping

The method name in your descriptor is automatically remapped according to your project's mappings settings. This allows you to use the readable method names instead of the SRG names (ie. `addMix` instead of `func_193357_a`). This means there's an extra step if you are trying to mixin to a method that is not obfuscated and subsequently renamed by the mappings (anything outside of a vanilla class, for example something added by forge or another mod). You must set `remap` to false in your inject annotation. 

    @Inject(method = "...", at = @At(...), remap = true)
    private void injected(...) {
        // whatever code
    }

### Injection Point 

HEAD
RETURN

### Return a Value

If you want to change the return value of the target method (or just cancel the rest of the method call), you must set `cancellable` to true in your mixin method annotation. 

The last perameter of your mixin method will be of the type `CallbackInfoReturnable<T>` with `T` being the type returned by the target method. You can call `setReturnValue(value);` to change the return value of the target method. Note that this does not immediately exit your method the way a `return;` statement would.


    @Inject(method = "...", at = @At(...), cancellable = true)
    private void injected(CallbackInfoReturnable<Float> callback) {
        callback.setReturnValue(3.14F);
    }

### Accessing The Object

#### Shadowing

When you need to access methods or fields on the target class from your mixin code, you can use the `@Shadow` annotation. Create a null field or an empty method that just throws an error. The `@Shadow` annotation will redirect these references to the ones on your target class. 

#### Using `this`

You may want to directly access the object you are mixing into. You may be frustraited to discover that when you use the `this` keyword, it is an instance of your mixin class, not of the target class. You can get around this by casting to `Object` and then to your target class. This often serves the same purpose as shadowing. 

For example, in a method that targets the ItemEntity class, you could use the following code to get the actual object and then any public methods/fields will be available. 

```
ItemEntity item = (ItemEntity) (Object) this; 
```

> Also note that the mixin class is not actually present at runtime. Its bytecode is just added to vanilla's. You should not have static fields on the mixin class that you try to reference from other places in your mod's code. However, the code in your mixin methods can access the rest of your mod's classes.  


## Other Documentation  

The mixin system used by forge is called [SpongeMixin](https://github.com/SpongePowered/Mixin) and is developed by [Mumfrey](https://github.com/Mumfrey). It is also used by Fabric and Sponge so almost any documentation on mixins for those platforms will also apply to Forge. 

If you want to read more about how to use mixins, check out the following: 

 - [The official mixin docs](https://github.com/SpongePowered/Mixin/wiki)
 - [Mixin Introduction by Darkhax](https://darkhax.net/2020/07/mixins)
 - [Fabric Wiki - tutorial:mixin](https://fabricmc.net/wiki/tutorial:mixin_introduction)

# Dependencies on Other Mods

Sometimes you'll want gradle to load other libraries or mods so you can access their code. Most of the work will be done in your build.gradle file. 

## Dependencies Block

```
dependencies {
    <function> <descriptor>
}
```

The function tells gradle when it should load the dependency.

- **Use `implementation` to compile against the dependency and have the mod present when you run the game from your development environment. This is probably what you want most of the time.**
- If you want a mod present when you run the game from your development environment but do not want to compile against its code, use `runtimeOnly`. Useful if you just want other mods present in game without referencing their code. For example, JEI to see recipes or an RF generator mod for testing. 
- If you want to compile against the dependency but don't want it present when you run the game from your development environment, use `compileOnly`. When you do this you must be careful about when the dependency is class loaded so it doesnt crash without it. Useful for optional dependencies when you want to test that your mod works without them present. 

The descriptor tells it what dependency to load. But you must define urls to repositories where it can download the files for the dependencies you declare. 

Wrapping the descriptor string in the `fg.deobf` function will tell forge to deobfuscate that jar which lets you use compiled mods in your development environment. 

## Repository Sources

### Project Repo

Many mods specifically intended to be libraries used by other developers will provide their own public maven repository for you to use. Make sure to read the specific project's documentation (curseforge description, github readme, wiki, etc) and see how they recommend depending on them. For example, Geckolib has an [installation wiki page](https://github.com/bernie-g/geckolib/wiki/Installation) that shows how to reference their cloudsmith maven repository. If the project you want to depend on doesn't provide something just use one of the options below that will work regardless. 

### Curseforge

If you want to get a mod directly from curseforge you can use [cursemaven.com](https://www.cursemaven.com). 

```
repositories {
    maven {
        url 'https://cursemaven.com'
    }
}

dependencies {
    implementation fg.deobf('curse.maven:Descriptor-ProjectID>:FileID')
}
```

- The Descriptor can be any string you want. It's just there to help you tell which dependency is which. Making it the modid of the mod you're depending on is probably a good plan.
- The Project ID can be found on the About Project section of the project
- To get a File ID, go to the download page of file you want to use, and the file ID will be in the URL.

Here's an example that adds Repurposed Structures: https://github.com/LukeGrahamLandry/mercenaries-mod/blob/main/build.gradle#L150-L152

### Github

If you want to get a mod from directly from github you can use [jitpack.io](https://jitpack.io). It will be a bit slow the first time because they have to checkout and build the project before they can serve it to you. You can go to [their website](https://jitpack.io) and trigger a build of the correct version of your repository ahead of time so its ready when you need it. Note that the main artifact built will be the normal mod jar so you must tell ForgeGradle to deobfuscate it for use in your development environment. 

```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation fg.deobf('com.github.UserName:RepoName:Version')
}
```

The version should be the tag name of a github release. If the repository doesn't have any releases published, you can use the short commit hash (which you can find on the commits page) instead of the version. You can also use `branchname-SNAPSHOT` to get the latest version. 

### Your Computer

If you want to temporarily depend on a jar file without dealing with finding a maven repo to host it, you can load it directly from your computer's file system. This has a big disadvantage in that anyone who wants to work with your mod's code has to go find the right jar files and put them in the right places. If you want to help others contribute to your project, go with one of the other maven repository sources above. 

```
repositories {
    flatDir {
        dirs '/path/to/libs/folder'
    }
}

dependencies {
    implementation fg.deobf('tld.packagedomain.mod:Name:Version')
}
```

The descriptor above will first search for `/path/to/libs/folder/Name-Version.jar` and then if that isn't found  `/path/to/libs/folder/Name.jar`.

> If you use `Name-Version` instead of `Name:Version`, it will still find the same jar file but the fg.deobf function will crash because it tries to name the remapped artifact based on the version which it only gets by splitting the artifact descriptor on `:`

## mods.toml

You should add an entry to the bottom of your mods.toml file. This will give users a more meaningful crash message when they try to start the game without the required mod. Base it off the format of the `minecraft` and `forge` dependencies in the default mods.toml from the mdk. 

- You can depend on a specific version range of the mod
- You can mark the dependency as optional so it doesn't crash without it
- You can mark which side the dependency applies on (SERVER or CLIENT or BOTH)
- You can say whether the dependency mod should be loaded before or after your mod. 

## Shading

Shading a dependency allows you to bundle it into your mod's built jar file so players don't need to install it separately.

On fabric you could just use the `include` function but old versions of forge make it way more work. Forge 1.19 and later alleges to natively support the JarJar library for including other mod jars within yours so you can take a look at [the Jar-In-Jar forge community wiki page](https://forge.gemwire.uk/wiki/Jar-in-Jar) instead (**ONLY FOR 1.19+**). This should even work for jars that forge needs to load as mods. For older versions, read on!

Shading has a few disadvantages:

- extra effort to setup
- makes your jar file larger
- players my end up with multiple copies of the same library if its used by multiple mods (doesn't actually break anything just feels wasteful)
- prevents library authors from getting income from curse points if players don't have to severalty download their mod
- challenging to get right since shaded jars are not loaded as mods. It's intended for java libraries whose code you need to access rather than other mods that need to access forge events or mixins (which should be downloaded separately).

How To:

- https://gist.github.com/SizableShrimp/949e7c219bfc94487a45226b64ac7749
- shade docs: https://imperceptiblethoughts.com/shadow/introduction/
- geo wiki: https://github.com/bernie-g/geckolib/wiki/Shadowing
- geo example: https://gist.github.com/AzureDoom/3f0df105ac480c058a486879ebc86520

## Mixins

Mixin is a powerful framework that allows you to directly modify Minecraft's byte code at runtime. You can use mixins to modify the byte code of other mods, just like you would vanilla Minecraft. It's not very safe to do this however since mods are likely to change drastically between versions with no warning. This means that although your mixin works fine for the version of your dependency you test with, if the mods updates to add a new feature, the method you target may disappear or the code within may change enough that whatever you're doing no longer works properly. It will probably be lots of effort to keep your mixins functional over an extended period of time. A more future proof option may be to find your dependency on github and make a pull request that adds an API that makes whatever you're doing easy. 

My [mixins tutorial](mixins) is incomplete. See other resources:

 - [The official mixin docs](https://github.com/SpongePowered/Mixin/wiki)
 - [Mixin Introduction by Darkhax](https://darkhax.net/2020/07/mixins)
 - [Fabric Wiki - tutorial:mixin](https://fabricmc.net/wiki/tutorial:mixin_introduction)


Remember that your inject annotation must have `remap=false` when targeting methods added by other mods (or forge itself). This is because mixin automatically tries to remap everything according to your project's mappings settings but will get confused because the other mod won't have a remapped name since it was never obfuscated. 

### Mixin Plugins

- mixin plugin that stops it from attempting to apply the mixin if the targeted mod isn't present
- api: https://github.com/SpongePowered/Mixin/blob/master/src/main/java/org/spongepowered/asm/mixin/extensibility/IMixinConfigPlugin.java
- example: https://github.com/Tfarcenim/CuriousJetpacks/blob/1.16.x/src/main/java/tfar/curiousjetpacks/MixinPlugin.java (with `plugin": "tfar.curiousjetpacks.MixinPlugin"` in mixins json)

### Fixing Error With Other Mod's Mixins

Mixin can get a bit confused when you depend on a mod that needs to add its own mixins but uses different [mappings](http://blog.minecraftforge.net/personal/sciwhiz12/what-are-mappings/) than you do. To fix this, in your build.gradle, add the following to your `client` and `server` definitions within the `run` block.

```
property 'mixin.env.remapRefMap', 'true'
property 'mixin.env.refMapRemappingFile', "${projectDir}/build/createSrgToMcp/output.srg"
```

## Class Loading 

For soft dependencies you should check that the mod is loaded before trying to reference any of its code so it doesn't crash when the dependency isn't available. 

TODO: example

## Final Thoughts

Please don't be one of those people that makes a bullshit library mod just to farm ad income on curse forge. It's so fucking annoying. It makes it that much more of a pain in the ass for other people to work with your mod's code (they have to setup two projects to actually be able to change anything). It's also a waste of time for players that don't use the curse forge launcher since they have to go install the library as well. This extra time adds up if every mod author makes the genius discovery that you earn a couple extra cents if you split up all your mods into several. 

That's not to say there are no good library mods. Geckolib, Patchouli, Curios, and Architectury, to name a few, single handedly save developers hundreds of hours. Just try to be one of the useful ones. 

# Releasing Your Mod

When your mod is ready for other people to play it, you'll want them be able to add it to their game rather than only running in a development environment. I'll review your options for distribution websites and give a tutorial on using gradle to automatically publish your mod. 

## Build 

To build your mod's code into a jar file that can be loaded by minecraft, you simply run the `build` gradle task. This can be done from intellij or from the command line. 

```
./gradlew build
```

> Make sure you use `cd` to navigate to your mod project folder before running the command.  
> On windows, do not include the `./` at the beginning of the command.  

When the task is finished running, your built jar file will be in the `build/libs` folder.   

To install the mod, just drop that jar file in `minecraft/mods` and launch the game with forge installed. 

## Distribution Platforms 

There are many websites that will let you upload your mods and make it easy for players to install them. If you want to give players the choice of what distribution platform they prefer, nothing's stopping you from uploading your mods to multiple websites. 

### Github

[Github](https://github.com) is a great place to host your mod's code if you want it to be open source. It also has "Github Releases" which lets you upload files and tag them as the release corresponding to a certain commit. 

Github Releases Advantages:

- Don't have to create a new project on a new website if you're already using Github for version control. 

Github Releases Disadvantages:

- Doesn't have special support for minecraft mods. You can upload files but there's no integrated system for marking which minecraft version and mod loader is supported (you can just write it in the description tho). 

### Curseforge

[Curseforge](https://www.curseforge.com) has been the most popular website for hosting Minecraft mods for a long time.  

Curseforge Advantages:

- Include Minecraft specific meta data (version, mod loader, etc) with your uploaded files 
- If your mod gets a lot of downloads you will earn Curse Points which can be redeemed to paypal 
- Lots of modpacks are developed using the Curseforge platform which only makes it easy to include mods from Curseforge. Your mod will probably reach more players if it can be used in popular modpacks.  
- They have a popular launcher that integrates tightly with their mod and modpack platform. 

Curseforge Disadvantages:

- Their frontend is just bad. Page often load slowly and somewhat regularly its just completely down for a few hours.

### Modrinth 

[Modrinth](https://modrinth.com) is an open source alternative to Curseforge.

Modrinth Advantages:

- Include Minecraft specific meta data (version, mod loader, etc) with your uploaded files 
- **Much** faster and prettier website than Curseforge 
- It's nice to support platforms that prevent one company from having a total monopoly on mod distribution. 
- They have their own modpack format that can be imported by ATLauncher, MultiMC, and Prism Launcher.
- Provides an api for downloading mods that isn't crippled by Curseforge's monetization obsession (this makes it easier for third party apps to integrate with them)

Modrinth Disadvantages:

- Less popular than Curseforge, might not provide as much discoverability for your mod. 
- Still under development, some features you might want from their website aren't ready yet (comments, ad revenue sharing, analytics).

## Gradle Plugins

If you end up making several mods, it will eventually get irritating to have to click through the website's interface to upload a file every time you want to release some new content. Especially if you want to give players the choice of what distribution platform to use so you want to upload to multiple websites. If you get to that point, check out the following gradle plugins. With a bit of configuration, they'll give you a gradle task to automatically upload a new release. 

- https://github.com/shedaniel/unified-publishing
- https://github.com/BreadMoirai/github-release-gradle-plugin

## JustEnoughPublishing

JustEnoughPublishing is a gradle script I wrote that uses the plugins above to release your mod based on a simple json configuration file. Personally I dislike copy pasting the plugin boiler plate across all my projects. Instead, I just put a one line script import in my build.gradle file which will set up everything based on a json file containing the project's settings. This script is less versatile than using the plugins directly so use whichever suits your situation best. 

### Usage

1. import the script (bottom of build.gradle): `apply from: "https://moddingtutorials.org/publish.gradle"`
2. create the config file as specified bellow
3. define your api keys as environment variables and make sure you expose the required gradle properties (more info below)
4. run `gradlew publishMod` whenever you want to publish a new version (can be done in a github action, [example](https://github.com/LukeGrahamLandry/ForgedFabric/blob/1.16/.github/workflows/publish.yml))
 
### Config File
 
Location Options

1. {root}/.github/publish.json 
2. {subproject}/publish.json 
3. {root}/publish.json):

The config file contains a json object with the following fields: 

- versions: array of supported minecraft versions. ie ["1.19.3", "1.19.2"]
- loader: "forge" or "fabric" (fabric will automatically mark for quilt as well)
- changelogUrl: url will be added to description body (optional, a changelog.txt file may be used instead)
- releaseType: (optional) default is "release", could also be "beta" or "alpha"
- github: (optional)
     - repo: the name of the repository to release to
     - owner: the name of the github account that owns repo
     - branch: the name of the branch this release is based on
     - requiredDependencies: (optional) map of display name to url
- curseforge: (optional)
     - id: the id of the project to publish the file (as a string)
     - requiredDependencies: (optional) list of project slugs
- modrinth: (optional)
     - id: the id of the project to publish the file (as a string)
     - requiredDependencies: (optional) list of project slugs

[example](https://github.com/LukeGrahamLandry/ForgedFabric/blob/1.16/.github/publish.json)

### gradle.properties variables

- version: the version number of your mod's current release, should match the entry in your mods.toml

### Secrets

These are your api keys and should **not** be committed to your git repository. They can be set in `USER_HOME_FOLDER/.gradle/gradle.properties` or as environment variables. Each is only required if you defined that platform in your publish.json file. 

- CURSEFORGE_API_KEY: https://authors.curseforge.com/account/api-tokens
- GH_API_KEY: personal token with repo permissions https://github.com/settings/tokens
- MODRINTH_API_KEY: https://modrinth.com/settings/account

## Forge Version Checker

- https://forge.gemwire.uk/wiki/Version_Checker

# Modding Questions and Answers 

If there's something you want to learn how to do that i dont have a tutorial for and you can't figure out from vanilla's code, feel free to [join my discord server](https://discord.com/invite/uG4DewBcwV) to ask me. This page is a collection of information I've given people on that server so you can search it more easily. It will only be helpful if you're already comfortable with Java! These answers are for a mix of versions so you will have to do some translating but they should point you in the right direction. 

> I might add an index and split it into seperate pages to help the SEO at some point if i feel inspired

## how scale and translate text on a gui? 

> QUESTION: I’m using posestack.scale for the scale and just manually tweaking some math for the x and y to get it to look okay. It works but I’m thinking there must be a better way to translate the positions when doing non standard scales like 0.8**

the thing that makes it weird is that when you scale the matrix it scales any translations you've done as well, including the position you told the text to be at. so really what you want is to draw the text centred at (0,0), then scale it, then translate it to where you actually want it. tho calling the translate/scale methods will move everything, not just the text you're trying to work with. it might work if you,

- PoseStack#pushPose
- render text centered on 0,0 (you'll probably need to check the width and height of the component since i'm guessing you pass in the top left coordinate when you render it)
    - the Screen class has a this.font variable you can use (it gets it from Minecraft.getInstance().font). can do this.font.draw(poseStack, Component.literal("words"), x, y, 0xFFFFFF); 
- scale it
- translate to real position
- PoseStack#popPose
and wrap that up into a method that lets you render whatever text at whatever position at whatever scale

## is it a problem to override depreciated methods on Block/Item?

the depreciated warning istelling you that if you ever need to call that method, you should call it from the blockstate/itemstack instead but if you're making your own block/item you do have to override it on the block/item singleton class itself

## what are the parameters of the Slot constructor (for inventory menus)? and how does the slot know how big to be in the gui?

- Container/Inventory: the container of items the slot should mondify 
- index: the container has a list of itemstacks. this is the index in that list that the slot represents. used when `Container#getItem`, `Container#setItem`, and `Container#removeItem` are called by the `Menu`
- x: the x coordinate of the top left corner of the slot's square on the gui
- y: the y coordinate of the top left corner of the slot's square on the gui

you'll notice you dont have to pass in the width/height because all slots are a square of a set size (you'll notice that all the slots in vanilla inventories are the same size).

## what do i do instead of the genIntellijRuns gradle task if i want to use vscode

you dont have to do the generate runs thing, its just a convince thing for the intellij/eclipse people to run the game from within the ide. can just use gradlew runClient from the command line when you want to run the game
other than that, should be exactly the same as long as you can figure out how to make vscode import a gradle project on your own (there is a plugin for it)

## i was following one of your tutorials and dont have one of the packages you mentioned 

the packages are just for organization, doesn’t actually matter, you can just make it if you feel inspired. please learn java more, ty!

## you say to copy a class from vanilla (ie. tool Tiers or ArmorMaterials), how do i see vanilla classes?

in intellij you can press shift twice to search through vanilla classes (theres a little check box in the top right that says “include non project items” or something that you should click so its not just searching your code), im sure eclipse/vscode can do it to but idk the keyboard shortcut  

also, if you're looking at a reference to a vanilla class/method, you can "Go To Declaration", generally by holding command/control and clicking on it

## how do i make a geckolib animation sync with an attack

geckolib has a pretty good wiki (https://github.com/bernie-g/geckolib/wiki/Home) for the details of making animations play at all. for an attack animation specificly, you'd have a dataparameter on your entity that syncs an int from server to client and then when your attack starts you set that to the length of your animation (in ticks) and decrement it every tick until its 0 and check it on the client (in your animation predicate) to play your animation while its ticking down. my mimic mod has an example you can look at https://github.com/LukeGrahamLandry/mimic-mod (look in the `MimicEntity` and `MimicAttackGoal` classes)

## how do i make the dev environment work on apple silicon for 1.12

absolutly no clue. deal with the slowness of roseta 2 emulating it or upgrade to a version of minecraft from not 5 years ago i guess. if you happen to figure it out, please tell me how and i'll document it here

## how do i make my tile entity save its nbt data when you break the block and place it down again?

can look at how shulker boxes do it. this is their loot table https://github.com/InventivetalentDev/minecraft-assets/blob/1.18.2/data/minecraft/loot_tables/blocks/shulker_box.json and ShulkerBoxBlock overrides getDrops to set a tag through that loot table in a complicated way  
but i think you should be able to just override getDrops to return a list of just an item stack of the block with whatever data written to the tag 

## when i create a FluidTank from my block entity (for capability), what do i pass into the validator?

the validator is just a predicate for which fluids its allowed to store. so like if you want it to store any type of fluid just use `(f) -> true`, or if it should only store water that would be `(f) -> f.getFluid().is(FluidTags.WATER)` and i think thats like if it is ever allowed to store that type of fluid so dealing with like limiting it to a certain capacity is handled separately 

## my data gen isnt working

make sure you are running the gradle `runData` task (ie `./gradlew runData` from the terminal)

## why does calling `forceAddEffect` crash?

`LivingEntity#forceAddEffect` only exists on the client so if you try to call it from server/common code, it will crash. instead, you should always call `LivingEntity#addEffect`, preferably only on the server side (check that `!level.isClientSide()`) and let minecraft sync it to the client on its own

## how do i make a bunch of versions of the same item but change the colour of part of the texture (like leather armor or spawn eggs)

theres a whole thing for having a greyscale overlay texture and then tinting it. its like the one thing forge docs have lol https://mcforge.readthedocs.io/en/1.16.x/models/color/ thats for 1.16 but i think it should be the same. 

you can store which colour it should use in the item stack's nbt tag and read it back when the game wants to apply the tint so you dont need lots of different items. you can also have different layers of texture, defined in your model json file, if you only want to tint part of the texture a certain colour

for doing something that tints based on the biome colour (like grass and leaves) look at the vanilla code for how it gets that biome's colour in BlockColors#createDefault which calls GrassColors#get

## how do i replace my arrow render with a blockbench model 

not something i know of a specific tutorial for but i can give general instructions if you're quite comfortable with java. (its pretty much the same as giving any entity a custom model if you can find a tutorial for that except you can't use a LivingEntityRenderer since your arrow isnt a LivingEntity)

- make sure the bb model is in the Java Modded Entity project type
- export it as a java class & import all the packages it needs
- have an EntityRenderersEvent.RegisterLayerDefinitions event listener and call `event.registerLayerDefinition(YourModelClass.LAYER_LOCATION, YourModelClass::createBodyLayer);` (specificlly 1.17+)
- create a renderer class that renders your new model instead of the arrow one. its render method would call renderToBuffer on your model. 
- bind that renderer to your arrow entity type in FMLClientSetupEvent as befor

## i want to make an entity's health configureable but it doesnt sync when i update the config file (it only syncs when the game restarts)

if just got the config value on the `EntityAttributeCreationEvent` or whatever where you bind the base attributes to the entity type, thats not gonna work because that event happens once at the beginning when entity types are registered, it's not called for every entity. to make it actually update live with the config you'd have to have all the entities checking if it changes and applying attribute modifiers to their health to get it to the right value

## how can I increase the melee attack range for a vanilla mob

mixin to `MeleeAttackGoal#getAttackReachSqr`

## how do I check what dimension a player is in?

`player.getLevel().dimension()` gives you a `ResourceKey<Level>` which can be compared to `Level.OVERWORLD`, `Level.NETHER`,  `Level.END`, etc

## how can i make sleeping skip night in another dimension

I it seems to be handled by the SleepStatus class which players are added to in ServerPlayer#startSleepInBed only if the dimension type has natural set to true but you could probably add people on your own. and then ServerLevel#tick checks the doDaylightCycle game rule and sets the time to day if SleepStatus thinks enough people are sleeping.  so maybe some mixins somewhere in there could do what you want. idk if that helps at all 🤷. There's also a PlayerSleepInBedEvent but i think it mostly lets you block sleeps, not allow them

## im looking to make a custom furnace but i do not know where to start

Look at how vanilla furnaces work. Basiclly, make a tile entity that holds items and crafts things every x ticks, bind a container/screen to it so you can put in items, decide how you want to deal with recipes (hard code them or make your own recipes type and use json files)

## how do i make my projectile render like an item

you could extend `ProjectileItemEntity` or just impliment `IRendersAsItem` and then when you bind a renderer to your entity type, you would do `RenderingRegistry.registerEntityRenderingHandler(EntityInit.PUT_NAME_HERE.get(), (m) -> new SpriteRenderer<>(m, Minecraft.getInstance().getItemRenderer()));`

## why am i getting a "player moved wrongly" error

instead of calling `player.setPos(...)`, try `((ServerPlayerEntity)player).connection.teleport(x, y, z, player.yRot, player.xRot, EnumSet.noneOf(SPlayerPositionLookPacket.Flags.class))` (make sure to do it in the if block that checks !isclientside). thats what the teleport command uses 

## my custom cactus block just breaks when it grows 

If you made your cactus based on the vanilla CactusBlock, it breaks if the canSurvive method returns false. vanilla has that method call canSustainPlant on the block state below it, so you should make sure that your plant is a valid block for your plant to grow on. You can do that by overriding canSustainPlant on your custom cactus block and returning true if the state passed in is of your plant. I think this would work, 

```java
@Override
public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
    BlockState plant = plantable.getPlant(world, pos.relative(facing));

    if (plant.is(this)){
        return true;
    } else {
        return super.canSustainPlant(state, world, pos, facing, plantable);
    }
}
```

## my custom sugarcane/cactus blocks just continues to grow instead of stopping at 3

for the sugar cane, i would think using the same randomTick method as vanilla's SugarCaneBlock would work. it seems to check that there's less than a 3 tall stack of your plant before growing so unless you changed the number in that for loop / took out the if statement, idk

## how would i make a block drop xp like ores do?

vanilla ones use the OreBlock class which just overrides this method (you can return whatever number you want. can give more based on fortune level but should give 0 if they have silk touch)

```java
@Override
public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
    return silktouch == 0 ? RANDOM.nextInt(7) : 0;
}
```

## my non-cube blockbench modeled block makes ajacent faces of blocks not render properly

![](/img/nonfull-block-render-error.png)

minecraft's rendering system culls block faces it thinks are hidden. you need to tell it your block is not a full cube by calling `RenderTypeLookup.setRenderLayer(BlockInit.YOUR_BLOCK.get(), RenderType.cutout())` on the `FMLClientSetupEvent`

## any error with the phrase "not valid resource location"

ResourceLocations cannot have spaces or capital letters

## how to make a spawn egg for custom entity (forge deferred registers)

since items are registered before entities, you should make your own verison of the `SpawnEggItem` class and override `getType` in such a way that you don't have to call .get() on your registry object before its registered

## how do i make a pickaxe that destroys 3x3 area

steal the logic from this https://github.com/Ellpeck/ActuallyAdditions/blob/main/src/main/java/de/ellpeck/actuallyadditions/common/items/ItemDrill.java (its MIT licensed) and port it to your MC version / mod loader

## how can i fix NullPointerException that is produced when summoning my entity

make sure you bind a renderer in the `ClientSetupEvent` and bind attributes (max health specificlly) on the `EntityAttributeCreationEvent` (like `event.put(EntityInit.YOUR_ENTITY.get(), attributes)`)

## how do i make an autosmelt pickaxe (or other conditional modification of lots of loot tables at once)

use global loot modifers! they run whenever minecraft gets items out of a loot table

- docs: https://mcforge.readthedocs.io/en/1.17.x/items/globallootmodifiers/
- example: https://github.com/LukeGrahamLandry/inclusive-enchanting-mod/blob/main/src/main/java/io/github/lukegrahamlandry/inclusiveenchanting/events/SmeltingLootModifier.java

## why doesnt the tick method on my block fire

problem is that the `tick` method on blocks is for random ticks (https://minecraft.fandom.com/wiki/Tick#Random_tick) so its not called every tick. You also need to call `randomTicks()` on your `Block.Properties` to make it react to random ticks at all. To do something every tick you could have a tile entity (ive got a tutorial for that). if you just want a timer to do something a while after you place your block, it might be better to schedule a tick. if you do `world.getBlockTicks().scheduleTick(pos, this, delay)` in your `onPlace` method, it should call `tick` after `<delay>` ticks.

## how do i make my item use the honey bottle gulping animation? 

the HoneyBottleItem has a method that specifies the animation to play while its being used

```java
@Override
public UseAnim getUseAnimation(ItemStack pStack) {
    return UseAnim.DRINK;
}
```

and then theres a big switch statement that checks for that and renders it differently in ItemInHandRenderer#renderArmWithItem

## how do i change the slipperiness of every block?

You could use a mixin to change the return value of `Block#getSlipperiness` (there are two versions of that method with different parameters, should do both). Here's a good explanation of mixins: https://darkhax.net/2020/07/mixins  

Alternatively, you could also use an access transformer to make `Block#slipperiness` public and then loop through every block and change the value after the blocks have been registered (ie. on `FMLCommonSetupEvent`)

## how to do i make custom armor models (1.17+)

- model class should extend HumanoidModel and have all the parts that should move with the player named correctly (same as 1.16 so probably already done).
- make sure you're registering the model layer definition on the EntityRenderersEvent.RegisterLayerDefinitions event. (example: https://github.com/LukeGrahamLandry/AmbientAdditions/blob/forge-1.18/src/main/java/coda/ambientadditions/client/ClientEvents.java#L158)
- on your custom item class that extends ArmorItem, you override the initializeClient method and add a IItemRenderProperties instance that has a getArmorModel method that returns an insteance of your custom  model class. here's an example: https://github.com/LukeGrahamLandry/AmbientAdditions/blob/forge-1.18/src/main/java/coda/ambientadditions/common/items/YetiArmWarmersItem.java#L46-L58 

## my FMLClientSetupEvent isnt firing

- make sure the method is static
- make sure the method has the `@SubscribeEvent` annotation
- make sure the class has the `@Mod.EventBusSubscriber(modid = ModMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)`. You must use `Bus.Mod`, NOT `Bus.FORGE`

## how do i make key bindings?

- Create a keybind `public static final KeyMapping OPEN = new KeyMapping("key.your_action_name", GLFW.GLFW_KEY_M, "key.categories." + ModMain.MOD_ID);` somewhere
- Call `ClientRegistry.registerKeyBinding(YOUR_KEY);` on `FMLClientSetupEvent` 
- checking `YOUR_KEY.isDown()` or `YOUR_KEY.consumeClick()` on `KeyInputEvent` should work. You'll have to send a packet if you want to react to it on the server side

## What is the int parameter on `Level#setBlock`

the third parameter is a flag about which block updates it should send. flags can be OR-ed (passing in `2 | 4` will have the effect of both)

- 1 will cause a block update
- 2 will send the change to clients
- 4 will prevent the block from being re-rendered
- 8 will force any re-renders to run on the main thread instead 
- 16 will prevent neighbor reactions (e.g. fences connecting, observers pulsing)
- 32 will prevent neighbor reactions from spawning drops
- 64 will signify the block is being moved

i think the fourth parameter is how many more layers of recursive block updates to allow. you can just use the version of the method that doesnt need this fourth parameter and just sets it to a reasonable default (512)

## how do I make a mob say something in the chat when it is killed?

There's an `LivingEntity#onDeath` method to override (make sure you still call the super of it) and then you can loop through `world.getPlayers()` and call `player.sendStatusMessage(new TextComponent("hello world"), false)` 

## how do i make an entity that has multiple textures?

whatever resource location is returned by the `getEntityTexture` method in your renderer will be used as the texture. but theres only ever one instance of the renderer so you cant do the random in its constructor. you have to have the entity know which texture it should have, check that from the `getEntityTexture` method and return the appropriate `ResourceLocation`. Note that you'll want to save which texture it chose in its nbt data (override `addAdditionalSaveData` and `readAdditionalSaveData`) as well as use a `EntityDataAccessor` (previously called `DataParameter`) to sync the chosen texture from the server to the client (forge docs: https://mcforge.readthedocs.io/en/1.18.x/networking/entities/#data-parameters). You can look at vanilla's `Cat` code for an example. 

## does the order that deferred registers are used in the mod's constructor matter?

nope, the whole point is that forge magically does it at the right time for you. 

## how to make an item appear with blue letters under the name like a potion
 
look at the `PotionItem#appendHoverText` which calls `PotionUtils#addPotionTooltip`. Example: `pTooltips.add((new TranslatableComponent("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE));`

## how do i have different behaviour for specific players?

Each minecraft account has a Universally Unique Identifier. There are websites to get these from a player's username (like https://mcuuid.net). Have a list of people's uuids (ie. `List<UUID> coolPeople = List.of(new UUID[]{UUID.fromString("bcb2252d-70de-4abc-9932-bc46bd5dc62f")});`) and then have an if statement that checks `coolPeople.contains(player.getUUID())`. You could even put that list on pastebin or equivilent and fetch it with an http request so you could update it without changing your mod's code. 

## how to make an http request

its just part of the normal jdk libraries

```java
public static String get(String url) {
    try {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        InputStream responseStream = connection.getInputStream();

        return new BufferedReader(
                new InputStreamReader(responseStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}
```

## how to change the enchantment glint colour 

its not easy. here's an overview of i did for 1.18.2, 

I was hoping this would be defined as a hex value in the code but it's actually a texture: minecraft:textures/misc/enchanted_item_glint.png.
This texture is at ItemRenderer.ENCHANT_GLINT_LOCATION but that variable is not actually referenced
at render time so changing it back and forth wouldnt work. Instead, the texture is used as a
RenderStateShard.TextureStateShard to create a RenderType. (the specific one i care about changing
in this case is ARMOR_ENTITY_GLINT). I use a mixin to HumanoidArmorLayer that runs before and after
renderArmorPiece is called. This allows me to access the actual item stack and then react to it in
another mixin to a method that wouldn't have that context available. Then I mixin to ItemRenderer#getArmorFoilBuffer
to change the render type that is used to create the vertex consumer to be rendered.

here's the code, dont forget the mixins and access transformers, https://github.com/LukeGrahamLandry/herobrine-thing/blob/main/src/main/java/ca/lukegrahamlandry/herobrinething/client/WhiteArmorGlintHelper.java

## how to play a gif as an overlay in game

https://gist.github.com/gigaherz/56b259126f715807c8e6c3dc055b924b

## how to add a villager like trade gui to a custom entity 

have your entity implement Merchant
- on mobInteract call setTradingPlayer and openTradingScreen
- override getOffers , you'll have to make a MerchantOffers object (if you choose the trades randomly you'll probably want to save them in the entity's nbt so they stay the same for the same entity instance). look at WanderingTrader#updateTrades for an example of how to populate the trades. the individual trade options will be an object implementing ItemListing, there are a bunch of classes you can use in VillagerTrades, just scroll down a bit
- you could just extend AbstractVillager and override updateTrades to save some time implementing the other Merchant stuff. depends if your entity needs to extend something else for some specific reason 
- if you actually want to load trades from a data pack instead of hardcoding them thats some extra work. you have to register a SimpleJsonResourceReloadListener on AddReloadListenerEvent, here's an example https://github.com/LukeGrahamLandry/modular-professions-mod/blob/forge-1.18/src/main/java/ca/lukegrahamlandry/modularprofessions/api/ProfessionDataLoader.java. you'd load the trade data in from json files and then use that to create the MerchantOffers described above

## how do tile entity renderers work 

basically you end up with a render method that gets called every frame for each instance of your tile entity where you can run whatever rendering code you like. you're never going to find a detailed tutorial on what to actually do in that method cause there's like infinite variations and nobody likes dealing with rendering code. you pretty much just have to look around vanilla and other mod's code and slowly figure out what you can do

More Info: https://forge.gemwire.uk/wiki/Block_Entity_Renderer

## how to create a moving entity

start by registering your entity and renderer like in [my projectiles tutorial](/arrows) but have it extend PathfinderMob then you can override registerGoals, look at some vanilla mobs for examples, like Cow uses WaterAvoidingRandomStrollGoal

if you want details on how goals work take a look at https://github.com/Tslat/SmartBrainLib/wiki/How-do-Goal-Selectors-Work