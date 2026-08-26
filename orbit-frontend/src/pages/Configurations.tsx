import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

interface Configuration {
  id: number;
  key: string;
  value: string;
}

function Configurations() {
  const navigate = useNavigate();
  const { projectId, environmentId } = useParams();

  const [configurations, setConfigurations] = useState<Configuration[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadConfigurations = async () => {
      try {
        const response = await api.get(
          `/projects/${projectId}/environments/${environmentId}/configurations`
        );

        setConfigurations(response.data);
      } catch (error) {
        console.error("Failed to load configurations:", error);
      } finally {
        setLoading(false);
      }
    };

    loadConfigurations();
  }, [projectId, environmentId]);

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
        <h2>Configurations</h2>
        <p>
          Manage runtime configuration values for this environment.
        </p>
      </div>

      {loading ? (
        <p>Loading configurations...</p>
      ) : configurations.length === 0 ? (
        <p>No configurations found.</p>
      ) : (
        <div className="configuration-list">
          {configurations.map((configuration) => (
            <div
              key={configuration.id}
              className="configuration-card"
              onClick={() =>
                navigate(
                  `/projects/${projectId}/environments/${environmentId}/configurations/${configuration.id}`
                )
              }
            >
              <div>
                <h3>{configuration.key}</h3>

                <p className="configuration-value">
                  {configuration.value}
                </p>
              </div>

              <span className="configuration-arrow">
                →
              </span>
            </div>
          ))}
        </div>
      )}
    </main>
  </div>
);
}

export default Configurations;