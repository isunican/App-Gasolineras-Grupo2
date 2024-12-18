# Manual de uso

# Índice

1. [Puntos de Interés](#puntos-de-interés)
   - [Añadir un nuevo punto de interés](#añadir-un-nuevo-punto-de-interés)
   - [Cancelar la Operación](#cancelar-la-operación)
   - [Posibles Errores y Mensajes](#posibles-errores-y-mensajes)
2. [Filtros sobre Gasolineras](#filtros-sobre-gasolineras)
   - [Ordenar gasolineras más cercanas a un punto de interés](#ordenar-gasolineras-más-cercanas-a-un-punto-de-interés)
   - [Cancelar la Operación](#cancelar-la-operación-1)
   - [Posibles Errores y Mensajes](#posibles-errores-y-mensajes-1)
3. [Filtros sobre Precio de Combustible](#filtros-sobre-precio-de-combustible)
   - [Filtrar por precio máximo de un combustible concreto](#filtrar-por-precio-máximo-de-un-combustible-concreto)
   - [Cancelar la Operación](#cancelar-la-operación-2)
   - [Posibles Errores y Mensajes](#posibles-errores-y-mensajes-2)
4. [Quitar filtros y ordenaciones](#quitar-filtros-y-ordenaciones)
5. [Novedades de Interfaz](#novedades-de-interfaz)
   - [Horario gasolineras](#horario-gasolineras)
   - [Mostrar los Precios de los Combustibles](#mostrar-los-precios-de-los-combustibles)


## Puntos de Interés

### Añadir un nuevo punto de interés

1. El usuario selecciona la opción **Añadir punto de interés** en el menú desplegable.


<img src="IMG/imagenAnhadirPunto.png" alt="Paso 1" width="200">


3. El usuario debe rellenar los siguientes campos:
   - **Nombre**: El nombre del nuevo punto de interés.
   - **Latitud**: La latitud en formato numérico (por ejemplo, `43.4623`).
   - **Longitud**: La longitud en formato numérico (por ejemplo, `-3.8099`).

<img src="IMG/imagenMenuAnhadirPunto.png" alt="Paso 3" width="200">


5. Después de introducir la información, el usuario debe pulsar el botón **Guardar** para almacenar el punto de interés en la base de datos.

6. Si algún campo está vacío o los valores numéricos son incorrectos, el sistema mostrará un mensaje de error:
   - "Por favor, llene todos los campos" si algún campo está vacío.
   - "Por favor, ingresa valores numéricos válidos para latitud y longitud" si los valores no son numéricos.

7. Si ya existe un punto de interés con el mismo nombre, se mostrará un mensaje:
   - "Ya existe un punto de interés con ese nombre".

8. Si todo es correcto, el sistema mostrará un mensaje de éxito:
   - "Punto de interés guardado".
   - A continuación, la vista se cerrará automáticamente.

### Cancelar la Operación

El usuario puede cancelar el proceso en cualquier momento pulsando el botón **Cancelar**, lo cual cerrará la vista sin guardar ningún dato.

### Posibles Errores y Mensajes

- **Por favor, llene todos los campos**: Aparece cuando uno o más campos están vacíos.
- **Por favor, ingresa valores numéricos válidos para latitud y longitud**: Aparece cuando los valores introducidos para latitud o longitud no son numéricos.
- **Ya existe un punto de interés con ese nombre**: Aparece cuando el nombre del punto de interés ya está registrado en la base de datos.
- **Ha ocurrido un error en la base de datos**: Aparece cuando ocurre un error inesperado al acceder a la base de datos.

## Filtros sobre gasolineras

### 1. Ordenar gasolineras mas cercanas a un punto de interés.

1. El usuario pulsa el icono de ordenar gasolineras que aparece en la barra de opciones de la parte superior de la aplicación.

<img src="IMG/imagenIconoOrdenarPunto.png" alt="Paso 3" width="200">

2. Aparece una ventana emergente donde se le muestra al usuario los puntos de interés. Una vez seleccionado pulsa el botón "ORDENAR".
   
<img src="IMG/imagenVentanaOrdenarPunto.png" alt="Paso 3" width="200">

3. Se le muestra al usuario la lista de gasolineras ordenada por cercanía al punto de interés solicitado.
   
<img src="IMG/imagenGasolinerasOrdenadasPorPunto.png" alt="Paso 3" width="200">

### 2. Cancelar la Operación

El usuario puede cancelar el proceso en cualquier momento pulsando el botón **Cancelar**, lo cual cerrará la vista sin guardar ningún dato.

## Posibles Errores y Mensajes

1. **Error: No hay ningún punto de interés añadido** En este caso el usuario tendrá que pulsar el botón "CANCELAR" y añadir un punto de interés.
   
<img src="IMG/imagenErrorSinPuntos.png" alt="Paso 3" width="200">

## Filtros sobre precio de combustible 

### 1. Filtrar por precio máximo de un combustible concreto

1. El usuario pulsa el icono de filtrar que aparece en la barra de opciones de la parte superior de la aplicación.

<img src="IMG/img-precio-max-1-1.png" alt="Paso 3" width="200">

2. Aparece una ventana emergente donde puedes seleccionar el combustible y escribir el precio máximo pulsando en su campo respectivamente. Una vez que has puesto tus preferencias pulsa el botón **FILTRAR** para filtrar las gasolineras.

<img src="IMG/img-precio-max-2.png" alt="Paso 3" width="200">

3. Si el campo de **PRECIO MAXIMO** está vacío al pulsar **FILTRAR** aparecerá el siguiente mensaje por pantalla:

   - Por favor, introduce un precio máximo.

### 2. Cancelar la Operación

El usuario puede cancelar el proceso en cualquier momento pulsando el botón **CANCELAR**, lo cual cerrará la vista sin guardar ningún dato.

### Posibles Errores y Mensajes

1. **Por favor, introduce un precio máximo**: Aparece cuando el campo **PRECIO MÁXIMO** está vacío.

## Quitar filtros y ordenaciones

1. Aplicar algún filtro u ordenación (consultar apartados anteriores).

2. Pulsar el icono de "X" que ha aparecido en el menú superior de la aplicación.

<img src="IMG/imagenQuitar.png" alt="Paso 3" width="200">

3. Confirmar la eliminación de filtros y ordenaciones pulsando en "ACEPTAR" en el menú de confirmación desplegado.

<img src="IMG/imagenQuitar2.png" alt="Paso 3" width="200">

## Novedades de Interfaz

### 1. Horario gasolineras

1. Hemos añadido a la interfaz donde aparecen las listas de las gasolineras el horario que tiene cada una en el día actual y además mostramos si la gasolinera está abierta o cerrada.

<img src="IMG/imagenHorarioGasolinera.png" alt="Paso 3" width="200">


### Mostrar los Precios de los Combustibles

1. Seleccionas la Gasolinera que quieras comprobar sus Precios


<img src="IMG/imagenClickarGasolinera.png" alt="Paso 3" width="200">

2. En la parte de abajo de los detalles de la Gasolinera aparecen los Precios de cada Combustible

<img src="IMG/imagenMuestraPreciosCombustibles.png" alt="Paso 3" width="200">
