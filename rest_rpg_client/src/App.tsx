import "./App.css";
import { ChakraProvider, CSSReset } from "@chakra-ui/react";
import Navbar from "./components/pages/Navbar";
import Pages from "./components/pages/Pages";

function App() {
  return (
    <ChakraProvider>
      <CSSReset />
      <Navbar />
      <Pages />
    </ChakraProvider>
  );
}

export default App;
