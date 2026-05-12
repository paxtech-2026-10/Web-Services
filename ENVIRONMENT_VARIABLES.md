# Environment Variables

Spring Boot supports profile-specific configuration files such as
`application-dev.properties` and `application-prod.properties`. The active
profile can be selected with `SPRING_PROFILES_ACTIVE` or with the command-line
argument `--spring.profiles.active=prod`.

This project defaults to the `dev` profile for local development.

## Local Development

Default local values are defined in `src/main/resources/application-dev.properties`.
With a local PostgreSQL database named `utime_dev`, user `postgres`, and
password `postgres`, you can run the backend without extra variables:

```powershell
.\mvnw.cmd spring-boot:run
```

If your local PostgreSQL credentials are different, override them:

```powershell
$env:DATASOURCE_URL="jdbc:postgresql://localhost:5432/utime_dev"
$env:DATASOURCE_USERNAME="postgres"
$env:DATASOURCE_PASSWORD="postgres"
.\mvnw.cmd spring-boot:run
```

## Production

Activate the production profile:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
```

Required variables:

| Variable | Purpose |
|---|---|
| `SPRING_PROFILES_ACTIVE` | Use `prod` in production. |
| `PORT` | HTTP port. Defaults to `8080` when omitted. |
| `DATASOURCE_URL` | PostgreSQL JDBC URL, for example `jdbc:postgresql://host:5432/database`. |
| `DATASOURCE_USERNAME` | PostgreSQL username. |
| `DATASOURCE_PASSWORD` | PostgreSQL password. |
| `JWT_SECRET` | JWT signing secret. Must be at least 32 characters for HS256. |
| `JWT_EXPIRATION_DAYS` | Token expiration in days. Defaults to `7`. |
| `SUPABASE_URL` | Supabase project URL. |
| `SUPABASE_BUCKET` | Supabase storage bucket. |
| `SUPABASE_SERVICE_ROLE_KEY` | Supabase service role key. |
| `STRIPE_SECRET_KEY` | Stripe secret key. |
| `STRIPE_WEBHOOK_SECRET` | Stripe webhook signing secret. |
| `JPA_DDL_AUTO` | Hibernate schema mode. Defaults to `update`. |
| `LOGGING_FILE_NAME` | Optional log file path. Defaults to Azure App Service log path in prod. |

## Example Production Run

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DATASOURCE_URL="jdbc:postgresql://host:5432/utime"
$env:DATASOURCE_USERNAME="utime_user"
$env:DATASOURCE_PASSWORD="change-me"
$env:JWT_SECRET="replace-with-a-secure-secret-at-least-32-chars"
$env:SUPABASE_URL="https://example.supabase.co"
$env:SUPABASE_BUCKET="profiles"
$env:SUPABASE_SERVICE_ROLE_KEY="replace-me"
$env:STRIPE_SECRET_KEY="sk_live_or_test_key"
$env:STRIPE_WEBHOOK_SECRET="whsec_replace_me"
.\mvnw.cmd spring-boot:run
```

## Notes

- Use `DATASOURCE_*`, not `DATABASE_*`. The Spring configuration reads
  `DATASOURCE_URL`, `DATASOURCE_USERNAME`, and `DATASOURCE_PASSWORD`.
- Do not commit production secrets.
- For a safer production database workflow, replace `JPA_DDL_AUTO=update` with
  migrations and then use `JPA_DDL_AUTO=validate`.
