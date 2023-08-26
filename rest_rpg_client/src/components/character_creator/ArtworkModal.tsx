import {
  Box,
  Button,
  Center,
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
} from "@chakra-ui/react";
import { useEffect, useState } from "react";
import useCharacterService, {
  THUMBNAIL_URL,
} from "../../services/useCharacterService";
import { useTranslation } from "react-i18next";
import { FormikErrors } from "formik";
import { CreateCharacterFormData } from "./CharacterCreator";

interface Props {
  setFieldValue: (
    field: string,
    value: string,
    shouldValidate?: boolean | undefined
  ) => Promise<void> | Promise<FormikErrors<CreateCharacterFormData>>;
}

const ArtworkModal = ({ setFieldValue }: Props) => {
  const [artworks, setArtworks] = useState([""]);
  const [artwork, setArtwork] = useState("");
  const characterService = useCharacterService();
  const { t } = useTranslation();

  useEffect(() => {
    async function getArtworks() {
      setArtworks(await characterService.getArtworksEnum());
    }
    getArtworks().catch((error) => console.log(error));
  }, []);

  useEffect(() => {
    setFieldValue("artwork", artwork).catch((err) => console.log(err));
  }, [artwork, setFieldValue]);

  const handleChangeArtwork = (artwork: string) => {
    setArtwork(artwork);
  };

  return (
    <Box>
      <Popover placement="auto">
        <PopoverTrigger>
          <Button isLoading={characterService.isLoading}>
            {t("CHARACTER.ARTWORK")}
          </Button>
        </PopoverTrigger>
        <PopoverContent width="70vw" bg="blackAlpha.800" color="white">
          <PopoverArrow />
          <PopoverCloseButton />
          <PopoverHeader>{t("CHARACTER.CHOOSE_ARTWORK")}</PopoverHeader>
          <PopoverBody>
            <Skeleton height="40px" isLoaded={!characterService.isLoading}>
              <Grid templateColumns="repeat(6, 1fr)" gap={3}>
                {!characterService.isLoading && artworks.map((art) => (
                  <GridItem w="100%" border={art === artwork ? "1px": "0px"}>
                    <button type="button" onClick={() => handleChangeArtwork(art)}>
                      <Image src={`${THUMBNAIL_URL}/${art}`} />
                    </button>
                  </GridItem>
                ))}
              </Grid>
            </Skeleton>
          </PopoverBody>
        </PopoverContent>
      </Popover>
      {artwork && (
        <Center p={3}>
          <Image maxW="75%" src={`${THUMBNAIL_URL}/${artwork}`} />
        </Center>
      )}
    </Box>
  );
};

export default ArtworkModal;
