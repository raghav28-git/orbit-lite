import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

interface EnvironmentData {
  id: number;
  name: string;
}

function Environment() {
  const navigate = useNavigate();
  const { projectId } = useParams();
  const [environments, setEnvironments] = useState<EnvironmentData[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadEnvironments = async () => {
      try {
        const response = await api.get(
          `/projects/${projectId}/environments`
        );

        setEnvironments(response.data);
      } catch (error) {
        console.error("Failed to load environments:", error);
      } finally {
        setLoading(false);
      }
    };

    loadEnvironments();
  }, [projectId]);

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
        onClick={() => navigate("/dashboard")}
      >
        ← Back to Projects
      </button>

      <div className="page-heading">
        <h2>Environments</h2>
        <p>Manage configuration environments for this project.</p>
      </div>

      {loading ? (
        <p>Loading environments...</p>
      ) : environments.length === 0 ? (
        <p>No environments found.</p>
      ) : (
        <div className="project-grid">
          {environments.map((environment) => (
            <div
              key={environment.id}
              className="project-card"
            >
              <h3>{environment.name}</h3>

              <div className="environment-actions">
                <button
                  onClick={() =>
                    navigate(
                      `/projects/${projectId}/environments/${environment.id}`
                    )
                  }
                >
                  Configurations
                </button>

                <button
                  onClick={() =>
                    navigate(
                      `/projects/${projectId}/environments/${environment.id}/api-keys`
                    )
                  }
                >
                  API Keys
                </button>

                <button
                  onClick={() =>
                    navigate(
                      `/projects/${projectId}/environments/${environment.id}/audit-logs`
                    )
                  }
                >
                  Audit Logs
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </main>
  </div>
);
}

export default Environment;