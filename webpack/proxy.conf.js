function setupProxy({ tls }) {
  const serverResources = ['/api', '/services', '/management', '/v3/api-docs', '/h2-console', '/health', '/auth', '/login'];
  return [
    {
      context: serverResources,

      // ===============================================================
      // !! IMPORTANT !!
      // The target should ALWAYS be your BACKEND server.
      // ===============================================================
      // For PRODUCTION administration (connecting to your live backend)
      target: 'https://zoranstepanoski-prof-api.fly.dev',
      secure: true, // Use true for 'https' URLs
      changeOrigin: true, // This is important for remote servers
//      target: `http${tls ? 's' : ''}://localhost:8080`,
//      secure: false, // Use true for 'https' URLs
//      changeOrigin: true,
    },
  ];
}

module.exports = setupProxy;
