import dayjs from 'dayjs/esm';

import { IBlogPost, NewBlogPost } from './blog-post.model';

export const sampleWithRequiredData: IBlogPost = {
  id: 13142,
  title: 'enormously yowza below',
  content: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T10:11'),
};

export const sampleWithPartialData: IBlogPost = {
  id: 19998,
  title: 'glider between',
  content: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T08:09'),
};

export const sampleWithFullData: IBlogPost = {
  id: 10904,
  title: 'well repeat hopelessly',
  content: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T02:11'),
};

export const sampleWithNewData: NewBlogPost = {
  title: 'with oh',
  content: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T10:56'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
