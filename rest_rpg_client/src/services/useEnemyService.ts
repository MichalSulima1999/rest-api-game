import { useState } from "react";
import useErrorToast from "../hooks/useErrorToast";
import { useStores } from "../store/RootStore";
import useSuccessToast from "../hooks/useSuccessToast";
import { Error } from "../classes/error/Error";
import { useTranslation } from "react-i18next";
import { AxiosError } from "axios";
import { DefaultApiFp, EnemyCreateRequest } from "../generated-sources/openapi";
import useAxiosPrivate from "../hooks/useAxiosPrivate";

const useEnemyService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const errorToast = useErrorToast();
  const successToast = useSuccessToast();
  const { enemyStore } = useStores();
  const { t } = useTranslation();
  const axiosPrivate = useAxiosPrivate();
  const api = DefaultApiFp();

  const create = async (request: EnemyCreateRequest) => {
    setIsLoading(true);
    const createEnemy = await api.createEnemy(request, {
      withCredentials: true,
    });

    createEnemy(axiosPrivate)
      .then((response) => {
        enemyStore.enemyLite(response.data);
        successToast(t("ENEMY.CREATION_SUCCESSFUL"));
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

export default useEnemyService;
