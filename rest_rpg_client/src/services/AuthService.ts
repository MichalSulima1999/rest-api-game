import { AxiosResponse } from "axios";
import { axiosPrivate } from "../api/axios";
import {
  AuthRequest,
  AuthResponse,
  RegisterRequest,
} from "../classes/auth/Auth";
import AuthStore from "../store/AuthStore";

const LOGIN_URL = "/auth/authenticate";
const REGISTER_URL = "/auth/register";

export const auth = async (authRequest: AuthRequest, authStore: AuthStore) => {
  try {
    const response: AxiosResponse<AuthResponse> = await axiosPrivate.post(
      LOGIN_URL,
      authRequest,
      {
        withCredentials: true,
      }
    );

    authStore.auth(response.data);

    console.log("Zalogowano");
  } catch (err) {
    console.log(err);
  }
};

export const register = async (registerRequest: RegisterRequest) => {
  try {
    await axiosPrivate.post(REGISTER_URL, registerRequest, {
      withCredentials: true,
    });

    console.log("Zarejestrowano!");
  } catch (err) {
    console.log(err);
  }
};
