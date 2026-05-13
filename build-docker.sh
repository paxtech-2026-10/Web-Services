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
echo "docker run -p 8080:8080 -e DATABASE_URL='tu-url' -e DATABASE_USERNAME='tu-usuario' -e DATABASE_PASSWORD='tu-password' paxtech-web-services"
echo ""
echo "📤 Para subir a Render, usa el nombre: paxtech-web-services"

