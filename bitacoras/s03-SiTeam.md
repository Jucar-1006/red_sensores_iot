# Bitacora individual - Semana [03]

## 1. Datos de la actividad

- **Estudiante:** Julián Cárdenas
- **Equipo:** Si Team
- **Semana:** 3
- **Fecha del laboratorio:** 2026-09-17
- **Fecha del taller:** 2026-09-17
- **Tema principal:** Búsqueda lineal, búsqueda binaria y análisis de eficiencia (O(n) vs O(log n))
- **Pregunta de la semana:** ¿Cómo encontramos una lectura específica cuando el repositorio pasa de cientos a cientos de miles o millones de registros?

## 2. Prediccion antes de ejecutar

Antes de abrir o ejecutar el programa, responde:

1. **Que creo que va a ocurrir?**
   En el Experimento 1 (Búsqueda Lineal), preveo que el número de comparaciones será exactamente igual al tamaño del arreglo al buscar el último dato (O(n)), ya que el ciclo `for` en `BuscadorLecturas.java` debe recorrer posición por posición. Para el Experimento 2 (Búsqueda Binaria), el algoritmo descartará mitades consecutivas, reduciendo las comparaciones a un número diminuto (O(log n)), siempre que los timestamps estén previamente ordenados de forma ascendente.

2. **Que parte del programa o del algoritmo puede fallar?**
   El uso del operador lógico de igualdad (`==`) para comparar cadenas de texto (`String`) en los identificadores de estación o en los timestamps. Si la clase `BuscadorLecturas` compara los identificadores de sensores utilizando `==` en vez del método `.equals()`, fallará al encontrar coincidencias, ya que evaluará las direcciones de memoria de los objetos instanciados y no su contenido, arrojando falsos negativos.

3. **Como comprobare mi prediccion?**
   Monitorizando los resultados de los experimentos impresos en consola desde el `main()` único en `IngestaSensores`. Analizaré la columna de "comparaciones" en la tabla del experimento dos, comparando el rendimiento lineal vs binario. Además, revisaré si la búsqueda intencionalmente defectuosa (`buscarPorEstacionDefectuoso`) retorna `-1` a pesar de que el identificador sí exista en el set de datos generado.

## 3. Evidencia del laboratorio

### Resultado observado

Al compilar y ejecutar el proyecto único a través de `java IngestaSensores`, se ejecutaron en cascada todos los experimentos generados por `BancoDePruebas`. En el caso del millón de registros, la búsqueda lineal invirtió exactamente 1,000,000 de comparaciones para encontrar el último dato, mientras que la búsqueda binaria alcanzó la misma respuesta correcta efectuando tan solo 20 comparaciones. 

### Diferencia entre la prediccion y el resultado

El comportamiento coincidió milimétricamente con el análisis algorítmico previsto en notación Big O: `O(n)` vs `O(log n)`. Sin embargo, al ejecutar el Experimento 4 probando la búsqueda binaria directamente sobre el campo de la variable de partículas (PM2.5), el resultado divergió drásticamente de la efectividad obtenida con el timestamp.

### Error o comportamiento inesperado

- **Que ocurrio?** En el experimento cuatro, la búsqueda binaria fue incapaz de encontrar registros válidos y preexistentes al realizar consultas filtradas por la variable `PM2.5`. Arrojó un valor muy inferior de aciertos respecto a la búsqueda secuencial lineal clásica.
- **Por que ocurrio?** Se violó deliberadamente una precondición estructural algorítmica. La búsqueda binaria requiere que el espacio de iteración esté ordenado numéricamente para poder aplicar el descarte de mitades (dividir y conquistar). El generador aleatorio de `PM2.5` (`5 + azar.nextDouble() * 55;`) inserta los datos en desorden, causando falsos rechazos en la evaluación de la partición media.
- **Como lo corregimos o que falta corregir?** Debemos ordenar explícitamente el arreglo en base al campo objetivo antes de ejecutar el algoritmo binario, o bien mantener el uso de búsqueda lineal si el arreglo sufre inserciones constantes desordenadas y el costo de reorganizarlo repetidamente supera al de la búsqueda misma. Este rediseño queda planteado para resolverse en el taller de la semana 4 (Ordenamiento).

## 4. Explicacion en lenguaje llano

Explica el concepto principal como se lo explicarias a una persona de doce
anos. Usa entre tres y cinco lineas y evita palabras tecnicas que no expliques.

> Imagina que buscas una palabra en un diccionario. La búsqueda lineal es como empezar en la página 1 y leer hoja por hoja hasta dar con la palabra; funciona, pero es lentísimo si está en la letra Z. La búsqueda binaria es como abrir el diccionario por la mitad: si tu palabra es con M y abriste en la H, descartas la primera mitad entera y vuelves a partir el bloque que sobra, encontrándola en segundos porque las palabras ya están ordenadas.

### Ejemplo o analogia

