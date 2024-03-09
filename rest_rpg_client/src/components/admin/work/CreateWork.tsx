import { WorkCreateRequest } from "../../../generated-sources/openapi";
import { useTranslation } from "react-i18next";
import { useFormik } from "formik";
import { Box, Button, Flex, HStack, Heading } from "@chakra-ui/react";
import FormikInput from "../../forms/FormikInput";
import useWorkService from "../../../services/useWorkService";
import {
  WorkCreateSchema,
  resourceTypes,
} from "../../../validation/work/WorkValidation";
import FormikRadioGroup from "../../forms/FormikRadioGroup";

const CreateWork = () => {
  const { t } = useTranslation();
  const workService = useWorkService();

  const handleSubmitFunc = async (values: WorkCreateRequest) => {
    await workService.createWork(values);
  };

  const { values, errors, touched, handleChange, handleSubmit } = useFormik({
    initialValues: {
      name: "",
      resourceType: resourceTypes[0],
      resourceAmount: 0,
      workMinutes: 0,
    },
    validationSchema: WorkCreateSchema,
    onSubmit: handleSubmitFunc,
  });

  return (
    <form onSubmit={handleSubmit}>
      <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
        <Box p={8} bg="blackAlpha.800" color="white" flex="1">
          <Heading mb={4}>{t("WORK.CREATE")}</Heading>
          <HStack spacing="8px" align={"end"}>
            <FormikInput
              error={errors.name}
              touched={touched.name}
              value={values.name}
              handleChange={handleChange}
              inputType="text"
              inputName="name"
              translationKey="WORK.NAME"
            />
          </HStack>
        </Box>
        <Box p={8} bg="blackAlpha.800" color="white" flex="1">
          <Heading mb={4}>{t("WORK.STATS")}</Heading>
          <HStack spacing="8px">
            <FormikInput
              error={errors.resourceAmount}
              touched={touched.resourceAmount}
              value={values.resourceAmount}
              handleChange={handleChange}
              inputType="number"
              inputName="resourceAmount"
              translationKey="WORK.RESOURCE_AMOUNT"
            />
            <FormikRadioGroup
              error={errors.resourceType}
              touched={touched.resourceType}
              value={values.resourceType}
              handleChange={handleChange}
              radioValues={resourceTypes}
              inputName="resourceType"
              translationKey="WORK.RESOURCE_TYPE"
            />
            <FormikInput
              error={errors.workMinutes}
              touched={touched.workMinutes}
              value={values.workMinutes}
              handleChange={handleChange}
              inputType="number"
              inputName="workMinutes"
              translationKey="WORK.WORK_TIME"
            />
          </HStack>
          <Button
            mt={4}
            colorScheme="teal"
            isLoading={workService.isLoading}
            type="submit"
          >
            {t("ADVENTURE.CREATE")}
          </Button>
        </Box>
      </Flex>
    </form>
  );
};

export default CreateWork;
