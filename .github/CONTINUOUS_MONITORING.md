# 7.4 Continuous Monitoring — Implementación real (GitHub Actions)

Este backend implementa el capítulo **7.4 Continuous Monitoring** usando
**GitHub Actions** como motor de pipeline. No se usa Jenkins: GitHub Actions es
el sustituto más simple, gratuito y demostrable que el equipo ya emplea para
CI/CD, y permite "ver los componentes" del pipeline y "conectar con Gmail para
enviar correo", que es el objetivo de la sección.

## Mapeo con la documentación

| Subsección | Qué se implementó | Dónde |
|---|---|---|
| **7.4.1 Tools and Practices** | GitHub Actions (pipeline), `curl` (sonda HTTP), Gmail SMTP (correo). Práctica: monitoreo nativo de plataforma + health-check programado. | Este repo |
| **7.4.2 Monitoring Pipeline Components** | Workflow programado (`monitoring.yml`) que cada 15 min sondea la salud del backend en producción + los jobs `test/build/deploy` visibles en la pestaña **Actions**. | `.github/workflows/monitoring.yml` |
| **7.4.3 Alerting Pipeline Components** | Job `alert` que se dispara **solo si** el health-check falla; job `notify` que detecta fallo de cualquier etapa del pipeline de despliegue. Es la señal de *"algo está mal"*. | `monitoring.yml` (job `alert`), `main_paxtech.yml` (job `notify`) |
| **7.4.4 Notification Pipeline Components** | Envío de **correo vía Gmail SMTP** (`dawidd6/action-send-mail`) tanto en caída del backend como en resultado del pipeline (éxito = notificación, fallo = alerta). | Ambos workflows |

> **Notificación vs. Alerta** (distinción que pidió el profesor):
> - **Notificación** = mensaje informativo del estado de una ejecución (ej. "despliegue exitoso").
> - **Alerta** = señal de que *algo está mal* y requiere ser investigado (backend caído o pipeline fallido).

## Configuración requerida (una sola vez)

En GitHub: **Settings → Secrets and variables → Actions**.

### Variables (pestaña *Variables*)
| Nombre | Valor de ejemplo |
|---|---|
| `BACKEND_URL` | `https://paxtech.azurewebsites.net` (URL pública real del App Service) |

### Secrets (pestaña *Secrets*)
| Nombre | Valor |
|---|---|
| `MAIL_USERNAME` | Cuenta Gmail que envía, ej. `gaelrszk@gmail.com` |
| `MAIL_PASSWORD` | **App Password** de 16 caracteres (NO la contraseña normal) |
| `MAIL_TO` | Destinatario(s), separados por coma |

### Cómo obtener el App Password de Gmail
1. Activar **verificación en 2 pasos** en la cuenta Google (requisito).
2. Ir a **Cuenta de Google → Seguridad → Contraseñas de aplicaciones**.
3. Crear una nueva (nombre: "uTime CI") y copiar los 16 caracteres → ese valor va en `MAIL_PASSWORD`.

## Cómo demostrarlo
- **Monitoreo:** pestaña **Actions → Continuous Monitoring** → botón **Run workflow** (o esperar el cron). Si el backend responde, el run queda en verde.
- **Alerta + correo:** poner temporalmente un `BACKEND_URL` inválido y ejecutar el workflow → el job `alert` se ejecuta y llega un correo `[ALERTA - CRITICA]`.
- **Notificación de pipeline:** un `push` a `main` ejecuta `main_paxtech.yml`; al terminar llega un correo `[OK]` (o `[ALERTA]` si algo falló).