La búsqueda lineal se asemeja a buscar un objeto extraviado en una habitación desordenada; debes revisar cajón por cajón. La búsqueda binaria se asemeja a adivinar un número del 1 al 100 preguntando siempre por la mitad: "¿Es mayor a 50?", descartando de golpe 50 opciones por cada intento. La analogía del número deja de ser exacta porque requiere que otra persona te responda si es "mayor" o "menor" (un ente externo), mientras que en el algoritmo el arreglo se compara contra el valor deseado de forma autónoma basándose en la precondición de ordenamiento.

## 5. El vacio que encontre

Al intentar explicar el tema, identifica el punto que aun no comprendes bien.

- **Mi duda concreta es:** Entendiendo que la búsqueda binaria `O(log n)` es infinitamente superior a la lineal `O(n)`, ¿por qué no aplicarla por defecto en toda la plataforma si su código no agrega un costo computacional mayor?
- **Lo que ya puedo explicar es:** Puedo explicar el funcionamiento interno del descarte de intervalos `(inicio + fin) / 2` y la diferencia teórica de rendimiento frente a un recorrido que revisa cada índice uno por uno desde el 0.
- **Para resolver la duda consulte:** El experimento número cuatro de la guía, la documentación oficial respecto a las precondiciones de algoritmos de reducción, y simulé mentalmente el estado del índice sobre arreglos aleatorios.
- **Ahora lo entiendo asi:** La búsqueda binaria no puede aplicarse a ciegas a todos los parámetros porque su diseño exige una precondición inquebrantable: el arreglo debe estar estrictamente ordenado bajo el campo que se desea consultar. Si la plataforma necesita buscar sensores por `PM2.5` y luego por `Temperatura`, ordenar el arreglo de un millón de registros cada vez que un usuario cambia el filtro podría representar un costo computacional tan masivo que neutralizaría el tiempo ganado en la búsqueda.

## 6. Trazado de la solucion

Escoge una ejecucion, recorrido o caso representativo y trazalo paso a paso.
Incluye los valores importantes despues de cada paso.

A continuación se traza el recorrido en memoria del método `busquedaBinariaPorTimestamp` al localizar el valor objetivo en un arreglo preordenado. 
Arreglo `datos`: `[10, 20, 30, 40, 50, 60, 70, 80]` — **Objetivo:** `70`.

| Paso | Estado de los datos o estructura | Decision o resultado |
|---|---|---|
| 1 | `inicio = 0`, `fin = 7`. Inicio del ciclo `while (inicio <= fin)`. | Se calcula el índice intermedio `medio = (0 + 7) / 2 = 3`. |
| 2 | Evaluación en `medio = 3`. El valor alojado `datos[3]` es `40`. | Al comparar, `40 < 70`. El objetivo debe estar en la mitad superior. |
| 3 | Reasignación de límites inferiores. Descarte de la mitad izquierda. | Se asigna `inicio = medio + 1 = 4`. El valor `fin` permanece en `7`. |
| 4 | Nuevo ciclo con `inicio = 4`, `fin = 7`. | Se calcula el nuevo índice intermedio `medio = (4 + 7) / 2 = 5`. |
| 5 | Evaluación en `medio = 5`. El valor alojado `datos[5]` es `60`. | Al comparar, `60 < 70`. El objetivo aún es mayor. |
| 6 | Nueva reasignación. | Se asigna `inicio = medio + 1 = 6`. `fin = 7`. |
| 7 | Último ciclo. Cálculo del pivote con el rango ajustado. | Se calcula el último índice `medio = (6 + 7) / 2 = 6`. |
| 8 | Evaluación en `medio = 6`. El valor alojado `datos[6]` es `70`. | `70 == 70`. La condición de igualdad se cumple. Retorna el índice `6`. |

## 7. Decision de diseño

Relaciona lo aprendido con la Plataforma de Monitoreo Ambiental Urbano.

- **Problema que debiamos resolver:** El alto costo de procesamiento y demora al consultar registros específicos por campo de *timestamp* sobre un volumen de datos que escaló al nivel del millón de filas, lo cual saturaba el sistema si utilizábamos ciclos secuenciales simples.
- **Estructura, algoritmo o estrategia elegida:** Integración de Búsqueda Binaria (`busquedaBinariaPorTimestamp`).
- **Alternativa descartada:** Mantener exclusivamente la función original de Búsqueda Lineal (`busquedaLinealPorTimestamp`).
- **Por que elegimos la primera:** Por optimización de escala. Dado que el generador de registros ambientales inserta los timestamps secuencialmente a medida que transcurre el tiempo cronológico, el arreglo ya cumple naturalmente con la precondición de ordenamiento ascendente. Aprovechando esto, la búsqueda binaria nos permite encontrar cualquier registro descartando progresivamente las mitades, reduciendo una carga teórica de 1.000.000 de iteraciones a menos de 25, sin impactar significativamente la memoria (RAM).
- **Que evidencia respalda la decision:** Los resultados del Experimento 2. Al enfrentar ambos algoritmos empíricamente con `1.000.000` de datos generados en memoria, la lectura del contador interno demostró la eficiencia logarítmica de la binaria (`O(log n)`) superando el rendimiento estático de la búsqueda secuencial (`O(n)`).

