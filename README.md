# 🍄 Mario Bros — Java + Web

## Arquitectura

```
JAVA (lógica 100%)          JS (vista 100%)
──────────────────          ───────────────
Física / colisiones    →    Pinta canvas
Animación de frames    →    Lee /state JSON
Progresión / candados  →    Muestra overlays
Estado del juego       →    Envía teclas
Patrones de diseño     →    Música Web Audio
```

## Patrones implementados

| Patrón | Clase |
|--------|-------|
| **Builder** | `NivelBuilder` + `DirectorNiveles` |
| **Factory** | `FabricaEnemigos` |
| **Singleton** | `ScoreManager`, `ProgresionJuego` |
| **State** | `EstadoMenu`, `EstadoJugando`, `EstadoNivelCompletado`, `EstadoJuegoCompletado`, `EstadoGameOver` |
| **Observer** | `SujetoPartida` + `ObservadorPartida` |
| **Facade** | `FachadaJuego` |
| **Adapter** | `AdaptadorTecladoWeb`, `ServidorWebJuego` |

## Cómo ejecutar

### Windows
```bat
compile.bat
run.bat
```

### Linux / Mac
```bash
chmod +x compile.sh run.sh
./compile.sh
./run.sh
```

### Manual
```bash
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
java -cp bin Main
```

Abrir **http://localhost:8080** en el navegador.

## Controles
- `← →` Moverse
- `↑` / `SPACE` Saltar
- `P` Pausar

## Niveles
1. **Pradera Feliz** — disponible desde el inicio
2. **Cueva Oscura** — se desbloquea al completar nivel 1
3. **Castillo Final** — se desbloquea al completar nivel 2

## Correcciones aplicadas

- **Bug salto**: Detector usa `prevY` para no perder colisiones a alta velocidad
- **Candados**: `ProgresionJuego` (Singleton) gestiona niveles desbloqueados
- **Puntaje acumulado**: Aparece en el menú principal
- **Juego completado**: Pantalla especial al superar el nivel 3
- **Tortuga color**: Corregido `@00a800` → `#00a800`
- **Scroll cámara**: `cameraX` con lerp suave en JS
- **Música**: Web Audio API procedural 8-bit

# Autor
**Manuela Henao Bedoya**

# Licencia
**Este proyecto está bajo la licencia MIT.**
