import * as Yup from "yup";

export const LoginSchema = Yup.object().shape({
  username: Yup.string().required("REQUIRED"),
  password: Yup.string().min(8, "PASSWORD_SHORT").required("REQUIRED"),
});

export const RegisterSchema = Yup.object().shape({
  username: Yup.string().required("REQUIRED"),
  email: Yup.string().email("NOT_EMAIL").required("REQUIRED"),
  password: Yup.string().min(8, "PASSWORD_SHORT").required("REQUIRED"),
});