## 8. Aporte al proyecto

- **Archivo(s) o modulo(s) trabajado(s):** Principalmente `BuscadorLecturas.java`, `BancoDePruebas.java`, y `GeneradorDatos.java`.
- **Cambio realizado:** En `BuscadorLecturas`, implementé la lógica de partición de límites (`inicio`, `fin`, `medio`) para efectuar búsquedas binarias, corrigiendo el error de iteración infinita ajustando correctamente `inicio = medio + 1`. Asimismo, adapté el único `main()` en `IngestaSensores` para correr los cuatro experimentos bajo la misma sesión de la JVM sin segmentar el proyecto.
- **Como se conecta con la capa anterior:** Los algoritmos diseñados en esta semana 3 se interconectan con el Tipo Abstracto de Dato (TAD) construido en la semana 2. La capa de búsqueda recibe las referencias de la memoria estática ya curada e implementa sobre ella métodos de consulta directa para localizar métricas ambientales específicas, permitiendo realizar consultas estadísticamente útiles.
- **Que queda pendiente para la siguiente semana:** Resolver la precondición faltante del Experimento 4. La plataforma requiere ordenar de manera independiente métricas desalineadas como el `PM2.5` antes de aplicar rutinas logarítmicas. Falta definir e integrar el algoritmo de ordenamiento más eficiente (Semana 4).

## 9. Commits realizados

Registra los commits que muestran tu aporte individual.

| Commit | Mensaje | Que demuestra |
|---|---|---|
| `[hash corto]` | `feat: agregar búsqueda lineal por timestamp` | Implementación secuencial base para comparar eficiencia |
| `[hash corto]` | `feat: generar datos sintéticos ordenados` | Creación de casos de prueba sin sobrecargar el archivo `.csv` |
| `[hash corto]` | `feat: implementar búsqueda binaria y banco de pruebas` | Aplicación de la lógica `O(log n)` y consolidación en único `main` |

*(Reemplaza `[hash corto]` con los identificadores reales de tu repositorio local al hacer push).*

## 10. Reexplicacion final

Despues del taller, vuelve a responder la pregunta de la semana en cinco lineas
o menos. Esta respuesta debe ser mas precisa que la de la seccion 4 y debe
incluir la razon de tu decision tecnica.

Encontramos datos masivos implementando un algoritmo de búsqueda binaria `O(log n)`. Esta técnica divide iterativamente el arreglo por la mitad, descartando sectores que numéricamente no contienen el valor. La decisión técnica se justifica porque el costo algorítmico pasa de procesar un millón de iteraciones (búsqueda lineal) a escasamente 20, asumiendo estrictamente la precondición de que los timestamps lleguen al repositorio ordenados de forma ascendente.

## 11. Reflexion individual

Responde con honestidad:

1. **Lo que ahora puedo hacer y antes no podia:**
   Comprender y medir formalmente el impacto de un mal diseño en la escalabilidad. Antes resolvía problemas iterando arreglos completos; ahora soy capaz de diseñar estructuras que dividen y conquistan bajo esquemas logarítmicos, validando precondiciones para evitar ciclos infinitos.
2. **El error o supuesto que mas me enseno:**
   Creer ciegamente que comparar cadenas con `==` arrojaría fallos explícitos de compilación. Entender por qué en Java se debe invocar el método `.equals()` para comparar el contenido de dos objetos `String` me aclaró la diferencia arquitectónica entre comparar el puntero referencial de la memoria frente a la semántica real del dato.
3. **La pregunta que llevaria a la proxima clase:**
   Al someter una arquitectura a grandes cargas de procesamiento, ¿cómo evaluamos si el costo en memoria y CPU de aplicar un algoritmo de ordenamiento total (como `QuickSort` o `MergeSort`) se justifica económicamente frente a conformarnos con una búsqueda secuencial simple?
4. **Que parte del trabajo fue realmente mia:**
   La corrección estructural del flujo del ciclo `while` en el diseño binario (asegurar el avance de los punteros iterativos) y la justificación argumentada de por qué el algoritmo fracasaba silenciosamente al operar sobre un parámetro desordenado como el indicador `PM2.5`.

## Lista de verificacion antes de entregar

- [X] Escribi la prediccion antes de consultar el resultado.
- [X] Inclui evidencia concreta del laboratorio.
- [X] Explique un concepto sin depender de jerga.
- [X] Registre un vacio, una duda o un error real.
- [X] Trace al menos un caso paso a paso.
- [X] Justifique una decision del proyecto y una alternativa descartada.
- [X] Registre mis commits y mi aporte individual.
- [X] Deje claro que queda pendiente.
- [X] Renombre el archivo con el formato `sXX-nombre.md`.
