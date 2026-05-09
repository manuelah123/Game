@echo off
echo Compilando Mario Bros...
if not exist bin mkdir bin
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt
if %errorlevel%==0 (
    echo.
    echo ==============================
    echo  Compilacion exitosa!
    echo  Ejecuta: run.bat
    echo ==============================
) else (
    echo ERROR en compilacion
)
