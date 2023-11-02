/* eslint-disable @typescript-eslint/no-misused-promises */
import { Box, Divider, Flex, Link } from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { Link as RouterLink, useParams } from "react-router-dom";
import ErrorComponent from "../../main/ErrorComponent";

const HomeNavbar = () => {
  const { t } = useTranslation();
  const { characterId } = useParams();

  return (
    <Box bg="gray.900" color="gray.100" py={4} px={8} width="100vw">
      <Divider marginBottom={1} />
      {!characterId ? (
        <ErrorComponent />
      ) : (
        <>
          <Flex alignItems="center" justify={"space-around"}>
            <Link
              as={RouterLink}
              to={`/user/character/${characterId}/main`}
              fontSize="xl"
              fontWeight="bold"
            >
              {t("CHARACTER.MAIN")}
            </Link>
            <Link
              as={RouterLink}
              to={`/user/character/${characterId}/statistics`}
              fontSize="xl"
              fontWeight="bold"
            >
              {t("CHARACTER.STATISTICS.NAME")}
            </Link>
            <Link
              as={RouterLink}
              to={`/user/character/${characterId}/adventure/show`}
              fontSize="xl"
              fontWeight="bold"
            >
              {t("ADVENTURE.SHOW")}
            </Link>
          </Flex>
        </>
      )}
    </Box>
  );
};

export default HomeNavbar;
