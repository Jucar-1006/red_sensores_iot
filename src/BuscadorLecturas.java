/**
 * PLATAFORMA DE MONITOREO AMBIENTAL URBANO
 *
 * Contiene algoritmos de búsqueda utilizados por el proyecto.
 */
public class BuscadorLecturas {

    /**
     * Cantidad de comparaciones realizadas por la última búsqueda.
     */
    private static int comparaciones = 0;

    public static int getComparaciones() {
        return comparaciones;
    }

    /**
     * Búsqueda lineal por timestamp.
     * No necesita que los datos estén ordenados.
     */
    public static int busquedaLinealPorTimestamp(LecturaSensor[] datos, String timestamp) {
        comparaciones = 0;

        for (int i = 0; i < datos.length; i++) {
            comparaciones++;

            if (datos[i].getTimestamp().equals(timestamp)) {
                return i;
            }
            /* Utilizamos .equals() porque queremos comparar el valor real 
               contenido en memoria y no solo las referencias o punteros de los objetos String. */
        }
        return -1;
    }

    /**
     * Busca la primera lectura de una estación de forma lineal.
     */
    public static int buscarPorEstacion(LecturaSensor[] datos, String idSensor) {
        comparaciones = 0;

        for (int i = 0; i < datos.length; i++) {
            comparaciones++;

            if (datos[i].getIdSensor().equals(idSensor)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Busca un timestamp mediante búsqueda binaria O(log n).*/
    public static int busquedaBinariaPorTimestamp(LecturaSensor[] datos, String timestamp) {
        comparaciones = 0;

        int inicio = 0;
        int fin = datos.length - 1;

        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            comparaciones++;

            int comparacion = datos[medio].getTimestamp().compareTo(timestamp);

            if (comparacion == 0) {
                return medio;
            }

            if (comparacion < 0) {
                inicio = medio + 1; 
                /* Sumamos +1 al índice para forzar el avance del intervalo y descartar 
                   el pivote actual, evitando así caer en un ciclo iterativo infinito. */
            } else {
                fin = medio - 1;
            }
        }
        return -1;
    }

    /**
     * Búsqueda binaria iterando sobre el campo PM2.5.
     * PRECONDICIÓN INCUMPLIDA: El arreglo del generador no está ordenado por PM2.5.
     */
    public static int busquedaBinariaPorPm25(LecturaSensor[] datos, double pm25) {
        comparaciones = 0;

        int inicio = 0;
        int fin = datos.length - 1;

        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            comparaciones++;

            if (datos[medio].getPm25() == pm25) {
                return medio;
            }

            if (datos[medio].getPm25() < pm25) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        
    
        return -1;
    }
}
