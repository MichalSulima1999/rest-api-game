import * as Yup from "yup";

export const LoginSchema = Yup.object().shape({
  username: Yup.string().required("Required"),
  password: Yup.string().min(8, "Password too short!").required("Required"),
});

export const RegisterSchema = Yup.object().shape({
    username: Yup.string().required("Required"),
    email: Yup.string().email("Not an email format").required("Required"),
    password: Yup.string().min(8, "Password too short!").required("Required"),
  });