import { useToast } from "@chakra-ui/react";

const useSuccessToast = () => {
  const toast = useToast();

  const showSuccessToast = (message: string) => {
    toast({
      title: message,
      status: "success",
      isClosable: true,
    });
  };

  return showSuccessToast;
};

export default useSuccessToast;
