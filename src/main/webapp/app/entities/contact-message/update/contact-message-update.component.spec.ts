import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContactMessageService } from '../service/contact-message.service';
import { IContactMessage } from '../contact-message.model';
import { ContactMessageFormService } from './contact-message-form.service';

import { ContactMessageUpdateComponent } from './contact-message-update.component';

describe('ContactMessage Management Update Component', () => {
  let comp: ContactMessageUpdateComponent;
  let fixture: ComponentFixture<ContactMessageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contactMessageFormService: ContactMessageFormService;
  let contactMessageService: ContactMessageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ContactMessageUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ContactMessageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactMessageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactMessageFormService = TestBed.inject(ContactMessageFormService);
    contactMessageService = TestBed.inject(ContactMessageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contactMessage: IContactMessage = { id: 456 };

      activatedRoute.data = of({ contactMessage });
      comp.ngOnInit();

      expect(comp.contactMessage).toEqual(contactMessage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContactMessage>>();
      const contactMessage = { id: 123 };
      jest.spyOn(contactMessageFormService, 'getContactMessage').mockReturnValue(contactMessage);
      jest.spyOn(contactMessageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMessage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactMessage }));
      saveSubject.complete();

      // THEN
      expect(contactMessageFormService.getContactMessage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactMessageService.update).toHaveBeenCalledWith(expect.objectContaining(contactMessage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContactMessage>>();
      const contactMessage = { id: 123 };
      jest.spyOn(contactMessageFormService, 'getContactMessage').mockReturnValue({ id: null });
      jest.spyOn(contactMessageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMessage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactMessage }));
      saveSubject.complete();

      // THEN
      expect(contactMessageFormService.getContactMessage).toHaveBeenCalled();
      expect(contactMessageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContactMessage>>();
      const contactMessage = { id: 123 };
      jest.spyOn(contactMessageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactMessage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactMessageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
