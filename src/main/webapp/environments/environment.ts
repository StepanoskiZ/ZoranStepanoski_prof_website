declare const __ADMIN_SECRET__: string;

export const environment = {
  VERSION: __VERSION__,
  DEBUG_INFO_ENABLED: false,
  isAdminEnv: false,
  ADMIN_SECRET: __ADMIN_SECRET__,
};
