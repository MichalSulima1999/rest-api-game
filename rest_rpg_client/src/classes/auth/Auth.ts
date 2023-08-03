export interface AuthResponse {
  username: string;
  token: string;
  role: string;
}

export interface AuthRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  role: string;
}
