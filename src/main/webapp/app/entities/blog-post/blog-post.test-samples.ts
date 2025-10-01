import dayjs from 'dayjs/esm';

import { IBlogPost, NewBlogPost } from './blog-post.model';

export const sampleWithRequiredData: IBlogPost = {
  id: 2679,
  title: 'supposing ack to',
  contentHTML: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T00:39'),
};

export const sampleWithPartialData: IBlogPost = {
  id: 2902,
  title: 'ouch wrestler',
  contentHTML: '../fake-data/blob/hipster.txt',
  imageUrl: 'quicker',
  publishedDate: dayjs('2025-08-30T08:17'),
};

export const sampleWithFullData: IBlogPost = {
  id: 26856,
  title: 'breakable',
  contentHTML: '../fake-data/blob/hipster.txt',
  imageUrl: 'since',
  publishedDate: dayjs('2025-08-30T06:51'),
};

export const sampleWithNewData: NewBlogPost = {
  title: 'ghostwrite worthwhile weary',
  contentHTML: '../fake-data/blob/hipster.txt',
  publishedDate: dayjs('2025-08-30T01:00'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
