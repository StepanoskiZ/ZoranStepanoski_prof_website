import dayjs from 'dayjs/esm';

import { IBlogPost, NewBlogPost } from './blog-post.model';

export const sampleWithRequiredData: IBlogPost = {
  id: 7084,
  title: 'loyal drat',
  contentHTML: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T17:41'),
};

export const sampleWithPartialData: IBlogPost = {
  id: 7121,
  title: 'nor hmph including',
  contentHTML: '../fake-data/blob/hipster.txt',
  imageUrl: 'yet since',
  publishedDate: dayjs('2025-08-30T18:42'),
};

export const sampleWithFullData: IBlogPost = {
  id: 24625,
  title: 'disafforest heartfelt',
  contentHTML: '../fake-data/blob/hipster.txt',
  imageUrl: 'yum',
  publishedDate: dayjs('2025-08-29T20:11'),
};

export const sampleWithNewData: NewBlogPost = {
  title: 'finally brr tug',
  contentHTML: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T13:08'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
