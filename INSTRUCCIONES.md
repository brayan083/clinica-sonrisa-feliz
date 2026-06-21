# Clínica Sonrisa Feliz — Entrega 4 (Interfaz Gráfica Swing)

Sistema de gestión de una clínica odontológica con interfaz gráfica en **Java Swing**,
arquitectura en capas (modelo → repositorio → servicio → controlador → presentación),
persistencia en archivos CSV y manejo de excepciones de dominio.

## Requisitos

- **JDK 17 o superior** (el proyecto se desarrolló y probó con **JDK 25**).
- No requiere dependencias externas ni Maven/Gradle: usa solo la biblioteca estándar de Java.

## Ejecución rápida (JAR)

El archivo `dist/ClinicaSonrisaFeliz.jar` es ejecutable. Para correrlo:

- **Doble clic** sobre `ClinicaSonrisaFeliz.jar` (si el sistema tiene Java asociado a los `.jar`), **o**
- Desde la línea de comandos:

```bash
java -jar dist/ClinicaSonrisaFeliz.jar
```

> Importante: ejecutá el JAR desde la carpeta raíz del proyecto, ya que los datos
> se leen y escriben en la subcarpeta `datos/`.

## Compilar desde la línea de comandos

### Opción A — script automático (Linux/macOS)

```bash
./build.sh          # compila y genera el JAR
./build.sh run      # compila, genera el JAR y abre la GUI
./build.sh consola  # compila y abre la versión por consola
```

### Opción B — manual (cualquier sistema operativo)

```bash
# 1) Compilar todas las fuentes a la carpeta out/
javac -encoding UTF-8 -d out $(find src -name "*.java")

# 2) Crear el manifiesto y empaquetar el JAR
echo "Main-Class: clinicasonrisafeliz.Main" > out/MANIFEST.MF
jar cfm dist/ClinicaSonrisaFeliz.jar out/MANIFEST.MF -C out clinicasonrisafeliz

# 3) Ejecutar
java -jar dist/ClinicaSonrisaFeliz.jar
```

En Windows (PowerShell), reemplazá el paso 1 por:

```powershell
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName } > sources.txt
javac -encoding UTF-8 -d out "@sources.txt"
```

## Dos puntos de entrada

| Clase | Interfaz | Cómo ejecutar |
|-------|----------|---------------|
| `clinicasonrisafeliz.Main` | **Gráfica (Swing)** — Entrega 4 | `java -jar dist/ClinicaSonrisaFeliz.jar` |
| `clinicasonrisafeliz.MainConsola` | Consola — Entregas 1 a 3 | `java -cp out clinicasonrisafeliz.MainConsola` |

Ambas comparten la misma capa de servicios y persistencia.

## Acceso

Al iniciar, la aplicación carga los datos de `datos/` (o datos de prueba si no existen).
Iniciá sesión con un legajo de recepcionista existente —por ejemplo **`REC-001`**— o
registrá una nueva recepcionista desde la pantalla de acceso.

## Estructura de carpetas

```
clinica-sonrisa-feliz/
├── src/        Código fuente (paquete clinicasonrisafeliz)
├── out/        Clases compiladas (.class)
├── dist/       JAR ejecutable
├── datos/      Persistencia CSV (pacientes, odontólogos, turnos, recepcionistas)
├── uml/        Diagramas e informes
└── build.sh    Script de compilación/empaquetado
```
