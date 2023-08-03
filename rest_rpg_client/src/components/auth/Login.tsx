/* eslint-disable @typescript-eslint/no-misused-promises */
import "./Auth.css";
import React, { useState } from "react";
import { useStores } from "../../store/RootStore";
import { AuthRequest } from "../../classes/auth/Auth";
import { auth } from "../../services/AuthService";

const Login = () => {
  const { authStore } = useStores();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    const request: AuthRequest = {
      username: username,
      password: password,
    };
    await auth(request, authStore);
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h1 className="login-header">Logowanie</h1>
        <form onSubmit={handleSubmit}>
          <div>
            <label className="input-field">
              Username:
              <input
                type="text"
                value={username}
                onChange={handleUsernameChange}
              />
            </label>
          </div>
          <div className="input-field">
            <label>
              Password:
              <input
                type="password"
                value={password}
                onChange={handlePasswordChange}
              />
            </label>
          </div>
          <div>
            <button type="submit" className="login-button">
              Login
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
