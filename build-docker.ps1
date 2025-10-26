# Script para construir la imagen Docker en PowerShell
Write-Host "🔨 Construyendo la aplicación..." -ForegroundColor Green
mvn clean package -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host "🐳 Construyendo imagen Docker..." -ForegroundColor Blue
    docker build -t paxtech-web-services .
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Imagen construida exitosamente!" -ForegroundColor Green
        Write-Host "📦 Nombre de la imagen: paxtech-web-services" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "🚀 Para ejecutar localmente:" -ForegroundColor Cyan
        Write-Host "docker run -p 8080:8080 -e DATABASE_URL='tu-url' -e DATABASE_USERNAME='tu-usuario' -e DATABASE_PASSWORD='tu-password' paxtech-web-services"
        Write-Host ""
        Write-Host "📤 Para subir a Render, usa el nombre: paxtech-web-services" -ForegroundColor Magenta
    } else {
        Write-Host "❌ Error construyendo la imagen Docker" -ForegroundColor Red
    }
} else {
    Write-Host "❌ Error construyendo la aplicación" -ForegroundColor Red
}

