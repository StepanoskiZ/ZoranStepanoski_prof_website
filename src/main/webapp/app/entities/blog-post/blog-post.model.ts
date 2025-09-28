import dayjs from 'dayjs/esm';

export interface IBlogPost {
  id: number;
  title?: string | null;
  contentHTML?: string | null;
  imageUrl?: string | null;
  publishedDate?: dayjs.Dayjs | null;
}

export type NewBlogPost = Omit<IBlogPost, 'id'> & { id: null };
