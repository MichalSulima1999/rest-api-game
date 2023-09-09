import { AxiosInstance, AxiosPromise } from "axios";
import { Error } from "../../classes/error/Error";
import useAxiosPrivate from "../../hooks/useAxiosPrivate";
import useErrorToast from "../../hooks/useErrorToast";
import { useTranslation } from "react-i18next";
import { Dispatch, SetStateAction } from "react";

const useServiceHelper = () => {
  const axiosPrivate = useAxiosPrivate();
  const errorToast = useErrorToast();
  const { t } = useTranslation();

  function getResources<T>(
    getFunc: (
      axios?: AxiosInstance | undefined,
      basePath?: string | undefined
    ) => AxiosPromise<T>,
    setIsLoading: Dispatch<SetStateAction<boolean>>
  ): Promise<T | undefined> {
    return getFunc(axiosPrivate)
      .then((response) => {
        return response.data;
      })
      .catch((err: Error) => {
        if (err.response?.data) {
          errorToast(t(`ERROR.${err.response.data.message}`));
        } else {
          errorToast(t("ERROR.COMMUNICATION"));
        }
        return undefined;
      })
      .finally(() => setIsLoading(false));
  }

  return {
    getResources,
  };
};

export default useServiceHelper;
