import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

import { Position } from '../../models/position';
import { MonsterSnapshot } from '../../models/monster-snapshot';
import { WorldSnapshot } from '../../models/world-snapshot';

import { WebsocketService } from '../../core/services/websocket.service';
import { AuthService } from '../../core/services/auth.service';
import { PlayerService } from '../../core/services/player.service';
import { PlayerResponseDto, Stats } from '../../models/player-response-dto';

@Component({
  selector: 'app-game-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './game-view.component.html',
  styleUrl: './game-view.component.css',
})
export class GameViewComponent implements OnInit, OnDestroy {
  WORLD_WIDTH = 5000;
  WORLD_HEIGHT = 5000;

  VIEWPORT_WIDTH = window.innerWidth;
  VIEWPORT_HEIGHT = window.innerHeight;

  private readonly SPEED = 5;
  private readonly SEND_INTERVAL = 50;

  PLAYER_ID!: number;
  PLAYER_USERNAME!: string;

  playerState!: Position;
  playerInformation!: PlayerResponseDto;

  otherShips: Map<string, Position> = new Map();
  monsters: Map<string, MonsterSnapshot> = new Map();

  cameraX = 0;
  cameraY = 0;

  isLoading = true;

  private keysPressed: Record<string, boolean> = {};
  private lastSendTime = 0;
  private animationFrameId!: number;

  private worldSubscription!: Subscription;
  private statsSubscription!: Subscription;

  constructor(
    public wsService: WebsocketService,
    private authService: AuthService,
    private playerService: PlayerService,
  ) {}

  ngOnInit(): void {
    this.loadInitialState();
    window.addEventListener('keydown', this.handleKeyDown);
    window.addEventListener('keyup', this.handleKeyUp);
  }

  ngOnDestroy(): void {
    this.wsService.disconnect();
    this.worldSubscription?.unsubscribe();
    this.statsSubscription?.unsubscribe();

    cancelAnimationFrame(this.animationFrameId);

    window.removeEventListener('keydown', this.handleKeyDown);
    window.removeEventListener('keyup', this.handleKeyUp);
  }

  loadInitialState(): void {
    this.playerService.$getPlayerInformation().subscribe({
      next: (data: PlayerResponseDto) => {
        this.playerInformation = data;

        this.PLAYER_USERNAME = data.username;
        this.PLAYER_ID = data.id;

        this.playerState = {
          x: data.lastPosition.x,
          y: data.lastPosition.y,
          angle: data.lastPosition.angle,
          mapId: data.lastPosition.mapId,
          username: this.PLAYER_USERNAME,
        };

        this.wsService.connect(this.PLAYER_USERNAME);
        this.worldSubscription = this.wsService.world$.subscribe((w) =>
          this.handleWorldSnapshot(w),
        );

        console.log(this.PLAYER_USERNAME);
        this.statsSubscription = this.wsService.stats$.subscribe((stats) => {
          this.playerInformation.stats = stats;
          console.log(this.playerInformation.stats);
        });

        this.animationFrameId = requestAnimationFrame(this.gameLoop);
        this.isLoading = false;
      },
    });
  }

  gameLoop = (timestamp: number) => {
    if (!this.playerState) {
      this.animationFrameId = requestAnimationFrame(this.gameLoop);
      return;
    }

    let moved = false;

    if (this.keysPressed['KeyW'] || this.keysPressed['ArrowUp']) {
      this.playerState.y -= this.SPEED;
      moved = true;
    }
    if (this.keysPressed['KeyS'] || this.keysPressed['ArrowDown']) {
      this.playerState.y += this.SPEED;
      moved = true;
    }
    if (this.keysPressed['KeyA'] || this.keysPressed['ArrowLeft']) {
      this.playerState.x -= this.SPEED;
      moved = true;
    }
    if (this.keysPressed['KeyD'] || this.keysPressed['ArrowRight']) {
      this.playerState.x += this.SPEED;
      moved = true;
    }

    this.clampPlayer();
    this.updateCamera();

    if (moved && timestamp - this.lastSendTime > this.SEND_INTERVAL) {
      this.wsService.sendMovement(this.playerState);
      this.lastSendTime = timestamp;
    }

    this.animationFrameId = requestAnimationFrame(this.gameLoop);
  };

  updateCamera(): void {
    this.cameraX = this.VIEWPORT_WIDTH / 2 - this.playerState.x;
    this.cameraY = this.VIEWPORT_HEIGHT / 2 - this.playerState.y;
  }

  clampPlayer(): void {
    this.playerState.x = Math.max(
      0,
      Math.min(this.WORLD_WIDTH, this.playerState.x),
    );
    this.playerState.y = Math.max(
      0,
      Math.min(this.WORLD_HEIGHT, this.playerState.y),
    );
  }

  handleWorldSnapshot(snapshot: WorldSnapshot): void {
    const activeIds = new Set(snapshot.players.map((p) => p.username));

    snapshot.players.forEach((p) => {
      if (p.username !== this.PLAYER_USERNAME) {
        this.otherShips.set(p.username, p);
      }
    });

    [...this.otherShips.keys()].forEach((id) => {
      if (!activeIds.has(id)) this.otherShips.delete(id);
    });

    // Monsters
    this.monsters.clear();
    snapshot.monsters.forEach((m) => {
      if (m.alive) {
        this.monsters.set(m.id, m);
      }
    });
  }

  attackMonster(monsterId: string): void {
    this.wsService.sendAttack(monsterId);
    console.log(`Attacked monster with ID: ${monsterId}`);
  }

  onMonsterHover(id: string) {
    console.log('ENTER MONSTER', id);
  }

  handleKeyDown = (e: KeyboardEvent) => (this.keysPressed[e.code] = true);
  handleKeyUp = (e: KeyboardEvent) => (this.keysPressed[e.code] = false);
}
