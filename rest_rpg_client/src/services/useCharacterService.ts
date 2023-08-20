import { useState } from "react";
import useErrorToast from "../hooks/useErrorToast";
import { useStores } from "../store/RootStore";
import useSuccessToast from "../hooks/useSuccessToast";
import { Error } from "../classes/error/Error";
import { useTranslation } from "react-i18next";
import { AxiosError } from "axios";
import {
  CharacterCreateRequest,
  DefaultApiFp,
} from "../generated-sources/openapi";
import useAxiosPrivate from "../hooks/useAxiosPrivate";

const useCharacterService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const errorToast = useErrorToast();
  const successToast = useSuccessToast();
  const { characterStore } = useStores();
  const { t } = useTranslation();
  const axiosPrivate = useAxiosPrivate();
  const api = DefaultApiFp();

  const create = async (request: CharacterCreateRequest) => {
    setIsLoading(true);
    const createCharacter = await api.createCharacter(request, {withCredentials: true});
    
    createCharacter(axiosPrivate)
      .then((response) => {
        characterStore.characterLite(response.data);
        successToast(t("CHARACTER.CREATION_SUCCESSFUL"));
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
    create,
  };
};

export default useCharacterService;
