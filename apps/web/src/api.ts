const API_URL = (import.meta.env.VITE_API_URL ?? "http://localhost:8080").replace(/\/$/, "");

export type HealthResponse = { status: string; service: string };
export type CurrentUser = { id: string; email: string; displayName: string };

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_URL}${path}`, {
    ...init,
    credentials: "include",
    headers: { "Content-Type": "application/json", ...(init?.headers ?? {}) },
  });
  if (!response.ok) throw new Error(`Request failed: ${response.status}`);
  return response.json() as Promise<T>;
}

export const getHealth = () => request<HealthResponse>("/api/health");
export const getCurrentUser = () => request<CurrentUser | null>("/api/me").catch(() => null);
export const googleLoginUrl = `${API_URL}/oauth2/authorization/google`;