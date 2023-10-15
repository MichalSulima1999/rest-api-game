import {
  Box,
  Button,
  Grid,
  GridItem,
  Image,
  Popover,
  PopoverArrow,
  PopoverBody,
  PopoverCloseButton,
  PopoverContent,
  PopoverHeader,
  PopoverTrigger,
  Skeleton,
  Text,
} from "@chakra-ui/react";
import { useState } from "react";
import useCharacterService, {
  THUMBNAIL_URL,
} from "../../services/useCharacterService";
import { useTranslation } from "react-i18next";
import { CharacterBasics } from "../../generated-sources/openapi";
import { Link } from "react-router-dom";

const CharactersPopover = () => {
  const [characters, setCharacters] = useState<CharacterBasics | undefined>();
  const [loadCharacters, setLoadCharacters] = useState<boolean>(true);
  const { t } = useTranslation();
  const characterService = useCharacterService();

  const getCharacters = async () => {
    if (loadCharacters) {
      setCharacters(await characterService.getUserCharacters());
    }
    setLoadCharacters(!loadCharacters);
  };

  return (
    <Box>
      <Popover
        placement="left"
        onOpen={() => {
          void getCharacters();
        }}
      >
        <PopoverTrigger>
          <Button isLoading={characterService.isLoading}>
            {t("HOME_NAVBAR.CHARACTERS")}
          </Button>
        </PopoverTrigger>
        <PopoverContent bg="blackAlpha.800" color="white">
          <PopoverArrow />
          <PopoverCloseButton />
          <PopoverHeader>{t("HOME_NAVBAR.CHARACTERS")}</PopoverHeader>
          <PopoverBody>
            <Skeleton isLoaded={!characterService.isLoading}>
              <Grid templateColumns="repeat(3, 1fr)" gap={3}>
                {!characterService.isLoading &&
                  characters?.content &&
                  characters.content.map((character) => (
                    <GridItem>
                      <Link to={`/character/${character.id}/main`}>
                        <Box bg="blackAlpha.900">
                          <Text fontSize="xl">{character.name}</Text>
                          <Text fontSize="md">
                            {t(`CHARACTER.CLASS.${character.characterClass}`)}{" "}
                            {t("CHARACTER.LEVEL")}{" "}
                            {character.statistics.currentLevel}
                          </Text>
                          <Image
                            src={`${THUMBNAIL_URL}/${character.artwork}`}
                          />
                        </Box>
                      </Link>
                    </GridItem>
                  ))}
              </Grid>
            </Skeleton>
          </PopoverBody>
        </PopoverContent>
      </Popover>
    </Box>
  );
};

export default CharactersPopover;
