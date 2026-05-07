#!/bin/bash

# Script para construir la imagen Docker
echo "🔨 Construyendo la aplicación..."
mvn clean package -DskipTests

echo "🐳 Construyendo imagen Docker..."
docker build -t paxtech-web-services .

echo "✅ Imagen construida exitosamente!"
echo "📦 Nombre de la imagen: paxtech-web-services"
echo ""
echo "🚀 Para ejecutar localmente:"
echo "docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE='prod' -e DATASOURCE_URL='tu-url' -e DATASOURCE_USERNAME='tu-usuario' -e DATASOURCE_PASSWORD='tu-password' paxtech-web-services"
echo ""
echo "📤 Para subir a Render, usa el nombre: paxtech-web-services"

