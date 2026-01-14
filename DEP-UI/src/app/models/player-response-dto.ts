export interface PlayerResponseDto {
  id: number; 
  username: string;
  email: string;

  stats: Stats;
  inventory: Inventory;
  lastPosition: LastPosition;
}

export interface LastPosition {
  playerId: number;
  x: number;
  y: number;
  mapId: number;
  angle: number;
}

export interface Stats {
  id: number; 
  level: number;
  experience: number;
  honor: number;
  companyPoints: number;
}

export interface Inventory {
  id: number;
}