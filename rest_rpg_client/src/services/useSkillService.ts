import { useState } from "react";
import useErrorToast from "../hooks/useErrorToast";
import useSuccessToast from "../hooks/useSuccessToast";
import { Error } from "../classes/error/Error";
import { useTranslation } from "react-i18next";
import { AxiosError } from "axios";
import {
  DefaultApiFp,
  SkillCreateRequest,
  SkillLite,
  SkillLites,
} from "../generated-sources/openapi";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import useServiceHelper from "./helpers/useServiceHelper";

const useSkillService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const errorToast = useErrorToast();
  const successToast = useSuccessToast();
  const { t } = useTranslation();
  const axiosPrivate = useAxiosPrivate();
  const api = DefaultApiFp();
  const { getResources } = useServiceHelper();

  const create = async (request: SkillCreateRequest): Promise<SkillLite> => {
    request.effect = request.effect ? request.effect : undefined;
    setIsLoading(true);
    const createSkill = await api.createSkill(request, {
      withCredentials: true,
    });

    return createSkill(axiosPrivate)
      .then((response) => {
        successToast(t("SKILL.CREATION_SUCCESSFUL"));
        return response.data;
      })
      .catch((err: AxiosError) => {
        if (err.response?.data) {
          const error = err as Error;
          errorToast(t(`ERROR.${error.response.data.message}`));
        } else {
          errorToast(t("ERROR.COMMUNICATION"));
        }
        const skill: SkillLite = {
          id: -1,
          name: "",
        };
        return skill;
      })
      .finally(() => setIsLoading(false));
  };

  const getAllSkills = async (): Promise<SkillLites | undefined> => {
    setIsLoading(true);
    const getSkills = await api.getSkills({
      withCredentials: true,
    });

    return getResources(getSkills, setIsLoading);
  };

  return {
    isLoading,
    create,
    getAllSkills,
  };
};

export default useSkillService;
