import { StrictMode, useEffect, useState } from "react";
import { createRoot } from "react-dom/client";
import { getCurrentUser, getHealth, googleLoginUrl, type CurrentUser, type HealthResponse } from "./api";
import "./styles.css";

function App() {
  const [health, setHealth] = useState<HealthResponse | null>(null);
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getHealth().then(setHealth).catch((e: unknown) => setError(e instanceof Error ? e.message : "Unable to reach API."));
    getCurrentUser().then(setUser);
  }, []);

  return (
    <main>
      <section className="shell">
        <p className="eyebrow">OPENWEB / FOUNDATION</p>
        <h1>Build your corner of the web.</h1>
        <p className="description">Create, publish, discover, and browse a new generation of user-created websites.</p>
        <div className="status" aria-live="polite">
          <span className={health ? "indicator online" : "indicator"} />
          <div><strong>{health ? "API connected" : "Connecting to API"}</strong><span>{error ?? (health ? health.service : "localhost:8080")}</span></div>
        </div>
        <div className="auth-panel">
          {user ? (
            <div><strong>Signed in as {user.displayName}</strong><span>{user.email}</span></div>
          ) : (
            <a className="google-button" href={googleLoginUrl}>Continue with Google</a>
          )}
        </div>
      </section>
    </main>
  );
}

createRoot(document.getElementById("root")!).render(<StrictMode><App /></StrictMode>);
