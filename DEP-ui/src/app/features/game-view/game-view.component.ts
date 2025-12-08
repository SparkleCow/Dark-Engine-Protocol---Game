import { Component, OnDestroy, OnInit } from '@angular/core';
import { Position } from '../../models/position';
import { Subscription } from 'rxjs';
import { WebsocketService } from '../../core/services/websocket.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-game-view',
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
  public readonly SHIP_ELEMENT_ID: string;

  // Local player state
  playerState: Position;

  
  // Estado de Otros Jugadores (ID -> Posición)
  otherShips: Map<number, Position> = new Map();
  
  // Control de Bucle y Red
  private keysPressed: { [key: string]: boolean } = {};
  private lastSendTime = 0;
  private syncSubscription!: Subscription;
  private animationFrameId!: number;

  constructor(public wsService: WebsocketService, private authService: AuthService) {
    // Inicialización de IDs y Estado
    this.PLAYER_ID = Number(this.authService.getPlayerId());
    this.SHIP_ELEMENT_ID = 'player-' + this.PLAYER_ID;
    
    this.playerState = {
      playerId: this.PLAYER_ID,
      x: 500, 
      y: 300, 
      angle: 0,
      mapId: 1
    };
  }

  ngOnInit(): void {
    // 1. Conectar y Suscribirse a la Sincronización
    this.wsService.connect(this.PLAYER_ID);
    this.syncSubscription = this.wsService.sync$.subscribe(positions => {
      this.handleGlobalSync(positions);
    });

    // 2. Iniciar manejo de teclado
    window.addEventListener('keydown', this.handleKeyDown.bind(this));
    window.addEventListener('keyup', this.handleKeyUp.bind(this));
    
    // 3. Iniciar el Bucle de Juego
    this.animationFrameId = window.requestAnimationFrame(this.gameLoop.bind(this));
  }

  ngOnDestroy(): void {
    // 4. Limpieza
    this.syncSubscription.unsubscribe();
    this.wsService.disconnect();
    window.removeEventListener('keydown', this.handleKeyDown.bind(this));
    window.removeEventListener('keyup', this.handleKeyUp.bind(this));
    window.cancelAnimationFrame(this.animationFrameId);
  }

  // --- Manejo de Entrada ---

  private handleKeyDown(e: KeyboardEvent) {
    this.keysPressed[e.code] = true;
  }

  private handleKeyUp(e: KeyboardEvent) {
    this.keysPressed[e.code] = false;
  }

  // --- Bucle de Juego (Tick) ---

  gameLoop(timestamp: number): void {
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

  // --- Manejo de Sincronización Global ---

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