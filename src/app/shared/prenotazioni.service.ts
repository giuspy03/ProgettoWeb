import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpHeaders, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PrenotazioniService {
  private baseUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) {}

  // USER
  creaPrenotazione(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/appointments`, data);
  }

  getPrenotazioniUtente(userId: number): Observable<any[]> {
    let params = new HttpParams().set('userId', userId);
    return this.http.get<any[]>(`${this.baseUrl}/appointments/washer`, { params });
  }

  // WASHER
  inserisciDisponibilità(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/availability/washer`, data);
  }

  getAppuntamentiWasher(washerId: number): Observable<any[]> {
    let params = new HttpParams().set('washerId', washerId);
    return this.http.get<any[]>(`${this.baseUrl}/appointmentsToDo/washer`, { params });
  }

  // ADMIN
  promuoviAdmin(username: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/promote/admin`, username, {
      headers: new HttpHeaders({ 'Content-Type': 'text/plain' })
    });
  }

  promuoviWasher(username: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/promote/washer`, username, {
      headers: new HttpHeaders({ 'Content-Type': 'text/plain' })
    });
  }

  declassaAdmin(username: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/revoke/admin`, username, {
      headers: new HttpHeaders({ 'Content-Type': 'text/plain' })
    });
  }

  declassaWasher(username: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/revoke/washer`, username, {
      headers: new HttpHeaders({ 'Content-Type': 'text/plain' })
    });
  }
  //lllllllllllllllllllllllllllll
  testBackend(): Observable<HttpResponse<any>> {
    return this.http.get<any>(`${this.baseUrl}/testBackend`, { observe: 'response' });
  }

}
