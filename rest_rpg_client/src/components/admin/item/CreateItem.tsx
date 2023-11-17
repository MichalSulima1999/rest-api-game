import { ItemCreateRequest } from "../../../generated-sources/openapi";
import { useTranslation } from "react-i18next";
import { useFormik } from "formik";
import { Box, Button, Flex, HStack, Heading } from "@chakra-ui/react";
import FormikInput from "../../forms/FormikInput";
import FormikRadioGroup from "../../forms/FormikRadioGroup";
import {
  ItemCreateSchema,
  itemTypes,
} from "../../../validation/item/ItemValidation";
import useEquipmentService from "../../../services/useEquipmentService";

const CreateItem = () => {
  const { t } = useTranslation();
  const equipmentService = useEquipmentService();

  const handleSubmitFunc = async (values: ItemCreateRequest) => {
    await equipmentService.create(values);
  };

  const { values, errors, touched, handleChange, handleSubmit } = useFormik({
    initialValues: {
      name: "",
      type: itemTypes[0],
      power: 0,
      price: 0,
    },
    validationSchema: ItemCreateSchema,
    onSubmit: handleSubmitFunc,
  });

  return (
    <form onSubmit={handleSubmit}>
      <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
        <Box p={8} bg="blackAlpha.800" color="white" flex="1">
          <Heading mb={4}>{t("ITEM.CREATE")}</Heading>
          <HStack spacing="8px" align={"end"}>
            <FormikInput
              error={errors.name}
              touched={touched.name}
              value={values.name}
              handleChange={handleChange}
              inputType="text"
              inputName="name"
              translationKey="ITEM.NAME"
            />
            <FormikRadioGroup
              error={errors.type}
              touched={touched.type}
              value={values.type}
              handleChange={handleChange}
              radioValues={itemTypes}
              inputName="type"
              translationKey="ITEM.TYPE"
            />
          </HStack>
        </Box>
        <Box p={8} bg="blackAlpha.800" color="white" flex="1">
          <Heading mb={4}>{t("ITEM.STATS")}</Heading>
          <HStack spacing="8px">
            <FormikInput
              error={errors.power}
              touched={touched.power}
              value={values.power}
              handleChange={handleChange}
              inputType="number"
              inputName="power"
              translationKey="ITEM.POWER"
              isRequired={false}
            />
            <FormikInput
              error={errors.price}
              touched={touched.price}
              value={values.price}
              handleChange={handleChange}
              inputType="number"
              inputName="price"
              translationKey="ITEM.PRICE"
              isRequired={false}
            />
          </HStack>
          <Button
            mt={4}
            colorScheme="teal"
            isLoading={equipmentService.isLoading}
            type="submit"
          >
            {t("ITEM.CREATE")}
          </Button>
        </Box>
      </Flex>
    </form>
  );
};

export default CreateItem;
