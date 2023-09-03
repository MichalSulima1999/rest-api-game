/* eslint-disable @typescript-eslint/no-misused-promises */
import {
  Box,
  Flex,
} from "@chakra-ui/react";
import CharactersPopover from "./CharactersPopover";

const HomeNavbar = () => {
  return (
    <Box bg="gray.900" color="gray.100" py={4} px={8} width="100vw">
        <Flex alignItems="center">
          <CharactersPopover />
        </Flex>
    </Box>
  );
};

export default HomeNavbar;
