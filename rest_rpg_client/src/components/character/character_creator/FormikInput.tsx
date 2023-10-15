import {
  FormControl,
  FormErrorMessage,
  FormLabel,
  Input,
} from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import React from "react";

type Props = {
  error?: string;
  touched?: boolean;
  value: string | number;
  handleChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  inputType: "text" | "number";
  inputName: string;
  translationKey: string;
};

const FormikInput = ({
  error,
  touched,
  value,
  handleChange,
  inputType,
  inputName,
  translationKey,
}: Props) => {
  const { t } = useTranslation();
  return (
    <FormControl
      id={inputName}
      mb={4}
      isInvalid={error != null && touched}
      isRequired
    >
      <FormLabel>{t(translationKey)}</FormLabel>
      <Input
        name={inputName}
        type={inputType}
        value={value}
        onChange={handleChange}
        placeholder={t(translationKey)}
      />
      <FormErrorMessage>{error && t(`VALIDATION.${error}`)}</FormErrorMessage>
    </FormControl>
  );
};

export default FormikInput;
