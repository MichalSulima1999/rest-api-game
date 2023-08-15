import { useState } from "react";
import { auth, register as registerUser } from "./AuthService";
import useErrorToast from "../hooks/useErrorToast";
import { useStores } from "../store/RootStore";
import useSuccessToast from "../hooks/useSuccessToast";
import { AuthRequest, RegisterRequest } from "../classes/auth/Auth";
import { Error } from "../classes/error/Error";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { AxiosError } from "axios";

const useAuthService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const errorToast = useErrorToast();
  const successToast = useSuccessToast();
  const { authStore } = useStores();
  const { t } = useTranslation();
  const navigate = useNavigate();

  const authenticate = async (request: AuthRequest) => {
    setIsLoading(true);

    await auth(request)
      .then((response) => {
        authStore.auth(response);
        successToast(t("AUTH.LOGIN_SUCCESSFUL"));
        navigate("/home");
      })
      .catch((err: AxiosError) => {
        if (err.response?.data) {
          const error = err as Error;
        errorToast(t(`ERROR.${error.response.data.message}`));
        } else {
          errorToast(t("ERROR.COMMUNICATION"));
        }
      })
      .finally(() => setIsLoading(false));
  };

  const register = async (request: RegisterRequest) => {
    setIsLoading(true);

    await registerUser(request)
      .then(() => {
        successToast(t("AUTH.REGISTRATION_SUCCESSFUL"));
        navigate("/login");
      })
      .catch((err: AxiosError) => {
        if (err.response?.data) {
          const error = err as Error;
        errorToast(t(`ERROR.${error.response.data.message}`));
        } else {
          errorToast(t("ERROR.COMMUNICATION"));
        }
      })
      .finally(() => setIsLoading(false));
  };

  return {
    isLoading,
    authenticate,
    register,
  };
};

export default useAuthService;
