# Diamond Engine

    DEV NAME: JAVA_ENGINE

A Java game engine based on OpenGL.

This project is personal attempt to design and produce a close-to fully featured engine using Java. Its basic structure
consists on a front-end(_Sapphire_) and back-end(_Diamond_). At this moment both are treated as the same project, and 
are just separated into different packages internally. It's planned to separate them either into different projects or 
independent modules in the same project.

**_A lot of documentation is still missing from this file and form the Drive linked at the end. If you are reading this, 
please understand that it is a very early WIP, and it´s being design and developed as it goes. Plus, it's being developed
in my scarce free time. Thank you for your patience ;-)_**

## Priorities

At this moment, the priority is to develop _Sapphire_ into a state in which it has enough features to comfortably develop
the first core elements of Diamond, so they can be exposed and managed though Sapphire. This includes a file edition system, 
logging system, game viewport and some customization (docking windows, confirmation modals, etc.)

The development for this project is in the Google Drive folder linked at the end of the readme.

## Sapphire

Front-end for the engine. Based on the JNI implementation of ImGUI made by SpaiR (itself based on Dear ImGui from ocornut).

## Diamond

The engine itself, and back-end for _Sapphire_.

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

Specification, resources, diagrams and other design features or processes will be documented as READ ONLY in my Drive:
* https://drive.google.com/drive/folders/1QSpYuB_y0PqkAHAJdpkySM-61mCchTGd?usp=sharing