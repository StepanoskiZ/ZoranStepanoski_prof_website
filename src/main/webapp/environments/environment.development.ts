declare const __ADMIN_SECRET__: string;

export const environment = {
  VERSION: 'DEV',
  DEBUG_INFO_ENABLED: true,
  isAdminEnv: true,
  ADMIN_SECRET: __ADMIN_SECRET__,
};
