#!/bin/bash
echo "Compilando Mario Bros..."
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
if [ $? -eq 0 ]; then
    echo ""
    echo "=============================="
    echo " Compilacion exitosa!"
    echo " Ejecuta: ./run.sh"
    echo "=============================="
else
    echo "ERROR en compilacion"
fi
