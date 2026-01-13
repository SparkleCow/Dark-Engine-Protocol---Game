import { Component, OnDestroy, OnInit } from '@angular/core';
import { Position } from '../../models/position';
import { Subscription } from 'rxjs';
import { WebsocketService } from '../../core/services/websocket.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { PlayerService} from '../../core/services/player.service';
import { PlayerResponseDto } from '../../models/player-response-dto';

@Component({
  selector: 'app-game-view',
  standalone: true, // Asumo que es un componente standalone
  imports: [CommonModule],
  templateUrl: './game-view.component.html',
  styleUrl: './game-view.component.css'
})
export class GameViewComponent implements OnInit, OnDestroy {

  // Constantes del juego
  private readonly SPEED = 5; // Pixeles por tick
  private readonly SEND_INTERVAL = 50; // Enviar la posición al servidor cada 50ms

  // Identificadores de Cliente
  public PLAYER_ID: number;
  public SHIP_ELEMENT_ID!: string;
  public PLAYER_USERNAME!: string; // Almacena el nombre del jugador para la UI

  // Local player state
  playerState!: Position; // Inicializado de forma asíncrona

  // Estado de Otros Jugadores (ID -> Posición)
  otherShips: Map<number, Position> = new Map();

  public isLoading: boolean = true;

  // Control de Bucle y Red
  private keysPressed: { [key: string]: boolean } = {};
  private lastSendTime = 0;
  private syncSubscription!: Subscription;
  private animationFrameId!: number;

  constructor(public wsService: WebsocketService,
              private authService: AuthService,
              private playerService: PlayerService) {

    // 1. Obtener ID del almacenamiento local/sesión
    this.PLAYER_ID = Number(this.authService.getPlayerId());
  }

  ngOnInit(): void {
    // 2. Iniciar el proceso de carga asíncrono y todo el juego
    this.loadInitialState();
  }

  loadInitialState(): void {
    // 3. Llamada REST para obtener el estado inicial y los datos del jugador
    this.playerService.$getPlayerInformation().subscribe({
      next: (data: PlayerResponseDto) => {
        this.PLAYER_USERNAME = data.username;
        const lastPos = data.lastPosition;

        // 4. ESTABLECER EL ESTADO LOCAL con la posición guardada
        this.playerState = {
          playerId: this.PLAYER_ID,
          x: lastPos.x,
          y: lastPos.y,
          angle: lastPos.angle,
          mapId: lastPos.mapId,
          username: data.username
        };
        this.SHIP_ELEMENT_ID = 'player-' + this.PLAYER_ID;

        // 5. INICIALIZAR WEB SOCKET Y BUCLE DE JUEGO

        // Conectar WebSocket
        this.wsService.connect(this.PLAYER_ID);
        this.syncSubscription = this.wsService.sync$.subscribe(positions => {
          this.handleGlobalSync(positions);
        });

        // Iniciar manejo de teclado
        window.addEventListener('keydown', this.handleKeyDown.bind(this));
        window.addEventListener('keyup', this.handleKeyUp.bind(this));

        // Iniciar el Bucle de Juego (Tick)
        this.animationFrameId = window.requestAnimationFrame(this.gameLoop.bind(this));

        // Marcar como cargado
        this.isLoading = false;

      },
      error: () => {
        console.error("Error loading initial player state");
        // Aquí puedes manejar la redirección o un mensaje de error
        this.isLoading = false;
      }
    });
  }

  ngOnDestroy(): void {
    // 4. Limpieza (siempre debe ejecutarse)
    if (this.syncSubscription) {
      this.syncSubscription.unsubscribe();
    }
    this.wsService.disconnect();
    window.removeEventListener('keydown', this.handleKeyDown.bind(this));
    window.removeEventListener('keyup', this.handleKeyUp.bind(this));
    window.cancelAnimationFrame(this.animationFrameId);
  }

  // --- Manejo de Entrada (sin cambios) ---

  private handleKeyDown(e: KeyboardEvent) {
    this.keysPressed[e.code] = true;
  }

  private handleKeyUp(e: KeyboardEvent) {
    this.keysPressed[e.code] = false;
  }

  // --- Bucle de Juego (Tick) ---

  gameLoop(timestamp: number): void {

    // Verificación de seguridad: solo ejecutar si el estado está listo
    if (this.isLoading || !this.playerState) {
        this.animationFrameId = window.requestAnimationFrame(this.gameLoop.bind(this));
        return;
    }

    let moved = false;

    // 1. Actualización de posición local
    if (this.keysPressed['ArrowUp'] || this.keysPressed['KeyW']) {
      this.playerState.y -= this.SPEED;
      moved = true;
    }
    if (this.keysPressed['ArrowDown'] || this.keysPressed['KeyS']) {
      this.playerState.y += this.SPEED;
      moved = true;
    }
    if (this.keysPressed['ArrowLeft'] || this.keysPressed['KeyA']) {
      this.playerState.x -= this.SPEED;
      moved = true;
    }
    if (this.keysPressed['ArrowRight'] || this.keysPressed['KeyD']) {
      this.playerState.x += this.SPEED;
      moved = true;
    }

    // 2. Enviar posición al Servidor (si es hora)
    if (moved && timestamp - this.lastSendTime > this.SEND_INTERVAL) {
      this.wsService.sendMovement(this.playerState);
      this.lastSendTime = timestamp;
    }

    // 3. Continuar el bucle
    this.animationFrameId = window.requestAnimationFrame(this.gameLoop.bind(this));
  }

  // --- Manejo de Sincronización Global (sin cambios) ---

  handleGlobalSync(positions: Position[]): void {
    const activeIds = new Set(positions.map(p => p.playerId));

    // 1. Actualizar/Crear naves remotas
    positions.forEach(pos => {
      if (pos.playerId === this.PLAYER_ID) {
        return; // Ignora tu propia nave
      }

      // Actualiza o añade la nave al mapa
      this.otherShips.set(pos.playerId, pos);
    });

    // 2. Limpieza: Eliminar naves de jugadores desconectados
    this.otherShips.forEach((ship, id) => {
      if (!activeIds.has(id)) {
        this.otherShips.delete(id);
      }
    });
  }
}
