import { useState } from "react";
import useErrorToast from "../hooks/useErrorToast";
import useSuccessToast from "../hooks/useSuccessToast";
import { Error } from "../classes/error/Error";
import { useTranslation } from "react-i18next";
import { AxiosError } from "axios";
import {
  DefaultApiFp,
  ItemCreateRequest,
  ItemLite,
  ItemLitePage,
  ItemSearchRequest,
} from "../generated-sources/openapi";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import useServiceHelper from "./helpers/useServiceHelper";
import { useStores } from "../store/RootStore";

const useEquipmentService = () => {
  const [isLoading, setIsLoading] = useState(false);
  const errorToast = useErrorToast();
  const successToast = useSuccessToast();
  const { t } = useTranslation();
  const axiosPrivate = useAxiosPrivate();
  const api = DefaultApiFp();
  const { getResources } = useServiceHelper();
  const { statisticsStore, equipmentStore } = useStores();

  const create = async (request: ItemCreateRequest) => {
    setIsLoading(true);
    const createItem = await api.createItem(request, {
      withCredentials: true,
    });

    createItem(axiosPrivate)
      .then((response) => {
        successToast(t("ITEM.CREATION_SUCCESSFUL"));
        return response.data;
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

  const findItems = async (
    request: ItemSearchRequest
  ): Promise<ItemLitePage | undefined> => {
    setIsLoading(true);
    const findItems = await api.findItems(request, {
      withCredentials: true,
    });

    return getResources(findItems, setIsLoading);
  };

  const buyItem = async (
    itemId: number,
    characterId: number
  ): Promise<ItemLite | undefined> => {
    setIsLoading(true);
    const buyItem = await api.buyItem(itemId, characterId, {
      withCredentials: true,
    });

    return getResources(buyItem, setIsLoading, "ITEM.BOUGHT_SUCCESSFULLY");
  };

  const buyPotion = async (characterId: number) => {
    setIsLoading(true);
    const buyPotion = await api.buyPotion(characterId, {
      withCredentials: true,
    });

    return getResources(buyPotion, setIsLoading, "ITEM.BOUGHT_SUCCESSFULLY");
  };

  const getPotionInfo = async () => {
    setIsLoading(true);
    const getPotionInfo = await api.getPotionInfo({
      withCredentials: true,
    });

    return getResources(getPotionInfo, setIsLoading);
  };

  const usePotion = async (characterId: number) => {
    setIsLoading(true);
    const usePotion = await api.usePotion(characterId, {
      withCredentials: true,
    });

    const response = getResources(usePotion, setIsLoading, "ITEM.POTION_USED");
    response
      .then((r) => {
        if (r) {
          statisticsStore.statisticsLite(r);
          equipmentStore.healthPotions--;
        }
      })
      .catch((e) => console.log(e));
  };

  return {
    isLoading,
    create,
    findItems,
    buyItem,
    buyPotion,
    getPotionInfo,
    usePotion,
  };
};

export default useEquipmentService;
