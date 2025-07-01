import { Component } from '@angular/core';
import { PrenotazioniService } from '../../../shared/prenotazioni.service';
import {HttpResponse} from '@angular/common/http';



@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})

export class AdminComponent {
  username = '';


  testResult: boolean | null = null;
  statusCode: number | null = null;


  constructor(private service: PrenotazioniService) {}

  promuovi(tipo: 'washer' | 'admin') {
    const metodo = tipo === 'washer' ? this.service.promuoviWasher : this.service.promuoviAdmin;
    metodo.call(this.service, this.username).subscribe();
  }

  declassa(tipo: 'washer' | 'admin') {
    const metodo = tipo === 'washer' ? this.service.declassaWasher : this.service.declassaAdmin;
    metodo.call(this.service, this.username).subscribe();
  }

  testBackend() {
    this.service.testBackend().subscribe({
      next: (response: HttpResponse<any>) => {
        this.testResult = true;
        this.statusCode = response.status;  // <-- salva lo status code
      },
      error: (error) => {
        this.testResult = false;
        this.statusCode = error.status;     // <-- salva lo status code in caso di errore
      }
    });
  }


}
