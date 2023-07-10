export const validateImageUrl = (path: string): boolean => {
  if (path === null || path.trim() === '') {
    return false; // Null or empty string
  }
  const directoryRegex = /^[^\\/]+$/;
  const hasDirectoryPath = directoryRegex.test(path);
  return hasDirectoryPath;
};

export const validateImageExtension = (path: string): boolean => {
  // Check if the file has an image extension
  const imageRegex = /\.(jpg|jpeg|png)$/i;
  const isValidImagePath = imageRegex.test(path);
  return isValidImagePath;
};
