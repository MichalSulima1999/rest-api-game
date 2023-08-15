/* eslint-disable @typescript-eslint/no-misused-promises */
import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Input,
  Heading,
  FormErrorMessage,
  Flex,
  AbsoluteCenter,
} from "@chakra-ui/react";
import { Form, Formik } from "formik";
import { RegisterSchema } from "../../validation/auth/AuthValidation";
import { RegisterRequest } from "../../classes/auth/Auth";
import { useTranslation } from "react-i18next";
import useAuthService from "../../services/useAuthService";

const Register = () => {
  const { t } = useTranslation();
  const authService = useAuthService();

  const handleSubmit = async (values: RegisterRequest) => {
    await authService.register(values);
  };

  return (
    <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
      <Box p={8} bg="blackAlpha.800" color="white" flex="1">
        <Heading mb={4}>{t("AUTH.REGISTER")}</Heading>
        <Formik
          initialValues={{
            username: "",
            email: "",
            password: "",
            role: "USER",
          }}
          validationSchema={RegisterSchema}
          onSubmit={handleSubmit}
        >
          {({ values, errors, touched, handleChange }) => (
            <Form>
              <FormControl
                id="username"
                mb={4}
                isInvalid={
                  (errors.username as string) != null &&
                  (touched.username as boolean)
                }
                isRequired
              >
                <FormLabel>{t("AUTH.USERNAME")}</FormLabel>
                <Input
                  name="username"
                  type="text"
                  value={values.username}
                  onChange={handleChange}
                  placeholder={t("AUTH.USERNAME")}
                />
                <FormErrorMessage>
                  {errors.username && t(`VALIDATION.${errors.username}`)}
                </FormErrorMessage>
              </FormControl>
              <FormControl
                id="email"
                mb={4}
                isInvalid={
                  (errors.email as string) != null && (touched.email as boolean)
                }
                isRequired
              >
                <FormLabel>{t("AUTH.EMAIL")}</FormLabel>
                <Input
                  name="email"
                  type="email"
                  value={values.email}
                  onChange={handleChange}
                  placeholder={t("AUTH.EMAIL")}
                />
                <FormErrorMessage>
                  {errors.email && t(`VALIDATION.${errors.email}`)}
                </FormErrorMessage>
              </FormControl>
              <FormControl
                id="password"
                mb={6}
                isInvalid={
                  (errors.password as string) != null &&
                  (touched.password as boolean)
                }
                isRequired
              >
                <FormLabel>{t("AUTH.PASSWORD")}</FormLabel>
                <Input
                  name="password"
                  type="password"
                  value={values.password}
                  onChange={handleChange}
                  placeholder={t("AUTH.PASSWORD")}
                />
                <FormErrorMessage>
                  {errors.password && t(`VALIDATION.${errors.password}`)}
                </FormErrorMessage>
              </FormControl>
              <Button
                mt={4}
                colorScheme="teal"
                isLoading={authService.isLoading}
                type="submit"
              >
                {t("AUTH.REGISTER")}
              </Button>
            </Form>
          )}
        </Formik>
      </Box>
      <Box position="relative" p={8} bg="blackAlpha.800" color="white" flex="2">
        <AbsoluteCenter axis="both">
          <Heading>{t("NAVBAR.TITLE")}</Heading>
        </AbsoluteCenter>
      </Box>
    </Flex>
  );
};

export default Register;
