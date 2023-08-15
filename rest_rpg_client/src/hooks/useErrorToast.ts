import { useToast } from "@chakra-ui/react";

const useErrorToast = () => {
  const toast = useToast();

  const showErrorToast = (errorMessage: string) => {
    toast({
      title: errorMessage,
      status: "error",
      isClosable: true,
    });
  };

  return showErrorToast;
};

export default useErrorToast;
