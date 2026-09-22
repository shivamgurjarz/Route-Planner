let cities = [];

async function refreshCities() {
  const res = await fetch('/api/cities');
  cities = await res.json();
  renderCityList();
  renderSelects();
  refreshRoads();
}

function renderCityList() {
  const el = document.getElementById('cityList');
  el.innerHTML = cities.map(c => `<span class="chip">${c}</span>`).join('');
}

function renderSelects() {
  const ids = ['fromCity', 'toCity', 'algoStart', 'dijkstraStart', 'dijkstraEnd'];
  ids.forEach(id => {
    const sel = document.getElementById(id);
    const current = sel.value;
    sel.innerHTML = cities.map(c => `<option value="${c}">${c}</option>`).join('');
    if (cities.includes(current)) sel.value = current;
  });
}

async function addCity() {
  const input = document.getElementById('cityInput');
  const name = input.value.trim();
  if (!name) return;
  await fetch('/api/cities', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'name=' + encodeURIComponent(name)
  });
  input.value = '';
  refreshCities();
}

async function refreshRoads() {
  const res = await fetch('/api/roads');
  const roads = await res.json();
  const tbody = document.querySelector('#roadsTable tbody');
  tbody.innerHTML = roads.map(r =>
    `<tr>
      <td>${r.from}</td><td>${r.to}</td><td>${r.distance}</td>
      <td><span class="remove-link" onclick="removeRoad('${r.from}','${r.to}')">remove</span></td>
    </tr>`
  ).join('');
}

async function addRoad() {
  const from = document.getElementById('fromCity').value;
  const to = document.getElementById('toCity').value;
  const distance = document.getElementById('distanceInput').value;
  if (!from || !to || !distance) return;
  if (from === to) { alert('Choose two different cities'); return; }
  await fetch('/api/roads', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}&distance=${encodeURIComponent(distance)}`
  });
  document.getElementById('distanceInput').value = '';
  refreshRoads();
}

async function removeRoad(from, to) {
  await fetch(`/api/roads?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}`, { method: 'DELETE' });
  refreshRoads();
}

async function runBfs() {
  const start = document.getElementById('algoStart').value;
  if (!start) return;
  const res = await fetch(`/api/bfs?start=${encodeURIComponent(start)}`);
  const data = await res.json();
  showResult(data.error ? data.error : 'BFS order: ' + data.order.join(' → '));
  refreshHistory();
}

async function runDfs() {
  const start = document.getElementById('algoStart').value;
  if (!start) return;
  const res = await fetch(`/api/dfs?start=${encodeURIComponent(start)}`);
  const data = await res.json();
  showResult(data.error ? data.error : 'DFS order: ' + data.order.join(' → '));
  refreshHistory();
}

async function runDijkstra() {
  const start = document.getElementById('dijkstraStart').value;
  const end = document.getElementById('dijkstraEnd').value;
  if (!start || !end) return;
  const res = await fetch(`/api/dijkstra?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}`);
  const data = await res.json();
  if (data.error) showResult(data.error);
  else showResult(`Shortest path: ${data.path.join(' → ')}\nTotal distance: ${data.distance} km`);
  refreshHistory();
}

function showResult(text) {
  document.getElementById('resultBox').textContent = text;
}

async function refreshHistory() {
  const res = await fetch('/api/history');
  const history = await res.json();
  document.getElementById('historyList').innerHTML = history.map(h => `<li>${h}</li>`).join('');
}

async function resetGraph() {
  if (!confirm('This will clear all cities and roads. Continue?')) return;
  await fetch('/api/reset', { method: 'POST' });
  showResult('Graph reset.');
  refreshCities();
  refreshHistory();
}

refreshCities();
refreshHistory();
