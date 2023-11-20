import { AdventureCreateRequest } from "../../../generated-sources/openapi";
import { Box } from "@chakra-ui/react";
import useAdventureService from "../../../services/useAdventureService";
import AdventureForm from "./AdventureForm";

const CreateAdventure = () => {
  const adventureService = useAdventureService();

  const handleSubmitFunc = async (values: AdventureCreateRequest) => {
    await adventureService.create(values);
  };

  return (
    <Box>
      <AdventureForm
        handleSubmitFunc={handleSubmitFunc}
        headingText="ADVENTURE.CREATE"
      />
    </Box>
  );
};

export default CreateAdventure;
