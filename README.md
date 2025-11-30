Evaluación 3 - PetsOnlineSPA

1. Caso elegido y alcance

Caso: PetsOnlineSPA (Cliente, Productos, Servicios Veterinarios, Reservas y Mascotas).

Alcance: Esta entrega final incorpora una arquitectura completa con Backend propio, Base de datos en la nube, consumo de API externa, pruebas unitarias y generación de APK firmado para distribución.

2. Requisitos y ejecución

Stack Tecnológico:
- ViewModel + StateFlow para gestión de estado
- Retrofit + Coroutines para conexión con Backend
- JUnit4 + Mockk para Pruebas Unitarias
- Backend propio en Render (Node.js) + MongoDB Atlas

Instalación:
1. Clonar el repositorio:
   git clone https://github.com/ladbrone/Evaluacion3_PetsOnline.git
2. Abrir el proyecto en Android Studio.
3. Esperar la sincronización de Gradle.
4. Conectar un dispositivo físico o emulador.

Ejecución:
- Ejecutar con el botón Run app en Android Studio.
- O instalar directamente el archivo app-release.apk incluido en la carpeta app/release.

3. Arquitectura y flujo

Estructura de carpetas:
```text
data/
 ┣ local/          
 ┣ remote/         
 ┣ repository/     
domain/
 ┗ model/
navigation/
root/    
ui/
 ┣ components/        
 ┣ screens/
 ┣ theme/       
 ┗ viewmodels/
utils/
```
 Navegación: Implementada con NavHost. Flujo principal: Login → Home (con API Externa) → Perfil (con datos Backend) / Funcionalidades.

4. Funcionalidades Nuevas (Evaluación 4)

Backend Propio y Base de Datos:
- Despliegue de microservicio REST en Render.
- Conexión a base de datos MongoDB Atlas en la nube.
- Autenticación real (Login y Registro) validada contra el servidor.

API Externa:
- Integración con "Dog CEO API" en la pantalla Home.
- Muestra una imagen aleatoria ("Mascota destacada") al iniciar la app.

Pruebas Unitarias:
- Tests implementados en LoginViewModelTest.
- Validación de lógica de negocio (email inválido, campos vacíos) y simulación de éxito con Mockk.

APK Firmado:
- Generación de app-release.apk firmado digitalmente con petsonline-key.jks.
- Configurado para release (sin logs de debug en producción).

Persistencia y Sesión:
- Manejo seguro del Token JWT recibido desde el backend.

5. Endpoints Utilizados

Backend Propio (Render): Base URL: https://petsonline-backend.onrender.com/api/

Método: POST
- Ruta: /auth/login
- Body: { email, password }
- Respuesta: Token de acceso + Datos de usuario.

Método: POST
- Ruta: /auth/signup
- Body: { email, password }
- Respuesta: Creación de usuario y token.

Método: GET
- Ruta: /auth/profile
- Header: Authorization: Bearer <TOKEN>
- Respuesta: Datos del perfil del usuario autenticado.

API Externa (Dog CEO): Base URL: https://dog.ceo/api/

Método: GET
- Ruta: breeds/image/random
- Respuesta: URL de imagen aleatoria de perro.

6. User flows

Flujo de Autenticación:
- Usuario ingresa credenciales en Login.
- App consulta a Render.
- Si es correcto, guarda el Token y navega al Home.
- En Home, se carga automáticamente la "Mascota del día" desde la API externa.

Flujo de Perfil:
- Usuario entra a "Ver Perfil".
- App usa el Token guardado para consultar /auth/profile.
- Se muestra el correo real del usuario (admin@sistema.com).
- Al cerrar sesión, se borra el token y se vuelve al Login.
