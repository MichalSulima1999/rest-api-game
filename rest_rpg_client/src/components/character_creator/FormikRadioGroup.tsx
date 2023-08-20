import {
  FormControl,
  FormErrorMessage,
  FormLabel,
  Radio,
  RadioGroup,
  Stack,
} from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import React from "react";

type Props = {
  error?: string;
  touched?: boolean;
  value: string;
  handleChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  radioValues: string[];
  inputName: string;
  translationKey: string;
};

const FormikRadioGroup = ({
  error,
  touched,
  value,
  handleChange,
  radioValues,
  inputName,
  translationKey,
}: Props) => {
  const { t } = useTranslation();

  return (
    <FormControl
      id={inputName}
      mb={6}
      isInvalid={error != null && touched}
      isRequired
    >
      <FormLabel>{t(`${translationKey}.NAME`)}</FormLabel>
      <RadioGroup value={value}>
        <Stack direction="row">
          {radioValues.map((val) => (
            <Radio
              key={val}
              onChange={handleChange}
              name={inputName}
              value={val}
            >
              {t(`${translationKey}.${val}`)}
            </Radio>
          ))}
        </Stack>
      </RadioGroup>
      <FormErrorMessage>{error && t(`VALIDATION.${error}`)}</FormErrorMessage>
    </FormControl>
  );
};

export default FormikRadioGroup;
