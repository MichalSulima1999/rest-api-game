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
  Button,
} from "@chakra-ui/react";
import { AdventureBasicPage } from "../../../generated-sources/openapi";
import useAdventureService from "../../../services/useAdventureService";
import Pagination from "../../tables/Pagination";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

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

function AdminShowAdventures() {
  const { t } = useTranslation();
  const adventureService = useAdventureService();
  const [page, setPage] = useState(0);
  const [data, setData] = useState<AdventureBasicPage | null>(null);

  useEffect(() => {
    getAdventurePage().catch((error) => console.log(error));
  }, [page]);

  async function getAdventurePage() {
    const adventures = await adventureService.findAdventures({
      pagination: { pageNumber: page, elements: pageSize },
    });
    if (adventures) {
      setData(adventures);
    }
  }

  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  const handleDeleteAdventure = (adventureId: number) => {
    adventureService
      .deleteAdventure(adventureId)
      .catch((e) => console.log(e))
      .finally(() => {
        getAdventurePage().catch((error) => console.log(error));
      });
  };

  return (
    <ChakraProvider theme={theme}>
      <Box p={4} bgColor={"gray.800"} color={"white"}>
        <Skeleton isLoaded={!adventureService.isLoading}>
          <Table variant="simple" bgColor={"gray.900"} size="md" mb={4}>
            <Thead>
              <Tr>
                <Th>{t("ADVENTURE.NAME")}</Th>
                <Th>{t("CHARACTER.STATISTICS.XP")}</Th>
                <Th>{t("CHARACTER.STATISTICS.GOLD")}</Th>
                <Th>{t("ENEMY.NAME")}</Th>
                <Th>{t("ADVENTURE.EDIT")}</Th>
                <Th>{t("ADVENTURE.DELETE")}</Th>
              </Tr>
            </Thead>
            <Tbody>
              {data?.content.map((item, index) => (
                <Tr key={index}>
                  <Td>{item.name}</Td>
                  <Td>{item.xpForAdventure}</Td>
                  <Td>{item.goldForAdventure}</Td>
                  <Td>{item.enemy?.name}</Td>
                  <Td>
                    <Button as={Link} to={`/admin/adventure/${item.id}/edit`}>
                      {t("ADVENTURE.EDIT")}
                    </Button>
                  </Td>
                  <Td>
                    <Button onClick={() => handleDeleteAdventure(item.id)}>
                      {t("ADVENTURE.DELETE")}
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

export default AdminShowAdventures;
