import { TestBed } from '@angular/core/testing';

import { PrenotazioniService } from './prenotazioni.service';

describe('Prenotazioni', () => {
  let service: PrenotazioniService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrenotazioniService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
