import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AboutMeService } from '../service/about-me.service';
import { IAboutMe } from '../about-me.model';
import { AboutMeFormService } from './about-me-form.service';

import { AboutMeUpdateComponent } from './about-me-update.component';

describe('AboutMe Management Update Component', () => {
  let comp: AboutMeUpdateComponent;
  let fixture: ComponentFixture<AboutMeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aboutMeFormService: AboutMeFormService;
  let aboutMeService: AboutMeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AboutMeUpdateComponent],
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
      .overrideTemplate(AboutMeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AboutMeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aboutMeFormService = TestBed.inject(AboutMeFormService);
    aboutMeService = TestBed.inject(AboutMeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aboutMe: IAboutMe = { id: 456 };

      activatedRoute.data = of({ aboutMe });
      comp.ngOnInit();

      expect(comp.aboutMe).toEqual(aboutMe);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAboutMe>>();
      const aboutMe = { id: 123 };
      jest.spyOn(aboutMeFormService, 'getAboutMe').mockReturnValue(aboutMe);
      jest.spyOn(aboutMeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutMe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aboutMe }));
      saveSubject.complete();

      // THEN
      expect(aboutMeFormService.getAboutMe).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aboutMeService.update).toHaveBeenCalledWith(expect.objectContaining(aboutMe));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAboutMe>>();
      const aboutMe = { id: 123 };
      jest.spyOn(aboutMeFormService, 'getAboutMe').mockReturnValue({ id: null });
      jest.spyOn(aboutMeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutMe: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aboutMe }));
      saveSubject.complete();

      // THEN
      expect(aboutMeFormService.getAboutMe).toHaveBeenCalled();
      expect(aboutMeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAboutMe>>();
      const aboutMe = { id: 123 };
      jest.spyOn(aboutMeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutMe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aboutMeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
