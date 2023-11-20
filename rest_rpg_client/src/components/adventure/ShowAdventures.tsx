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
  useDisclosure,
  Text,
  Button,
} from "@chakra-ui/react";
import { AdventureBasicPage } from "../../generated-sources/openapi";
import useAdventureService from "../../services/useAdventureService";
import Pagination from "../tables/Pagination";
import { useTranslation } from "react-i18next";
import AdventureDetailsModal from "./AdventureDetailsModal";
import { useStores } from "../../store/RootStore";
import { secondsToTime } from "../../helpers/DateHelper";
import { Link, useParams } from "react-router-dom";
import useCharacterService from "../../services/useCharacterService";

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

function ShowAdventures() {
  const { t } = useTranslation();
  const adventureService = useAdventureService();
  const characterService = useCharacterService();
  const [page, setPage] = useState(0);
  const [data, setData] = useState<AdventureBasicPage | null>(null);
  const [currentAdventureId, setCurrentAdventureId] = useState(0);
  const { characterStore } = useStores();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const { characterId } = useParams();

  useEffect(() => {
    async function getCharacterIfNeeded() {
      if (characterStore.id == -1 && characterId) {
        await characterService.getUserCharacter(parseInt(characterId));
      }
    }
    getCharacterIfNeeded().catch((error) => console.log(error));
  }, []);

  useEffect(() => {
    async function getAdventurePage() {
      const adventures = await adventureService.findAdventures({
        pagination: { pageNumber: page, elements: pageSize },
      });
      if (adventures) {
        setData(adventures);
      }
    }
    getAdventurePage().catch((error) => console.log(error));
  }, [page]);

  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  const handleEndAdventure = () => {
    if (characterId) {
      adventureService
        .endAdventure(Number(characterId))
        .catch((e) => console.log(e));
    }
  };

  return (
    <ChakraProvider theme={theme}>
      <Box p={4} bgColor={"gray.800"} color={"white"}>
        <Text>{`${t("ADVENTURE.TIME")}: ${secondsToTime(
          characterStore.occupationTime
        )}`}</Text>
        {characterStore.occupationType}
        <Button
          onClick={handleEndAdventure}
          isDisabled={
            characterStore.occupationTime > 0 ||
            characterStore.occupationType == "Fight"
          }
        >
          {t("ADVENTURE.END")}
        </Button>
        <Button as={Link} to={`/user/character/${characterId!}/fight`}>
          {t("FIGHT.NAME")}
        </Button>
        <Skeleton isLoaded={!adventureService.isLoading}>
          <Table variant="simple" bgColor={"gray.900"} size="md" mb={4}>
            <Thead>
              <Tr>
                <Th>{t("ADVENTURE.NAME")}</Th>
                <Th>{t("CHARACTER.STATISTICS.XP")}</Th>
                <Th>{t("CHARACTER.STATISTICS.GOLD")}</Th>
                <Th>{t("ENEMY.NAME")}</Th>
              </Tr>
            </Thead>
            <Tbody>
              {data?.content.map((item, index) => (
                <Tr
                  key={index}
                  onClick={() => {
                    onOpen();
                    setCurrentAdventureId(item.id);
                  }}
                >
                  <Td>{item.name}</Td>
                  <Td>{item.xpForAdventure}</Td>
                  <Td>{item.goldForAdventure}</Td>
                  <Td>{item.enemy?.name}</Td>
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
        </Skeleton>
      </Box>

      <AdventureDetailsModal
        isOpen={isOpen}
        onClose={onClose}
        adventureId={currentAdventureId}
      />
    </ChakraProvider>
  );
}

export default ShowAdventures;
