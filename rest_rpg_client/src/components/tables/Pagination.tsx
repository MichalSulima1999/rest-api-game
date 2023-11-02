import React from "react";
import { Button, HStack } from "@chakra-ui/react";
import { ChevronLeftIcon, ChevronRightIcon } from "@chakra-ui/icons";

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (newPage: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
}) => {
  const pagesArray = Array.from({ length: totalPages }, (_, i) => i);

  return (
    <HStack spacing={2} justify="center">
      <Button
        isDisabled={currentPage === 1}
        onClick={() => onPageChange(currentPage - 1)}
      >
        <ChevronLeftIcon boxSize={6} />
      </Button>

      {pagesArray.map((page) => (
        <Button
          key={page}
          onClick={() => onPageChange(page)}
          colorScheme={page === currentPage ? "teal" : undefined}
        >
          {page + 1}
        </Button>
      ))}

      <Button
        isDisabled={currentPage === totalPages}
        onClick={() => onPageChange(currentPage + 1)}
      >
        <ChevronRightIcon boxSize={6} />
      </Button>
    </HStack>
  );
};

export default Pagination;
