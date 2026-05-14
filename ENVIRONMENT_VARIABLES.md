# Variables de Entorno para Despliegue

## Azure App Settings requeridas

- `DATASOURCE_URL`: URL completa de conexion JDBC a PostgreSQL.
- `JWT_SECRET`: Clave secreta segura para firmar tokens JWT.
- `SUPABASE_URL`: URL del proyecto Supabase.
- `SUPABASE_BUCKET`: Bucket usado para archivos de perfiles.
- `SUPABASE_SERVICE_ROLE_KEY`: Service role key de Supabase.
- `STRIPE_SECRET_KEY`: Secret key de Stripe.
- `STRIPE_WEBHOOK_SECRET`: Webhook signing secret de Stripe.

## Azure App Settings opcionales

- `PORT`: Puerto donde corre la aplicacion. Por defecto: `8080`.
- `JWT_EXPIRATION_DAYS`: Dias de expiracion de los tokens. Por defecto: `7`.
- `JPA_SHOW_SQL`: Muestra SQL en logs. Por defecto: `false`.
- `JPA_DDL_AUTO`: Estrategia de Hibernate DDL. Por defecto: `update`.
- `FLYWAY_ENABLED`: Habilita Flyway. Por defecto: `false`.
- `LOGGING_FILE_NAME`: Ruta del archivo de logs. Por defecto: `/home/LogFiles/Application/app.log`.
- `LOGGING_LEVEL_ROOT`: Nivel raiz de logs. Por defecto: `INFO`.

## Configuracion local

Los valores locales estan en `application-local.properties`, que esta ignorado por Git y no se empaqueta dentro del JAR.

Para correr localmente con ese archivo en PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE='local'; mvn spring-boot:run
```

En bash:

```bash
SPRING_PROFILES_ACTIVE=local mvn spring-boot:run
```
