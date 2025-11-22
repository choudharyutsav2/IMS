// frontend/script.js - simple test fetch to check backend connectivity
(async function(){
  const API = window.APP_CONFIG && window.APP_CONFIG.API_BASE ? window.APP_CONFIG.API_BASE : '';
  if (!API) {
    console.warn('API base not set. Check frontend/config.js');
    return;
  }

  console.log('Testing API base:', API);

  // Try a simple GET. Adjust path if your API has a specific health endpoint.
  const testPaths = ['','/','/health','/api','/api/health','/products','/items']; // tries several common endpoints
  for (const p of testPaths) {
    try {
      const url = API.replace(/\/$/, '') + (p ? (p.startsWith('/') ? p : '/' + p) : '');
      console.log('Trying', url);
      const res = await fetch(url, { method: 'GET', credentials: 'include' });
      console.log('Response from', url, res.status, res.statusText);
      try {
        const json = await res.json().catch(()=>null);
        if (json) console.log('JSON:', json);
      } catch(e) {}
      // stop after first successful (2xx) response
      if (res.ok) break;
    } catch (err) {
      console.warn('Error fetching', p, err.message);
    }
  }
})();
