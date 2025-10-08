//function setupProxy({ tls }) {
//  const serverResources = ['/api', '/services', '/management', '/v3/api-docs', '/h2-console', '/health', '/auth', '/login'];
//  return [
//    // We only need ONE rule: send API calls to the remote server.
//    // The Angular dev server will handle everything else correctly by itself.
//    {
//      context: serverResources,
////      target: `http${tls ? 's' : ''}://localhost:4200`,
////      secure: false,
//      target: 'https://zoranstepanoski-prof-api.fly.dev',
//      secure: true,
//      changeOrigin: true,
//    },
//  ];
//}
//
//module.exports = setupProxy;

// This is the NEW and CORRECT configuration
function setupProxy({ tls }) {
  const serverResources = ['/api', '/services', '/management', '/v3/api-docs', '/h2-console', '/health', '/auth', '/login'];
  return [
    {
      context: serverResources,
//      target: `http${tls ? 's' : ''}://localhost:4200`,
//      secure: false,
      target: 'https://zoranstepanoski-prof-api-yqlyfjxyeq-lm.a.run.app',
      secure: true, // Use secure: true because the GCP URL is HTTPS
      changeOrigin: true, // Important for avoiding CORS issues
    },
  ];
}

module.exports = setupProxy;
