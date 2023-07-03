#  Proyecto Base de Karate para pruebas de integración en AcceptanceTest - REST, GraphQL, SOAP
_Karate es una herramienta de código abierto que combina la automatización de pruebas de API, simulacros , pruebas de rendimiento e incluso la automatización de la interfaz de usuario en un marco único y unificado . La sintaxis BDD popularizada por Cucumber es un lenguaje neutro y fácil incluso para los no programadores. Las afirmaciones y los informes HTML están integrados y puede ejecutar pruebas en paralelo para aumentar la velocidad._

_Si está familiarizado con Cucumber / Gherkin, la gran diferencia aquí es que no necesita escribir código extra de "pegamento" o "definiciones de pasos" de Java._

**RECOMENDACION !!!**: Visitar la documentacion oficial para obtener todas las ventajas de este potencial framework: https://github.com/intuit/karate
## Comenzando

### Instalación ?

**IMPORTANTE**: Este proyecto es una demo, proyecto base, para estructurar las pruebas de integracion (AcceptanceTest) que se realizarán, no es funcional si se ejecuta sin modificar, por eso a continuacion de contamos que debes modificar y configurar para comenzar en tu contexto de aplicacion con las pruebas:
- Ir al karate-config.js y modificar la `urlBase` por la url o endpoint de tu aplicacion
- Ir a los archivos .feature (src>test>resources>co.com.bancolombia) agregar tus escenarios, metodos de prueba, aserciones, y todo lo necesario para tus pruebas en particular
- Ir a los runners de prueba (src>test>java>co.com.bancolombia) y agregar en Runner.path(nombre del feature a probar)



Aquí se detalla la estructura que debe guiar las pruebas con Karate, es un ejemplo:

```
????src
????test
????java
?   ????co.com.bancolombia
?   |    ????demo
|   |    |    ????DemoRunner.java
|   ????TestParallel.java
|
????resources
????karate
|    ????demo
|    |   ????demo.feature
????karate-config.js
????logback-test.xml
```

- TestParallel -> Clase general en java que ejecuta los TESTS de karate en Paralelo y tambien genera el reporte de dichos TESTS en formato json que luego se convierte en reporte cucumber

## Ejecutando las pruebas ??

```gradle
gradlew clean test -i
```