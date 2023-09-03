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
import { useEffect, useState } from "react";
import useCharacterService, {
  THUMBNAIL_URL,
} from "../../services/useCharacterService";
import { useTranslation } from "react-i18next";
import { CharacterBasics } from "../../generated-sources/openapi";
import { Link } from "react-router-dom";

const CharactersPopover = () => {
  const [characters, setCharacters] = useState<CharacterBasics | undefined>();
  const { t } = useTranslation();
  const characterService = useCharacterService();

  useEffect(() => {
    async function getCharacters() {
      setCharacters(await characterService.getUserCharacters());
    }
    getCharacters().catch((error) => console.log(error));
  }, []);

  return (
    <Box>
      <Popover placement="auto">
        <PopoverTrigger>
          <Button isLoading={characterService.isLoading}>
            {t("HOME_NAVBAR.CHARACTERS")}
          </Button>
        </PopoverTrigger>
        <PopoverContent width="70vw" bg="blackAlpha.800" color="white">
          <PopoverArrow />
          <PopoverCloseButton />
          <PopoverHeader>{t("CHARACTER.CHOOSE_ARTWORK")}</PopoverHeader>
          <PopoverBody>
            <Skeleton height="40px" isLoaded={!characterService.isLoading}>
              <Grid templateColumns="repeat(3, 1fr)" gap={3}>
                {!characterService.isLoading &&
                  characters?.content &&
                  characters.content.map((character) => (
                    <GridItem w="100%">
                      <Link to={`/home/character/${character.id}`}>
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
