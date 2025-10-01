import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of, throwError } from 'rxjs';

import { BusinessServiceDetailModalComponent, BusinessServiceDetail } from './business-service-detail-modal.component';

describe('BusinessServiceDetailModalComponent', () => {
  let component: BusinessServiceDetailModalComponent;
  let fixture: ComponentFixture<BusinessServiceDetailModalComponent>;
  let httpMock: HttpTestingController;
  let activeModal: NgbActiveModal;

  const mockServiceDetail: BusinessServiceDetail = {
    id: 1,
    title: 'Test Service',
    description: '<p>Test Description</p>',
    mediaFiles: [{ url: 'test.jpg', type: 'IMAGE', caption: 'Test Caption' }],
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BusinessServiceDetailModalComponent],
      providers: [NgbActiveModal],
    }).compileComponents();

    fixture = TestBed.createComponent(BusinessServiceDetailModalComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    activeModal = TestBed.inject(NgbActiveModal);
  });

  afterEach(() => {
    httpMock.verify(); // Make sure that there are no outstanding requests
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch service details on init and set component properties', fakeAsync(() => {
    // GIVEN
    component.item = { id: 1 };

    // WHEN
    fixture.detectChanges(); // Triggers ngOnInit
    tick(); // Process the async operations

    // THEN
    const req = httpMock.expectOne('/api/business-services/1/details');
    expect(req.request.method).toBe('GET');
    req.flush(mockServiceDetail); // Respond with mock data

    tick(); // Process the subscription

    expect(component.title).toBe('Test Service');
    expect(component.content).toBe('<p>Test Description</p>');
    expect(component.mediaUrls.length).toBe(1);
    expect(component.mediaUrls[0].url).toBe('test.jpg');
  }));

  it('should use a default image if mediaFiles is empty', fakeAsync(() => {
    // GIVEN
    component.item = { id: 1 };
    const detailWithoutMedia: BusinessServiceDetail = { ...mockServiceDetail, mediaFiles: [] };

    // WHEN
    fixture.detectChanges();
    tick();

    // THEN
    const req = httpMock.expectOne('/api/business-services/1/details');
    req.flush(detailWithoutMedia);
    tick();

    expect(component.mediaUrls.length).toBe(1);
    expect(component.mediaUrls[0].url).toBe('default-service.jpg');
  }));

  it('should close the modal if item ID is not provided', () => {
    // GIVEN
    jest.spyOn(activeModal, 'close');
    component.item = { id: null as any }; // Simulate no ID

    // WHEN
    fixture.detectChanges();

    // THEN
    expect(activeModal.close).toHaveBeenCalled();
  });

  it('should close the modal on API error', fakeAsync(() => {
    // GIVEN
    jest.spyOn(activeModal, 'close');
    component.item = { id: 1 };

    // WHEN
    fixture.detectChanges();
    tick();

    // THEN
    const req = httpMock.expectOne('/api/business-services/1/details');
    req.flush('Error', { status: 500, statusText: 'Internal Server Error' }); // Simulate an error
    tick();

    expect(activeModal.close).toHaveBeenCalled();
  }));
});
