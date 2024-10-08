function setupProxy({ tls }) {
  const serverResources = ['/api', '/services', '/management', '/v3/api-docs', '/h2-console', '/oauth2', '/login', '/auth', '/health'];
  const conf = [
    {
      context: serverResources,
      // target: `http${tls ? 's' : ''}://localhost:8081`,
      target: `https://localhost:8081`,
      secure: false,
      changeOrigin: tls,
    },
  ];
  return conf;
}

module.exports = setupProxy;
