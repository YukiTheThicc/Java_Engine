# NOTAS

### PRIMEROS PASOS

Primero, crear el motor y añadir las funcionalidades justas y necesarias para CREAR UN JUEGO y EXPORTARLO usando
un editor separado del juego y el motor como tal.

- [ ! ] Necesitaré establecer el motor, el juego y el editor/forntend en paquetes/librerías independientes. Tanto
        el forntend como el juego serán dependientes del motor.

- [ ! ] Tendré que establecer una manera de crear proyectos desde el editor, editarlos y finalmente exportarlos de
manera que al ejecutarlos se abra solamente el juego. Podría facilitar esto teniendo dos clases contenedoras del
juego, una para el editor que ejecute el código de edición y otra que no. Así, al exportar el juego se podría
hacer directamente con la clase contenedora final en vez del contenedor del frontend.

- [ ! ] De momento no me voy a preocupar por tener precompiladas las librerías del motor o frontend, todo el
código va a ser abierto. Ignorar entonces cosas que puedan tener que ver con el IDE.

- [ ! ] Una vez el proyecto esté más avanzado, será importante separar los proyectos de forntend, motor y
 juego en sus propios proyectos en sus repositorios.

### A TENER EN CUENTA / TODO

- [x] Ver una alternativa para la carga y guardado de shaders
        - Simplemente se hacen dos constructores, uno de ellos recibe dos ficheros y el otro uno :)
      
- [ ! ] La capa de ImGUI ahroa mismo está asociada a la ventana, un objeto que debería de ser independiente de Sapphire
  - [ ] Cambiar / Refactorizar SappImGUILayer para que sea o un singleton o un objeto estático que se dibuje de forma
        independiente en el beginFrame del contenedor de Sapphire
    - [ ] Refactorizar ventana en general para que no haya ataduras entre ventana, capa de imgui y frame buffer. Igual
            centralizar los accesos a framebuffers o permitir tener más de uno, lanzando hilos de renderizado por cada vez
            que se quieran usar, etc...
  - [ ] Considerar crear un hilo para la capa de imgui (igual es mejor que no lo sea)


xd
