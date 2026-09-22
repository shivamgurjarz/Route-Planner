# Smart Route Planner

A Java-based route planning system that models a network of cities as a graph
and finds routes between them using BFS, DFS, and Dijkstra's Algorithm.
Includes a browser-based frontend (HTML/CSS/JS) that talks to a small Java
REST API — no external dependencies or database required.

## Tech Stack

- **Backend:** Java 21 (uses only the built-in `com.sun.net.httpserver` — no
  frameworks, no external libraries)
- **Frontend:** HTML, CSS, JavaScript (calls the backend via `fetch`)
- **Data structures/algorithms:** Graph (adjacency list), BFS, DFS,
  Dijkstra's shortest path, Stack (route history)

## Project Structure

```
smart-route-planner/
├── src/
│   ├── Main.java     # HTTP server + REST endpoints
│   └── Graph.java    # Graph data structure and algorithms
├── public/
│   ├── index.html
│   ├── style.css
│   └── script.js
├── Dockerfile
└── README.md
```

## Run It Locally (no Docker needed)

You need a JDK (Java 17+) installed.

```bash
cd smart-route-planner
javac -d out src/*.java
java -cp out Main
```

Then open **http://localhost:8080** in your browser.

## Run It With Docker

```bash
cd smart-route-planner
docker build -t route-planner .
docker run -p 8080:8080 route-planner
```

Then open **http://localhost:8080**.

## Deploy It for Free (Render.com)

1. Push this folder to a GitHub repository (e.g. update your existing
   `Route-Planner` repo, or push as a new repo).
2. Go to [render.com](https://render.com) → New → **Web Service**.
3. Connect your GitHub repo.
4. Render will detect the `Dockerfile` automatically — choose **Docker** as
   the environment (no build/start command needed, it's in the Dockerfile).
5. Instance type: **Free**.
6. Click **Create Web Service**. Render will build and deploy automatically.
7. Once live, you'll get a URL like `https://route-planner-xxxx.onrender.com`
   — this is your live demo link for your resume.

> Note: Render's free tier spins the service down after inactivity, so the
> first request after idle time can take ~30–60 seconds to wake up. This is
> normal for free hosting and fine for a resume/portfolio link.

## Deploy It for Free (Railway.app) — alternative

1. Push the folder to GitHub.
2. Go to [railway.app](https://railway.app) → New Project → **Deploy from
   GitHub repo**.
3. Railway auto-detects the Dockerfile and builds it.
4. Add a public domain from the service's **Settings → Networking** tab.

## API Endpoints (for reference)

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/cities` | List all cities |
| POST | `/api/cities` | Add a city (`name=`) |
| GET | `/api/roads` | List all roads |
| POST | `/api/roads` | Add a road (`from=`, `to=`, `distance=`) |
| PUT | `/api/roads` | Update a road's distance |
| DELETE | `/api/roads?from=&to=` | Remove a road |
| GET | `/api/bfs?start=` | Run BFS from a city |
| GET | `/api/dfs?start=` | Run DFS from a city |
| GET | `/api/dijkstra?start=&end=` | Shortest path between two cities |
| GET | `/api/history` | Recent operations (stack) |
| POST | `/api/reset` | Clear all data |

## Author

Shivam Chauhan — B.Tech Computer Science & Engineering
