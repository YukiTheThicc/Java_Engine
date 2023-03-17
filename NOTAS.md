# NOTAS

### PRIMEROS PASOS

Primero, crear el motor y aÃƒÂ±adir las funcionalidades justas y necesarias para CREAR UN JUEGO y EXPORTARLO usando
un editor separado del juego y el motor como tal.

- [ ! ] NecesitarÃƒÂ© establecer el motor, el juego y el editor/forntend en paquetes/librerÃƒÂ­as independientes. Tanto
        el forntend como el juego serÃƒÂ¡n dependientes del motor.

- [ ! ] TendrÃƒÂ© que establecer una manera de crear proyectos desde el editor, editarlos y finalmente exportarlos de
manera que al ejecutarlos se abra solamente el juego. PodrÃƒÂ­a facilitar esto teniendo dos clases contenedoras del
juego, una para el editor que ejecute el cÃƒÂ³digo de ediciÃƒÂ³n y otra que no. AsÃƒÂ­, al exportar el juego se podrÃƒÂ­a
hacer directamente con la clase contenedora final en vez del contenedor del frontend.

- [ ! ] De momento no me voy a preocupar por tener precompiladas las librerÃƒÂ­as del motor o frontend, todo el
cÃƒÂ³digo va a ser abierto. Ignorar entonces cosas que puedan tener que ver con el IDE.

- [ ! ] Una vez el proyecto estÃƒÂ© mÃƒÂ¡s avanzado, serÃƒÂ¡ importante separar los proyectos de forntend, motor y
 juego en sus propios proyectos en sus repositorios.

### A TENER EN CUENTA / TODO

- [x] Ver una alternativa para la carga y guardado de shaders
        - Simplemente se hacen dos constructores, uno de ellos recibe dos ficheros y el otro uno :)
      
- [ ! ] La capa de ImGUI ahroa mismo estÃƒÂ¡ asociada a la ventana, un objeto que deberÃƒÂ­a de ser independiente de Sapphire
  - [ ] Cambiar / Refactorizar SappImGUILayer para que sea o un singleton o un objeto estÃƒÂ¡tico que se dibuje de forma
        independiente en el beginFrame del contenedor de Sapphire
    - [ ] Refactorizar ventana en general para que no haya ataduras entre ventana, capa de imgui y frame buffer. Igual
            centralizar los accesos a framebuffers o permitir tener mÃƒÂ¡s de uno, lanzando hilos de renderizado por cada vez
            que se quieran usar, etc...
  - [ ] Considerar crear un hilo para la capa de imgui (igual es mejor que no lo sea)

