import React from "react";
import {
  Grid,
  GridItem,
  Image,
  ChakraProvider,
  extendTheme,
  Box,
} from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";

const theme = extendTheme({
  components: {
    Button: {
      baseStyle: {
        p: 4,
        boxShadow: "md",
        rounded: "md",
      },
    },
  },
});

interface TileProps {
  title: string;
  imageUrl: string;
}

function Tile({ title, imageUrl }: TileProps) {
  return (
    <Box maxW="sm" borderWidth="1px" borderRadius="lg" overflow="hidden">
      <Link to="/admin/enemy/create">
        <Image src={imageUrl} alt={title} />
        <Box p="6">
          <Box
            mt="1"
            fontWeight="semibold"
            as="h4"
            lineHeight="tight"
            noOfLines={1}
          >
            {title}
          </Box>
        </Box>
      </Link>
    </Box>
  );
}

const AdminHome = () => {
  const { t } = useTranslation();

  const tiles: TileProps[] = [
    {
      title: t("ADMIN.ENEMY.CREATE"),
      imageUrl: "https://cdn-icons-png.flaticon.com/512/1477/1477179.png",
    },
    { title: "Tile 2", imageUrl: "url_do_obrazka_2.jpg" },
    { title: "Tile 3", imageUrl: "url_do_obrazka_3.jpg" },
  ];

  return (
    <ChakraProvider theme={theme}>
      <Grid bg="blackAlpha.800" templateColumns="repeat(3, 1fr)" gap={4}>
        {tiles.map((tile, index) => (
          <GridItem key={index}>
            <Tile title={tile.title} imageUrl={tile.imageUrl} />
          </GridItem>
        ))}
      </Grid>
    </ChakraProvider>
  );
};

export default AdminHome;
