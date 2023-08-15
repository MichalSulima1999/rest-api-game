import { axiosPrivate } from "../api/axios";
import {
  AuthRequest,
  AuthResponse,
  RegisterRequest,
} from "../classes/auth/Auth";

const LOGIN_URL = "/auth/authenticate";
const REGISTER_URL = "/auth/register";

export const auth = async (authRequest: AuthRequest): Promise<AuthResponse> => {
  return axiosPrivate.post(LOGIN_URL, authRequest);
};

export const register = async (
  registerRequest: RegisterRequest
): Promise<void> => {
  return axiosPrivate.post(REGISTER_URL, registerRequest);
};
