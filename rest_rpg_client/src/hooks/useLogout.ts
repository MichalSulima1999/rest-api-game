import { useToast } from "@chakra-ui/react";
import axios from "../api/axios";
import { useStores } from "../store/RootStore";
import { Error } from "../classes/error/Error";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";

const useLogout = () => {
  const { authStore } = useStores();
  const toast = useToast();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const logout = async () => {
    try {
      await axios("/refresh-token/logout", {
        withCredentials: true,
      });
      authStore.auth({
        username: "",
        token: "",
        role: "",
      });
      toast({
        title: t('AUTH.LOGOUT_SUCCESSFUL'),
        status: "success",
        isClosable: true,
      });
      navigate("/login");
    } catch (err) {
        const error = err as Error;
        toast({
            title: t(error.response?.data?.message),
            status: "error",
            isClosable: true,
          });
    }
  };

  return logout;
};

export default useLogout;
