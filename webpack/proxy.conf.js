module.exports = setupProxy;
function setupProxy({ tls }) {
  const serverResources = [
    '/api',
    '/services',
    '/management',
    '/v3/api-docs',
    '/h2-console',
    '/health',
    '/auth',
    '/login'
  ];
  return [
    {
      context: serverResources,
      target: `http${tls ? 's' : ''}://localhost:8080`,
      secure: false,
      changeOrigin: tls,
    },
  ];
}

module.exports = setupProxy;
