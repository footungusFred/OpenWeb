const API_BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080";

export type HealthResponse = {
  status: string;
  service: string;
};

export async function getHealth(): Promise<HealthResponse> {
  const response = await fetch(`${API_BASE_URL}/api/health`);

  if (!response.ok) {
    throw new Error(`API request failed with HTTP ${response.status}`);
  }

  return response.json() as Promise<HealthResponse>;
}
