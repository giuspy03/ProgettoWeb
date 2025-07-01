import { Component, OnInit } from '@angular/core';
import { PrenotazioniService } from '../../../shared/prenotazioni.service';
import { KeycloakService } from 'keycloak-angular';
import {UserInfoService} from '../../../shared/user-info.service';


@Component({
  selector: 'app-user',
  standalone: false,
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})

export class UserComponent implements OnInit {
  userId!: number;
  prenotazioni: any[] = [];
  prenotazione = { washerId: 0, dateTime: '' };

  constructor(
    private service: PrenotazioniService,
    private userInfo: UserInfoService
  ) {}

  ngOnInit() {
    this.userInfo.getUserId().subscribe(data => {
      this.userId = data.userId;
      this.loadPrenotazioni();
    });
  }

  prenota() {
    const data = {
      washerId: this.prenotazione.washerId,
      userId: this.userId,
      requestedDateTime: this.prenotazione.dateTime
    };

    this.service.creaPrenotazione(data).subscribe(() => this.loadPrenotazioni());
  }

  loadPrenotazioni() {
    this.service.getPrenotazioniUtente(this.userId).subscribe(res => this.prenotazioni = res);
  }
}
