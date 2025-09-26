import dayjs from 'dayjs/esm';

import { IBlogPost, NewBlogPost } from './blog-post.model';

export const sampleWithRequiredData: IBlogPost = {
  id: 21474,
  title: 'whenever uh-huh perfection',
  content: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-29T19:09'),
};

export const sampleWithPartialData: IBlogPost = {
  id: 21279,
  title: 'showy',
  content: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T16:16'),
};

export const sampleWithFullData: IBlogPost = {
  id: 3965,
  title: 'helpfully pantology',
  content: '../fake-data/blob/hipster.txt',
  imageUrl: 'drum where broaden',
  publishedDate: dayjs('2025-08-30T07:25'),
};

export const sampleWithNewData: NewBlogPost = {
  title: 'experienced duh dangerous',
  content: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T05:09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
