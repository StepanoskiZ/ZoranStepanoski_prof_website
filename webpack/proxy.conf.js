function setupProxy({ tls }) {
  const serverResources = [
    //    {
    //      "/api": { "target": "http://localhost:8080", "secure": false },
    //      "/management": { "target": "http://localhost:8080", "secure": false },
    //      "/services": { "target": "http://localhost:8080", "secure": false },
    //      "/v3/api-docs": { "target": "http://localhost:8080", "secure": false },
    //      "/login": { "target": "http://localhost:8080", "secure": false },
    //      "/auth": { "target": "http://localhost:8080", "secure": false },
    //      "/health": { "target": "http://localhost:8080", "secure": false },
    //      "/h2-console": { "target": "http://localhost:8080", "secure": false }
    //    }
    '/api',
    '/services',
    '/management',
    '/v3/api-docs',
    '/h2-console',
    '/health',
    '/auth',
    '/login',
  ];
  return [
    {
      context: serverResources,

      // ===============================================================
      // !! IMPORTANT !!
      // The target should ALWAYS be your BACKEND server.
      // ===============================================================
      //     // For PRODUCTION administration (connecting to your live backend)
      //      target: 'https://zoranstepanoski-prof-api.fly.dev',
      //      secure: true, // Use true for 'https' URLs
      //      changeOrigin: true, // This is important for remote servers
      target: `http${tls ? 's' : ''}://localhost:8080`,
      secure: false, // Use true for 'https' URLs
      changeOrigin: tls,
    },
  ];
}

module.exports = setupProxy;

//{
//  "/api": {
//    "target": "https://zoranstepanoski-prof-api.fly.dev",
//    "secure": true,
//    "changeOrigin": true
//  },
//  "/management": {
//    "target": "https://zoranstepanoski-prof-api.fly.dev",
//    "secure": true,
//    "changeOrigin": true
//  },
//  // ... and so on for the other entries
//}
