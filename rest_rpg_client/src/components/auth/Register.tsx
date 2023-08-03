/* eslint-disable @typescript-eslint/no-misused-promises */
import React, { useState } from "react";
import { RegisterRequest } from "../../classes/auth/Auth";
import { register } from "../../services/AuthService";

const Register = () => {
  const [request, setRequest] = useState<RegisterRequest>({
    username: "",
    password: "",
    email: "",
    role: "USER",
  });

  const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRequest({ ...request, username: event.target.value });
  };

  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRequest({ ...request, password: event.target.value });
  };

  const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRequest({ ...request, email: event.target.value });
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    await register(request);
  };

  return (
    <div>
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>
            Username:
            <input
              type="text"
              value={request.username}
              onChange={handleUsernameChange}
            />
          </label>
        </div>
        <div>
          <label>
            Email:
            <input
              type="email"
              value={request.email}
              onChange={handleEmailChange}
            />
          </label>
        </div>
        <div>
          <label>
            Password:
            <input
              type="password"
              value={request.password}
              onChange={handlePasswordChange}
            />
          </label>
        </div>
        <div>
          <button type="submit">Register</button>
        </div>
      </form>
    </div>
  );
};

export default Register;
