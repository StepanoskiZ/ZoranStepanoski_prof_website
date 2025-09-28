import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAboutMe } from 'app/entities/about-me/about-me.model';
import { AboutMeService } from 'app/entities/about-me/service/about-me.service';
import { AboutMeMediaService } from '../service/about-me-media.service';
import { IAboutMeMedia } from '../about-me-media.model';
import { AboutMeMediaFormService } from './about-me-media-form.service';

import { AboutMeMediaUpdateComponent } from './about-me-media-update.component';

describe('AboutMeMedia Management Update Component', () => {
  let comp: AboutMeMediaUpdateComponent;
  let fixture: ComponentFixture<AboutMeMediaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aboutMeMediaFormService: AboutMeMediaFormService;
  let aboutMeMediaService: AboutMeMediaService;
  let aboutMeService: AboutMeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AboutMeMediaUpdateComponent],
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
      .overrideTemplate(AboutMeMediaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AboutMeMediaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aboutMeMediaFormService = TestBed.inject(AboutMeMediaFormService);
    aboutMeMediaService = TestBed.inject(AboutMeMediaService);
    aboutMeService = TestBed.inject(AboutMeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AboutMe query and add missing value', () => {
      const aboutMeMedia: IAboutMeMedia = { id: 456 };
      const aboutMe: IAboutMe = { id: 11620 };
      aboutMeMedia.aboutMe = aboutMe;

      const aboutMeCollection: IAboutMe[] = [{ id: 12295 }];
      jest.spyOn(aboutMeService, 'query').mockReturnValue(of(new HttpResponse({ body: aboutMeCollection })));
      const additionalAboutMes = [aboutMe];
      const expectedCollection: IAboutMe[] = [...additionalAboutMes, ...aboutMeCollection];
      jest.spyOn(aboutMeService, 'addAboutMeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aboutMeMedia });
      comp.ngOnInit();

      expect(aboutMeService.query).toHaveBeenCalled();
      expect(aboutMeService.addAboutMeToCollectionIfMissing).toHaveBeenCalledWith(
        aboutMeCollection,
        ...additionalAboutMes.map(expect.objectContaining),
      );
      expect(comp.aboutMesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aboutMeMedia: IAboutMeMedia = { id: 456 };
      const aboutMe: IAboutMe = { id: 12496 };
      aboutMeMedia.aboutMe = aboutMe;

      activatedRoute.data = of({ aboutMeMedia });
      comp.ngOnInit();

      expect(comp.aboutMesSharedCollection).toContain(aboutMe);
      expect(comp.aboutMeMedia).toEqual(aboutMeMedia);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAboutMeMedia>>();
      const aboutMeMedia = { id: 123 };
      jest.spyOn(aboutMeMediaFormService, 'getAboutMeMedia').mockReturnValue(aboutMeMedia);
      jest.spyOn(aboutMeMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutMeMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aboutMeMedia }));
      saveSubject.complete();

      // THEN
      expect(aboutMeMediaFormService.getAboutMeMedia).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aboutMeMediaService.update).toHaveBeenCalledWith(expect.objectContaining(aboutMeMedia));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAboutMeMedia>>();
      const aboutMeMedia = { id: 123 };
      jest.spyOn(aboutMeMediaFormService, 'getAboutMeMedia').mockReturnValue({ id: null });
      jest.spyOn(aboutMeMediaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutMeMedia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aboutMeMedia }));
      saveSubject.complete();

      // THEN
      expect(aboutMeMediaFormService.getAboutMeMedia).toHaveBeenCalled();
      expect(aboutMeMediaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAboutMeMedia>>();
      const aboutMeMedia = { id: 123 };
      jest.spyOn(aboutMeMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutMeMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aboutMeMediaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAboutMe', () => {
      it('Should forward to aboutMeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(aboutMeService, 'compareAboutMe');
        comp.compareAboutMe(entity, entity2);
        expect(aboutMeService.compareAboutMe).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
