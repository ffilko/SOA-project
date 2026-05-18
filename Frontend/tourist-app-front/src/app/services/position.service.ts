import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Position } from '../models/position.model';

@Injectable({ providedIn: 'root' })
export class PositionService {
  private apiUrl = 'http://localhost:9000/api/positions';

  constructor(private http: HttpClient) {}

  upsert(position: Position): Observable<Position> {
    return this.http.put<Position>(this.apiUrl, position);
  }

  getByTourist(touristId: string): Observable<Position | null> {
    return this.http.get<Position>(`${this.apiUrl}/${touristId}`).pipe(
      catchError(() => of(null))
    );
  }
}