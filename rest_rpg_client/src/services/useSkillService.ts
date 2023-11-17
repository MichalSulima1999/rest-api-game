import { useState } from "react";
import useErrorToast from "../hooks/useErrorToast";
import useSuccessToast from "../hooks/useSuccessToast";
import { Error } from "../classes/error/Error";
import { useTranslation } from "react-i18next";
import { AxiosError } from "axios";
import {
  CharacterSkillBasics,
  DefaultApiFp,
  SkillBasicPage,
  SkillCreateRequest,
  SkillDetails,
  SkillLite,
  SkillLites,
  SkillSearchRequest,
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

  const findSkills = async (
    request: SkillSearchRequest
  ): Promise<SkillBasicPage | undefined> => {
    setIsLoading(true);
    const findSkills = await api.findSkills(request, {
      withCredentials: true,
    });

    return getResources(findSkills, setIsLoading);
  };

  const getSkill = async (
    skillId: number
  ): Promise<SkillDetails | undefined> => {
    setIsLoading(true);
    const getSkill = await api.getSkill(skillId, {
      withCredentials: true,
    });

    return getResources(getSkill, setIsLoading);
  };

  const getAllSkills = async (): Promise<SkillLites | undefined> => {
    setIsLoading(true);
    const getSkills = await api.getSkills({
      withCredentials: true,
    });

    return getResources(getSkills, setIsLoading);
  };

  const getCharacterSkills = async (
    characterId: number
  ): Promise<CharacterSkillBasics | undefined> => {
    setIsLoading(true);
    const getCharacterSkills = await api.getCharacterSkills(characterId, {
      withCredentials: true,
    });

    return getResources(getCharacterSkills, setIsLoading);
  };

  const learnSkill = async (
    skillId: number,
    characterId: number
  ): Promise<SkillLite | undefined> => {
    setIsLoading(true);
    const learnSkill = await api.learnSkill(skillId, characterId, {
      withCredentials: true,
    });

    return getResources(learnSkill, setIsLoading, "SKILL.LEARNED_SUCCESSFULLY");
  };

  return {
    isLoading,
    create,
    findSkills,
    getAllSkills,
    getSkill,
    getCharacterSkills,
    learnSkill,
  };
};

export default useSkillService;
