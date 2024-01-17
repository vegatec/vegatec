function setupProxy({ tls }) {
  const serverResources = [
    '/api',
    '/services',
    '/management',
    '/v3/api-docs',
    '/h2-console',
    '/oauth2',
    '/login',
    '/auth',
    '/health',
    '/media',
    '/content',
    '/content/images',
  ];
  const conf = [
    {
      context: serverResources,
      target: `http${tls ? 's' : ''}://localhost:8081`,
      secure: false,
      changeOrigin: tls,
    },
    {
      context: ['/websocket'],
      target: 'ws://127.0.0.1:8081',
      ws: true,
    },
  ];
  return conf;
}

module.exports = setupProxy;
