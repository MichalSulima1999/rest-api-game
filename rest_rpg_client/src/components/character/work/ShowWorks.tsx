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
} from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";
import useWorkService from "../../../services/useWorkService";
import { WorkLitePage } from "../../../generated-sources/openapi";
import { useStores } from "../../../store/RootStore";
import useCharacterService from "../../../services/useCharacterService";
import { secondsToTime } from "../../../helpers/DateHelper";
import Pagination from "../../tables/Pagination";

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

function ShowWorks() {
  const { t } = useTranslation();
  const workService = useWorkService();
  const characterService = useCharacterService();
  const [page, setPage] = useState(0);
  const [data, setData] = useState<WorkLitePage | null>(null);
  const { characterStore } = useStores();
  const { characterId } = useParams();

  useEffect(() => {
    async function getCharacterIfNeeded() {
      if (characterStore.id == -1 && characterId) {
        await characterService.getUserCharacter(parseInt(characterId));
      }
    }
    getCharacterIfNeeded().catch((error) => console.log(error));
  }, [characterId]);

  useEffect(() => {
    async function getWorksPage() {
      const works = await workService.findWorks({
        pagination: { pageNumber: page, elements: pageSize },
      });
      if (works) {
        setData(works);
      }
    }
    getWorksPage().catch((error) => console.log(error));
  }, [page]);

  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  const handleStartWork = (workId: number) => {
    if (characterId) {
      workService
        .startWork(workId, Number(characterId))
        .catch((e) => console.log(e));
    }
  };

  const handleEndWork = () => {
    if (characterId) {
      workService.endWork(Number(characterId)).catch((e) => console.log(e));
    }
  };

  return (
    <ChakraProvider theme={theme}>
      <Box p={4} bgColor={"gray.800"} color={"white"}>
        <Text>{`${t("WORK.TIME")}: ${secondsToTime(
          characterStore.occupationTime
        )}`}</Text>
        {characterStore.occupationType}
        <Button
          onClick={handleEndWork}
          isDisabled={
            characterStore.occupationTime > 0 ||
            characterStore.occupationType == "Fight"
          }
        >
          {t("WORK.END")}
        </Button>
        <Skeleton isLoaded={!workService.isLoading}>
          <Table variant="simple" bgColor={"gray.900"} size="md" mb={4}>
            <Thead>
              <Tr>
                <Th>{t("WORK.NAME")}</Th>
                <Th>{t("CHARACTER.STATISTICS.GOLD")}</Th>
                <Th>{t("WORK.WORK_TIME")}</Th>
                <Th>{t("WORK.START")}</Th>
              </Tr>
            </Thead>
            <Tbody>
              {data?.content.map((item) => (
                <Tr key={item.id}>
                  <Td>{item.name}</Td>
                  <Td>{item.wage}</Td>
                  <Td>{item.workMinutes}m</Td>
                  <Td>
                    <Button onClick={() => handleStartWork(item.id)}>
                      {t("WORK.START")}
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
        </Skeleton>
      </Box>
    </ChakraProvider>
  );
}

export default ShowWorks;
