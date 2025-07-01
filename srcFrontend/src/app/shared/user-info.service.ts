import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {
  constructor(private http: HttpClient) {}

  getUserId() {
    return this.http.get<{ userId: number }>('http://localhost:8080/api/user/me');
  }
}
