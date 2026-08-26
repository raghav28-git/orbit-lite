import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

interface ApiKey {
  id: number;
  active: boolean;
  createdAt: string;
  revokedAt?: string | null;
}

function ApiKeys() {
  const { projectId, environmentId } = useParams();

  const [apiKeys, setApiKeys] = useState<ApiKey[]>([]);
  const [loading, setLoading] = useState(true);
  const [creating, setCreating] = useState(false);
  const [newApiKey, setNewApiKey] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const loadApiKeys = async () => {
      try {
        const response = await api.get(
          `/projects/${projectId}/environments/${environmentId}/api-keys`
        );

        setApiKeys(response.data);
      } catch (error) {
        console.error("Failed to load API keys:", error);
      } finally {
        setLoading(false);
      }
    };

    loadApiKeys();
  }, [projectId, environmentId]);

  const handleCreate = async () => {
  try {
    setCreating(true);

    const response = await api.post(
      `/projects/${projectId}/environments/${environmentId}/api-keys`
    );

    setNewApiKey(response.data.apiKey);

    const refreshed = await api.get(
      `/projects/${projectId}/environments/${environmentId}/api-keys`
    );

    setApiKeys(refreshed.data);
  } catch (error) {
    console.error("Failed to create API key:", error);
    alert("Failed to create API key");
  } finally {
    setCreating(false);
  }
};

const handleRevoke = async (apiKeyId: number) => {
  try {
    await api.delete(
      `/projects/${projectId}/environments/${environmentId}/api-keys/${apiKeyId}`
    );

    const response = await api.get(
      `/projects/${projectId}/environments/${environmentId}/api-keys`
    );

    setApiKeys(response.data);
  } catch (error) {
    console.error("Failed to revoke API key:", error);
    alert("Failed to revoke API key");
  }
};

const copyApiKey = async () => {
  if (!newApiKey) {
    return;
  }

  await navigator.clipboard.writeText(newApiKey);
  alert("API key copied");
};

  return (
  <div className="dashboard-page">
    <header className="topbar">
      <div>
        <h1>ORBIT</h1>
        <span>Lite</span>
      </div>

      <button
        onClick={() => {
          localStorage.removeItem("orbit_token");
          navigate("/");
        }}
      >
        Logout
      </button>
    </header>

    <main className="dashboard-content">
      <button
        className="back-button"
        onClick={() =>
          navigate(`/projects/${projectId}`)
        }
      >
        ← Back to Environment
      </button>

      <div className="page-heading">
        <h2>API Keys</h2>
        <p>
          Manage API access for this environment.
        </p>
      </div>

      <button
        className="primary-button"
        onClick={handleCreate}
        disabled={creating}
      >
        {creating ? "Creating..." : "Create API Key"}
      </button>

      {newApiKey && (
        <div className="api-key-warning">
          <h3>New API Key</h3>

          <p>
            Copy this key now. It will not be shown again.
          </p>

          <div className="api-key-value">
            <code>{newApiKey}</code>

            <button onClick={copyApiKey}>
              Copy
            </button>
          </div>
        </div>
      )}

      <section className="api-key-list">
        {loading ? (
          <p>Loading API keys...</p>
        ) : apiKeys.length === 0 ? (
          <p>No API keys found.</p>
        ) : (
          apiKeys.map((apiKey) => (
            <div
              key={apiKey.id}
              className="api-key-card"
            >
              <div>
                <h3>API Key #{apiKey.id}</h3>

                <span
                  className={
                    apiKey.active
                      ? "status-active"
                      : "status-revoked"
                  }
                >
                  {apiKey.active
                    ? "Active"
                    : "Revoked"}
                </span>

                <p>
                  Created:{" "}
                  {new Date(
                    apiKey.createdAt
                  ).toLocaleString()}
                </p>

                {apiKey.revokedAt && (
                  <p>
                    Revoked:{" "}
                    {new Date(
                      apiKey.revokedAt
                    ).toLocaleString()}
                  </p>
                )}
              </div>

              {apiKey.active && (
                <button
                  onClick={() =>
                    handleRevoke(apiKey.id)
                  }
                >
                  Revoke
                </button>
              )}
            </div>
          ))
        )}
      </section>
    </main>
  </div>
);
}

export default ApiKeys;