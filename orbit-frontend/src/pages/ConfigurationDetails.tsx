import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import { useNavigate } from "react-router-dom";


interface ConfigurationVersion {
  id: number;
  version: number;
  value: string;
  createdAt: string;
}


interface Configuration {
  id: number;
  key: string;
  value: string;
}

function ConfigurationDetails() {
  const navigate = useNavigate();
  const {
    projectId,
    environmentId,
    configurationId,
  } = useParams();

  const [configuration, setConfiguration] =
    useState<Configuration | null>(null);

    
  const [loading, setLoading] = useState(true);
  const [value, setValue] = useState("");
  const [saving, setSaving] = useState(false);
  const [versions, setVersions] = useState<ConfigurationVersion[]>([]);
  const [loadingVersions, setLoadingVersions] = useState(true);

  const handleUpdate = async () => {
    if (!configuration) {
      return;
    }

    try {
      setSaving(true);

      const response = await api.put(
        `/projects/${projectId}/environments/${environmentId}/configurations/${configuration.id}`,
        {
          value,
        }
      );

      setConfiguration(response.data);

      alert("Configuration updated successfully");
    } catch (error) {
      console.error("Failed to update configuration:", error);
      alert("Failed to update configuration");
    } finally {
      setSaving(false);
    }
  };

  const handleRollback = async (version: number) => {
  if (!configuration) {
    return;
  }

  try {
    await api.post(
      `/projects/${projectId}/environments/${environmentId}/configurations/${configuration.id}/rollback`,
      {
        version,
      }
    );

    alert(`Rolled back to version ${version}`);

    window.location.reload();
  } catch (error) {
    console.error("Failed to rollback configuration:", error);
    alert("Failed to rollback configuration");
  }
};


  useEffect(() => {
    const loadConfiguration = async () => {
      try {
        /*
         * We currently don't have a dedicated
         * GET /configurations/{id} endpoint.
         *
         * So load the environment's configurations
         * and find the requested configuration.
         */
        const response = await api.get(
          `/projects/${projectId}/environments/${environmentId}/configurations`
        );

        const found = response.data.find(
          (item: Configuration) =>
            item.id === Number(configurationId)
        );

        setConfiguration(found ?? null);

        if (found) {
        setValue(found.value);
        }
      } catch (error) {
        console.error(
          "Failed to load configuration:",
          error
        );
      } finally {
        setLoading(false);
      }
    };

    loadConfiguration();
  }, [projectId, environmentId, configurationId]);

  useEffect(() => {
  const loadVersions = async () => {
    try {
      const response = await api.get(
        `/projects/${projectId}/environments/${environmentId}/configurations/${configurationId}/versions`
      );

      setVersions(response.data);
    } catch (error) {
      console.error("Failed to load versions:", error);
    } finally {
      setLoadingVersions(false);
    }
  };

  loadVersions();
}, [projectId, environmentId, configurationId]);

  if (loading) {
    return <p>Loading configuration...</p>;
  }

  if (!configuration) {
    return <p>Configuration not found.</p>;
  }

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
          navigate(
            `/projects/${projectId}/environments/${environmentId}`
          )
        }
      >
        ← Back to Configurations
      </button>

      <div className="page-heading">
        <h2>{configuration.key}</h2>
        <p>Manage this configuration and its version history.</p>
      </div>

      <section className="configuration-editor">
        <h3>Current Value</h3>

        <input
          className="configuration-input"
          type="text"
          value={value}
          onChange={(event) => setValue(event.target.value)}
        />

        <button
          className="primary-button"
          onClick={handleUpdate}
          disabled={saving}
        >
          {saving ? "Updating..." : "Update Configuration"}
        </button>
      </section>

      <section className="version-section">
        <h2>Version History</h2>

        {loadingVersions ? (
          <p>Loading versions...</p>
        ) : versions.length === 0 ? (
          <p>No versions found.</p>
        ) : (
          <div className="version-list">
            {versions.map((version) => (
              <div
                key={version.id}
                className="version-card"
              >
                <div>
                  <h3>Version {version.version}</h3>

                  <p>
                    Value:{" "}
                    <code>{version.value}</code>
                  </p>

                  <small>
                    {new Date(
                      version.createdAt
                    ).toLocaleString()}
                  </small>
                </div>

                <button
                  onClick={() =>
                    handleRollback(version.version)
                  }
                >
                  Rollback
                </button>
              </div>
            ))}
          </div>
        )}
      </section>
    </main>
  </div>
);
}

export default ConfigurationDetails;