#README

La app de stress está diseñada para ser usada tanto por un terminal del sistema operativo como usando una interfaz gráfica de fácil uso.

Para usar la app de stress es necesario indicarle los siguientes parámetros:

* **Devices** o clientes a conectar al nodo destino.
* **Network Services** indica la cantidad de plugins de este tipo a iniciar por cada cliente.
* **Actors** cantidad de actores generados en cada *Network service*, emulando la creación de perfiles por un usuario en cada *network service*.
* **IP Address** la dirección IP del nodo destino de la prueba.

Esta app es capaz de iniciar los clientes indicados, poner a funcionar los plugins del tipo Network Service, crear actores, solicitar listado de actores conectados e iniciar un proceso de envío, recepción y respuesta de mensajes de forma automática por un tiempo predeterminado.

##Ejecución.
Se puede iniciar en modo gráfico ejecutando la clase **Main** desde la raíz del módulo `stress-app`, mientras que para iniciar la versión sin interfaz gráfica se ejecuta la clase **ConsoleMain** ubicada en el mencionado fichero.

Cada vez que se ejecuta un build de gradle, este genera un archivo .jar el cual se ubica en la carpeta `build/libs` llamado `stress_app.jar` el cual está listo para ser ejecutado usando `java -jar stress_app.jar`, con ese comando se ejecuta la app en modo gráfico, sin embargo, si se requiere el uso de la misma sin interfaz es necesario suministrar alguno de los siguientes argumentos:

```-d o --devices para indicar la cantidad de clientes a conectar
-n o --ns para indicar la cantidad de network services a iniciar
-a o --actors para indicar la cantidad de actores (usuarios) que se van a crear para emular el proceso de envío y recepción de mensajes.
-i o --ip para indicar la dirección IP del nodo al cual se requiere hacer la prueba de stress```

Por ejemplo:

```java -jar stress_app.jar -d 5 -n 10 -a 8 -ip 192.168.100.100```

Indica que se solicita realizar una prueba con 5 clientes, iniciando 10 Network Services cada uno y estos, a su vez, creando y registrando 8 actores. Todos estos elementos apuntarán apuntando a la dirección IP 162.168.100.100

En caso de no suministrar algún argumento o que exista un error en alguno de estos no hay problema, la app seleccionará un valor por defecto para realizar la prueba.