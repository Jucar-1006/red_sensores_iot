# Decisiones de diseño — Semana 3

## 1. Arquitectura Centralizada (Punto de entrada)
El proyecto mantiene un único punto de entrada en el ciclo de vida de la aplicación: `IngestaSensores.main()`. 
**Justificación:** Se descarta la creación de aplicaciones o clases `main` independientes por semana para garantizar la cohesión del sistema. Clases como `BancoDePruebas` actúan puramente como módulos auxiliares invocados por el controlador principal, respetando el Principio de Responsabilidad Única.

## 2. Estrategia de Búsqueda por Timestamp
Se implementan dos algoritmos con propósitos distintos:
- **Búsqueda lineal O(n):** Utilizada como línea base de rendimiento. No requiere precondiciones estructurales.
- **Búsqueda binaria O(log n):** Implementada como solución definitiva de eficiencia. 
**Justificación:** Dado que `GeneradorDatos` inyecta los registros en memoria simulando el paso cronológico del tiempo, el arreglo cumple inherentemente la precondición de estar ordenado de forma ascendente, permitiendo aprovechar la eficiencia logarítmica de la búsqueda binaria.

## 3. Búsqueda Binaria por PM2.5 (Fallo intencional)
La búsqueda binaria sobre el campo PM2.5 se mantiene en el código estrictamente como un experimento de validación algorítmica.
**Justificación:** Las lecturas de partículas ingresan al sistema con valores aleatorios. Al violar la precondición de ordenamiento, el algoritmo binario arroja falsos negativos. Esta decisión documenta que la eficiencia de un algoritmo es inútil si la estructura de datos no cumple con las reglas matemáticas subyacentes.

## 4. Semántica de Comparación (Strings)
Para localizar identificadores de estación, se utiliza exclusivamente el método `.equals()` en lugar del operador `==`.
**Justificación:** El operador `==` evalúa si dos variables apuntan a la misma dirección de memoria (punteros), mientras que `.equals()` evalúa el contenido semántico del objeto. Esto evita falsos negativos al comparar cadenas de texto instanciadas en diferentes momentos.

## 5. Métrica de Rendimiento (Profiling)
Para evaluar la eficiencia algorítmica entre `O(n)` y `O(log n)`, el análisis principal se basa en el contador de iteraciones internas (`comparaciones`), aislando el tiempo de ejecución en milisegundos como un dato secundario.
**Justificación:** Medir el tiempo de CPU es volátil y depende de la carga del sistema operativo y del Garbage Collector de la JVM. Contar el número de comparaciones lógicas ofrece una métrica determinista e inmutable para el análisis de notación Big O.

## 6. Evolución del Proyecto
La arquitectura del sistema sigue un modelo de capas acumulativas:
`Sensores -> Ingesta (Validación) -> Repositorio (TAD) -> Búsqueda y Eficiencia`.
La plataforma está lista para integrar módulos de reordenamiento de memoria en la Semana 4 sin alterar la lógica de persistencia de las semanas previas.
