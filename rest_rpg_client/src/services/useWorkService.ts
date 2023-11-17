import { useState } from "react";
import {
  DefaultApiFp,
  WorkCreateRequest,
  WorkLite,
  WorkLitePage,
  WorkSearchRequest,
} from "../generated-sources/openapi";
import useServiceHelper from "./helpers/useServiceHelper";

const useWorkService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const api = DefaultApiFp();
  const { getResources } = useServiceHelper();

  const createWork = async (
    request: WorkCreateRequest
  ): Promise<WorkLite | undefined> => {
    setIsLoading(true);
    const createWork = await api.createWork(request, {
      withCredentials: true,
    });

    return getResources(createWork, setIsLoading, "WORK.CREATED_SUCCESSFULLY");
  };

  const findWorks = async (
    request: WorkSearchRequest
  ): Promise<WorkLitePage | undefined> => {
    setIsLoading(true);
    const findWorks = await api.findWorks(request, {
      withCredentials: true,
    });

    return getResources(findWorks, setIsLoading);
  };

  const startWork = async (workId: number, characterId: number) => {
    setIsLoading(true);
    const startWork = await api.startWork(workId, characterId, {
      withCredentials: true,
    });

    return getResources(startWork, setIsLoading, "WORK.STARTED_SUCCESSFULLY");
  };

  const endWork = async (characterId: number) => {
    setIsLoading(true);
    const endWork = await api.endWork(characterId, {
      withCredentials: true,
    });

    return getResources(endWork, setIsLoading, "WORK.ENDED_SUCCESSFULLY");
  };

  return {
    isLoading,
    createWork,
    findWorks,
    startWork,
    endWork,
  };
};

export default useWorkService;
