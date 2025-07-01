import { Component, OnInit } from '@angular/core';
import { PrenotazioniService } from '../../../shared/prenotazioni.service';
import { KeycloakService } from 'keycloak-angular';
import {UserInfoService} from '../../../shared/user-info.service';

@Component({
  selector: 'app-washer',
  standalone: false,
  templateUrl: './washer.component.html',
  styleUrl: './washer.component.css'
})

export class WasherComponent implements OnInit {
  washerId!: number;
  from = '';
  to = '';
  appuntamenti: any[] = [];

  constructor(
    private service: PrenotazioniService,
    private userInfo: UserInfoService
  ) {}

  ngOnInit() {
    this.userInfo.getUserId().subscribe(data => {
      this.washerId = data.userId;
      this.loadAppuntamenti();
    });
  }

  inserisciDisponibilita() {
    const data = {
      washerId: this.washerId,
      availableFrom: this.from,
      availableTo: this.to
    };
    this.service.inserisciDisponibilità(data).subscribe();
  }

  loadAppuntamenti() {
    this.service.getAppuntamentiWasher(this.washerId).subscribe(res => this.appuntamenti = res);
  }
}
