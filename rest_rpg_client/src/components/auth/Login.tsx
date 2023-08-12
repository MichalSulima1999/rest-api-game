/* eslint-disable @typescript-eslint/no-misused-promises */
import { useStores } from "../../store/RootStore";
import { AuthRequest } from "../../classes/auth/Auth";
import { auth } from "../../services/AuthService";
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
  Checkbox,
  useToast,
} from "@chakra-ui/react";
import { Form, Formik, FormikHelpers } from "formik";
import { LoginSchema } from "./AuthValidation";
import useToggle from "../../hooks/useToggle";

const Login = () => {
  const { authStore } = useStores();
  const [persist, togglePersist] = useToggle("persist", false);
  const toast = useToast()

  const handleSubmit = async (
    values: AuthRequest,
    actions: FormikHelpers<AuthRequest>
  ) => {
    await auth(values, authStore, toast);
    actions.setSubmitting(false);
  };

  return (
    <Flex color="white" w="100%" direction={{ base: "column", md: "row" }}>
      <Box p={8} bg="blackAlpha.800" color="white" flex="1">
        <Heading mb={4}>Login</Heading>
        <Formik
          initialValues={{
            username: "",
            password: "",
          }}
          validationSchema={LoginSchema}
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
              <FormControl>
                <Checkbox
                  onChange={(e) => togglePersist(e.target.checked)}
                  isChecked={persist}
                >
                  Remember me
                </Checkbox>
              </FormControl>
              <Button
                mt={4}
                colorScheme="teal"
                isLoading={isSubmitting}
                type="submit"
              >
                Login
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

export default Login;
