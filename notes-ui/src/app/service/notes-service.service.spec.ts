import { TestBed } from '@angular/core/testing';

import { NotesService } from './notes-service.service';

describe('NotesServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NotesService = TestBed.get(NotesService);
    expect(service).toBeTruthy();
  });
});
