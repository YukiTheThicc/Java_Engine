# Diamond Engine

    DEV NAME: JAVA_ENGINE

A Java game engine based on OpenGL.

This project is a personal attempt to design and produce a close-to fully featured engine using Java. Its basic structure
consists on a front-end(_Sapphire_) and back-end(_Diamond_). 

## State

### Priorities

At this moment, the priority is to develop _Sapphire_ into a state in which it has enough features to comfortably develop
the first core elements of Diamond, so they can be exposed and managed through Sapphire. This includes the file edition system,
logging system, game viewport, customization elements and much more.

### Diamond

 * [x] Fully featured level-based logging system running on an independent non-blocking thread
 * [x] Wrapped TinyFD for native file dialog utilities

### Sapphire

 * [x] Can create, open and save files and edit them from Sapphire.
 * [x] Can create and open Sapphire/Diamond projects
 * [x] Can customize elements and preferences for Sapphire
 * [x] File navigator for the current project
 * [x] Preferences, settings and last session data is stored and recovered when booting up again

## Diamond Games

At this moment there is no game structure, as the engine and front-end are not featured enough yet to support building
a game.

## Dependencies

This engine is being developed using the following dependencies:

* lwjgl: https://github.com/LWJGL/lwjgl3
* SpaiR ImGUI: https://github.com/SpaiR/imgui-java
* jbox2d-library: https://github.com/jbox2d/jbox2d
* joml: https://github.com/JOML-CI/JOML
* gson: https://github.com/google/gson
