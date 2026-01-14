import { Position } from './position';
import { MonsterSnapshot } from './monster-snapshot';

export interface WorldSnapshot {
  timestamp: number;
  players: Position[];
  monsters: MonsterSnapshot[];
}
