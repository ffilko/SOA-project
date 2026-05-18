//import { KeyPoint } from './key-point.model';

export interface Tour {
  id: string;
  authorId: string;
  name: string;
  description: string;
  difficulty: TourDifficulty;
  tags: string[];
}

export enum TourDifficulty {
  EASY = 'EASY',
  MEDIUM = 'MEDIUM',
  HARD = 'HARD'
}

export enum TourStatus {
  DRAFT = 'DRAFT',
  PUBLISHED = 'PUBLISHED',
  ARCHIVED = 'ARCHIVED'
}