function setupProxy({ tls }) {
  const serverResources = ['/api', '/services', '/management', '/v3/api-docs', '/h2-console', '/health', '/auth', '/login'];
  return [
    // We only need ONE rule: send API calls to the remote server.
    // The Angular dev server will handle everything else correctly by itself.
    {
      context: serverResources,
//      target: `http${tls ? 's' : ''}://localhost:4200`,
//      secure: false, // Use true for 'https' URLs
      target: 'https://zoranstepanoski-prof-api.fly.dev',
      secure: true,
      changeOrigin: true,
    },
  ];
}

module.exports = setupProxy;
