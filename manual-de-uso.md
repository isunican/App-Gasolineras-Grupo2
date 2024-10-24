# Manual de uso

## Interacciones de Usuario

### 1. Añadir un Nuevo Punto de Interés

1. El usuario selecciona la opción **Añadir punto de interés** en el menú desplegable.

<img src="https://github.com/user-attachments/assets/5030fbcd-c801-4531-8e5b-835b563860b2" alt="Paso 1" width="400"/>


3. El usuario debe rellenar los siguientes campos:
   - **Nombre**: El nombre del nuevo punto de interés.
   - **Latitud**: La latitud en formato numérico (por ejemplo, `43.4623`).
   - **Longitud**: La longitud en formato numérico (por ejemplo, `-3.8099`).

<img src="https://github.com/user-attachments/assets/e7f5a18d-2407-4aa6-9da2-dfb5b98a314b" alt="Paso 3" width="400"/>


5. Después de introducir la información, el usuario debe pulsar el botón **Guardar** para almacenar el punto de interés en la base de datos.

6. Si algún campo está vacío o los valores numéricos son incorrectos, el sistema mostrará un mensaje de error:
   - "Por favor, llene todos los campos" si algún campo está vacío.
   - "Por favor, ingresa valores numéricos válidos para latitud y longitud" si los valores no son numéricos.

7. Si ya existe un punto de interés con el mismo nombre, se mostrará un mensaje:
   - "Ya existe un punto de interés con ese nombre".

8. Si todo es correcto, el sistema mostrará un mensaje de éxito:
   - "Punto de interés guardado".
   - A continuación, la vista se cerrará automáticamente.

### 2. Cancelar la Operación

El usuario puede cancelar el proceso en cualquier momento pulsando el botón **Cancelar**, lo cual cerrará la vista sin guardar ningún dato.

## Posibles Errores y Mensajes

- **Por favor, llene todos los campos**: Aparece cuando uno o más campos están vacíos.
- **Por favor, ingresa valores numéricos válidos para latitud y longitud**: Aparece cuando los valores introducidos para latitud o longitud no son numéricos.
- **Ya existe un punto de interés con ese nombre**: Aparece cuando el nombre del punto de interés ya está registrado en la base de datos.
- **Ha ocurrido un error en la base de datos**: Aparece cuando ocurre un error inesperado al acceder a la base de datos.

## Filtros sobre gasolineras

### 1. Ordenar gasolineras mas cercanas a un punto de interes.

1. El usuario pulsa el icono de filtrar gasolineras que aparece en la barra de opciones de la parte superior de la aplicacion.

<img src="https://github.com/user-attachments/assets/95adbb12-200c-46c1-b0cf-e0f41a16bf42" alt="Paso 3" width="400"/>

2. Aparece una ventana emergente donde se le muestra al usuario los puntos de interés. Una vez seleccionado pulsa el boton "ORDENAR".
   
<img src="https://github.com/user-attachments/assets/357e0296-fb1e-432b-8a8d-5d6ff09ac78e" alt="Paso 3" width="400"/>

3. Se le muestra al usuario la lista de gasolineras ordenada por cercanía al punto de interes solicitado.
   
<img src="https://github.com/user-attachments/assets/d18d3eb4-8efc-432d-a360-5a72ac7e1e7d" alt="Paso 3" width="400"/>

### 2. Cancelar la Operación

El usuario puede cancelar el proceso en cualquier momento pulsando el botón **Cancelar**, lo cual cerrará la vista sin guardar ningún dato.

## Posibles Errores y Mensajes

1. **Error: No hay ningun punto de interes añadido** En este caso el usuario tendra que pulsar el botón "CANCELAR" y añadir un punto de interés.
   
<img src="https://github.com/user-attachments/assets/0f670d9e-18f0-4559-8d5a-87739fbf74a5" alt="Paso 3" width="400"/>


