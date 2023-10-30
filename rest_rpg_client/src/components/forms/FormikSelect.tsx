import {
  FormControl,
  FormErrorMessage,
  FormLabel,
  Select,
} from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import React from "react";

type SelectValue = {
  value: string | number;
  name: string;
};

type Props = {
  error?: string;
  touched?: boolean;
  value: string | number;
  handleChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
  selectValues?: SelectValue[];
  inputName: string;
  translationKey: string;
  isRequired?: boolean;
  placeholder?: string;
};

const FormikSelect = ({
  error,
  touched,
  value,
  handleChange,
  selectValues,
  inputName,
  translationKey,
  isRequired = true,
  placeholder,
}: Props) => {
  const { t } = useTranslation();

  return (
    <FormControl
      id={inputName}
      mb={6}
      isInvalid={error != null && touched}
      isRequired={isRequired}
    >
      <FormLabel>{t(`${translationKey}.NAME`)}</FormLabel>
      <Select
        name={inputName}
        onChange={handleChange}
        placeholder={placeholder}
      >
        {selectValues &&
          selectValues.map((val) => (
            <option
              style={{ background: "black" }}
              key={val.value}
              selected={value === val.value}
              value={val.value}
            >
              {val.name}
            </option>
          ))}
      </Select>
      <FormErrorMessage>{error && t(`VALIDATION.${error}`)}</FormErrorMessage>
    </FormControl>
  );
};

export default FormikSelect;
