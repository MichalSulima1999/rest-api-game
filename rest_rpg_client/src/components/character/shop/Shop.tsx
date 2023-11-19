import { useState, useEffect } from "react";
import {
  ChakraProvider,
  extendTheme,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  Box,
  Skeleton,
  Text,
  Button,
  Heading,
} from "@chakra-ui/react";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import useSkillService from "../../../services/useSkillService";
import { ItemLitePage, PotionLite } from "../../../generated-sources/openapi";
import Pagination from "../../tables/Pagination";
import useEquipmentService from "../../../services/useEquipmentService";
import { useStores } from "../../../store/RootStore";
import useCharacterService from "../../../services/useCharacterService";

const theme = extendTheme({
  components: {
    Table: {
      baseStyle: {
        borderRadius: "lg",
        boxShadow: "md",
      },
    },
  },
});

const pageSize = 10;

function Shop() {
  const { t } = useTranslation();
  const skillService = useSkillService();
  const equipmentService = useEquipmentService();
  const characterService = useCharacterService();
  const [page, setPage] = useState(0);
  const [data, setData] = useState<ItemLitePage | null>(null);
  const [potionData, setPotionData] = useState<PotionLite | null>(null);
  const { characterId } = useParams();
  const { equipmentStore, characterStore } = useStores();

  useEffect(() => {
    async function getCharacterIfNeeded() {
      if (characterStore.id == -1 && characterId) {
        await characterService.getUserCharacter(parseInt(characterId));
      }
    }
    getCharacterIfNeeded().catch((error) => console.log(error));
  }, []);

  useEffect(() => {
    async function getPotionData() {
      const info = await equipmentService.getPotionInfo();
      if (info) {
        setPotionData(info);
      }
    }
    async function getItemsPage() {
      const items = await equipmentService.findItems({
        pagination: { pageNumber: page, elements: pageSize },
      });
      if (items) {
        setData(items);
      }
    }
    getItemsPage().catch((error) => console.log(error));
    getPotionData().catch((error) => console.log(error));
  }, [page]);

  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  const handleItemBuy = (itemId: number) => {
    if (characterId) {
      equipmentService
        .buyItem(itemId, parseInt(characterId))
        .catch((e) => console.log(e));
    }
  };

  const handlePotionBuy = () => {
    if (characterId) {
      equipmentService
        .buyPotion(parseInt(characterId))
        .catch((e) => console.log(e));
    }
  };

  return (
    <ChakraProvider theme={theme}>
      <Box p={4} bgColor={"gray.800"} color={"white"}>
        <Skeleton isLoaded={!skillService.isLoading}>
          <Heading mb={4}>{t("SHOP.NAME")}</Heading>
          <Text>Gold: {equipmentStore.gold}</Text>
          <Table variant="simple" bgColor={"gray.900"} size="md" mb={4}>
            <Thead>
              <Tr>
                <Th>{t("ITEM.NAME")}</Th>
                <Th>{t("ITEM.TYPE")}</Th>
                <Th>{t("ITEM.POWER")}</Th>
                <Th>{t("ITEM.PRICE")}</Th>
                <Th>{t("ITEM.BUY")}</Th>
              </Tr>
            </Thead>
            <Tbody>
              {data?.content.map((item, index) => (
                <Tr key={index}>
                  <Td>{item.name}</Td>
                  <Td>{item.type}</Td>
                  <Td>{item.power}</Td>
                  <Td>{item.price}</Td>
                  <Td>
                    <Button onClick={() => handleItemBuy(item.id)}>
                      {t("ITEM.BUY")}
                    </Button>
                  </Td>
                </Tr>
              ))}
            </Tbody>
          </Table>

          {data && (
            <Pagination
              currentPage={data.number || 1}
              totalPages={
                data.totalElements
                  ? Math.ceil(data.totalElements / pageSize)
                  : 1
              }
              onPageChange={handlePageChange}
            />
          )}

          <Text>
            Potion cost: {potionData?.price}, heal percent:{" "}
            {potionData?.healPercent}
          </Text>
          <Button onClick={handlePotionBuy}>Buy potion</Button>
        </Skeleton>
      </Box>
    </ChakraProvider>
  );
}

export default Shop;
