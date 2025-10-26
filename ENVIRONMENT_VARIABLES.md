# Variables de Entorno para Despliegue

## Variables de Base de Datos
- `DATABASE_URL`: URL completa de conexión a PostgreSQL (ej: jdbc:postgresql://host:port/database)
- `DATABASE_USERNAME`: Usuario de la base de datos
- `DATABASE_PASSWORD`: Contraseña de la base de datos

## Variables de JWT
- `JWT_SECRET`: Clave secreta para firmar tokens JWT (debe ser segura)
- `JWT_EXPIRATION_DAYS`: Días de expiración de los tokens (por defecto: 7)

## Variables de Aplicación
- `SPRING_PROFILES_ACTIVE`: Perfil de Spring a usar (prod para producción)
- `JAVA_OPTS`: Opciones de JVM (ej: -Xmx512m -Xms256m)
- `PORT`: Puerto donde correrá la aplicación (por defecto: 8080)

## Configuración en Render
1. Ve a tu servicio en Render
2. Ve a la sección "Environment"
3. Agrega cada variable con su valor correspondiente
4. Reinicia el servicio
