#!/usr/bin/env bash
# ──────────────────────────────────────────────────────────────────────────
# Script de compilación y empaquetado — Clínica Sonrisa Feliz (Entrega 4)
#
# Uso:
#   ./build.sh          → compila el código y genera el JAR ejecutable
#   ./build.sh run      → compila, genera el JAR y lanza la aplicación (GUI)
#   ./build.sh consola  → compila y ejecuta la versión por consola
#
# Requisitos: JDK 17 o superior (probado con JDK 25).
# ──────────────────────────────────────────────────────────────────────────
set -e

DIR_SALIDA="out"
DIR_DIST="dist"
JAR="$DIR_DIST/ClinicaSonrisaFeliz.jar"
CLASE_MAIN="clinicasonrisafeliz.Main"
CLASE_CONSOLA="clinicasonrisafeliz.MainConsola"

echo "▶ Compilando fuentes…"
rm -rf "$DIR_SALIDA"
mkdir -p "$DIR_SALIDA" "$DIR_DIST"
find src -name "*.java" > /tmp/clinica_sources.txt
javac -encoding UTF-8 -d "$DIR_SALIDA" @/tmp/clinica_sources.txt
echo "✓ Compilación correcta."

echo "▶ Generando JAR ejecutable…"
printf 'Main-Class: %s\n' "$CLASE_MAIN" > "$DIR_SALIDA/MANIFEST.MF"
jar cfm "$JAR" "$DIR_SALIDA/MANIFEST.MF" -C "$DIR_SALIDA" clinicasonrisafeliz
echo "✓ JAR generado en $JAR"

case "$1" in
  run)
    echo "▶ Lanzando interfaz gráfica…"
    java -jar "$JAR"
    ;;
  consola)
    echo "▶ Lanzando interfaz por consola…"
    java -cp "$DIR_SALIDA" "$CLASE_CONSOLA"
    ;;
esac
