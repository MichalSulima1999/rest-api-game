import { AxiosResponse } from "axios";
import { axiosPrivate } from "../api/axios";
import {
  AuthRequest,
  AuthResponse,
  RegisterRequest,
} from "../classes/auth/Auth";
import AuthStore from "../store/AuthStore";
import { CreateToastFnReturn } from "@chakra-ui/react";
import { Error } from "../classes/error/Error";

const LOGIN_URL = "/auth/authenticate";
const REGISTER_URL = "/auth/register";

export const auth = async (
  authRequest: AuthRequest,
  authStore: AuthStore,
  toast: CreateToastFnReturn
) => {
  try {
    const response: AxiosResponse<AuthResponse> = await axiosPrivate.post(
      LOGIN_URL,
      authRequest
    );

    authStore.auth(response.data);
    toast({
      title: `Successfully logged in!`,
      status: "success",
      isClosable: true,
    });
  } catch (err) {
    const error = err as Error;
    toast({
      title: error.response?.data?.message,
      status: "error",
      isClosable: true,
    });
  }
};

export const register = async (
  registerRequest: RegisterRequest,
  toast: CreateToastFnReturn
) => {
  try {
    await axiosPrivate.post(REGISTER_URL, registerRequest, {
      withCredentials: true,
    });

    toast({
      title: `Successfully registered! Please, confirm your email.`,
      status: "success",
      isClosable: true,
    });
  } catch (err) {
    const error = err as Error;
    toast({
      title: error.response?.data?.message,
      status: "error",
      isClosable: true,
    });
  }
};
