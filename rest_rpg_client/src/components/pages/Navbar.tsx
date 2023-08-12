import { Box, Flex, Link } from "@chakra-ui/react";
import { Link as RouterLink } from "react-router-dom";
import { useStores } from "../../store/RootStore";
import { useEffect, useState } from "react";
import { observer } from "mobx-react";

const Navbar = observer(() => {
  const [logged, setLogged] = useState(false);
  const { authStore } = useStores();

  useEffect(() => {
    setLogged(authStore.accessToken != "");
  }, [authStore.accessToken])

  return (
    <Box bg="gray.900" color="gray.100" py={4} px={8} width="100vw">
      <Flex alignItems="center" justifyContent="space-between">
        <Link as={RouterLink} to="/" fontSize="xl" fontWeight="bold">
          Rest RPG
        </Link>
        <Flex alignItems="center">
          <Link as={RouterLink} to="/home" mx={2}>
            Home
          </Link>
          {authStore.username}
          {!logged && (
            <>
              <Link as={RouterLink} to="/register" mx={2}>
                Register
              </Link>
              <Link as={RouterLink} to="/login" mx={2}>
                Login
              </Link>
            </>
          )}
          {logged && (
            <Link as={RouterLink} to="/logout" mx={2}>
              Logout
            </Link>
          )}
        </Flex>
      </Flex>
    </Box>
  );
});

export default Navbar;
