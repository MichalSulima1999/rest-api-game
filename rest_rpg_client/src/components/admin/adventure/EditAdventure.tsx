import {
  AdventureCreateRequest,
  AdventureDetails,
} from "../../../generated-sources/openapi";
import { Box, Skeleton } from "@chakra-ui/react";
import useAdventureService from "../../../services/useAdventureService";
import AdventureForm from "./AdventureForm";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";

const EditAdventure = () => {
  const adventureService = useAdventureService();
  const { adventureId } = useParams();
  const [adventure, setAdventure] = useState<AdventureDetails>();

  useEffect(() => {
    async function getAdventure() {
      if (adventureId) {
        const adventure = await adventureService.getAdventure(
          Number(adventureId)
        );
        if (adventure) {
          setAdventure(adventure);
        }
      }
    }
    getAdventure().catch((error) => console.log(error));
  }, []);

  const handleSubmitFunc = async (values: AdventureCreateRequest) => {
    if (adventureId) {
      await adventureService.editAdventure(Number(adventureId), values);
    }
  };

  return (
    <Box>
      <Skeleton isLoaded={!adventureService.isLoading}>
        <AdventureForm
          handleSubmitFunc={handleSubmitFunc}
          headingText="ADVENTURE.EDIT"
          initialValues={adventure}
        />
      </Skeleton>
    </Box>
  );
};

export default EditAdventure;
