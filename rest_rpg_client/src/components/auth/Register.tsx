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
  useToast,
} from "@chakra-ui/react";
import { Form, Formik, FormikHelpers } from "formik";
import { RegisterSchema } from "./AuthValidation";
import { register } from "../../services/AuthService";
import { RegisterRequest } from "../../classes/auth/Auth";

const Register = () => {
  const toast = useToast();

  const handleSubmit = async (
    values: RegisterRequest,
    actions: FormikHelpers<RegisterRequest>
  ) => {
    await register(values, toast);
    actions.setSubmitting(false);
  };

  return (
    <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
      <Box p={8} bg="blackAlpha.800" color="white" flex="1">
        <Heading mb={4}>Register</Heading>
        <Formik
          initialValues={{
            username: "",
            email: "",
            password: "",
            role: "USER",
          }}
          validationSchema={RegisterSchema}
          onSubmit={(values, actions) => handleSubmit(values, actions)}
        >
          {({ isSubmitting, values, errors, touched, handleChange }) => (
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
                <FormLabel>Username</FormLabel>
                <Input
                  name="username"
                  type="text"
                  value={values.username}
                  onChange={handleChange}
                  placeholder="Username"
                />
                <FormErrorMessage>{errors.username}</FormErrorMessage>
              </FormControl>
              <FormControl
                id="email"
                mb={4}
                isInvalid={
                  (errors.email as string) != null && (touched.email as boolean)
                }
                isRequired
              >
                <FormLabel>Email</FormLabel>
                <Input
                  name="email"
                  type="email"
                  value={values.email}
                  onChange={handleChange}
                  placeholder="Email"
                />
                <FormErrorMessage>{errors.email}</FormErrorMessage>
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
                <FormLabel>Password</FormLabel>
                <Input
                  name="password"
                  type="password"
                  value={values.password}
                  onChange={handleChange}
                  placeholder="Password"
                />
                <FormErrorMessage>{errors.password}</FormErrorMessage>
              </FormControl>
              <Button
                mt={4}
                colorScheme="teal"
                isLoading={isSubmitting}
                type="submit"
              >
                Register
              </Button>
            </Form>
          )}
        </Formik>
      </Box>
      <Box position="relative" p={8} bg="blackAlpha.800" color="white" flex="2">
        <AbsoluteCenter axis="both">
          <Heading>REST RPG</Heading>
        </AbsoluteCenter>
      </Box>
    </Flex>
  );
};

export default Register;
