import { StrictMode, useEffect, useState } from "react";
import { createRoot } from "react-dom/client";
import { getHealth, type HealthResponse } from "./api";
import "./styles.css";

function App() {
  const [health, setHealth] = useState<HealthResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getHealth().then(setHealth).catch((requestError: unknown) => {
      setError(requestError instanceof Error ? requestError.message : "Unable to reach the OpenWeb API.");
    });
  }, []);

  return (
    <main>
      <section className="shell">
        <p className="eyebrow">OPENWEB / FOUNDATION</p>
        <h1>Build your corner of the web.</h1>
        <p className="description">
          The OpenWeb foundation is running. This will grow into a platform
          for building, publishing, discovering, and browsing user-created sites.
        </p>
        <div className="status" aria-live="polite">
          <span className={health ? "indicator online" : "indicator"} />
          <div>
            <strong>{health ? "API connected" : "Connecting to API"}</strong>
            <span>{error ?? (health ? health.service : "localhost:8080")}</span>
          </div>
        </div>
      </section>
    </main>
  );
}

createRoot(document.getElementById("root")!).render(
  <StrictMode><App /></StrictMode>,
);
