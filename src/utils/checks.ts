export const validateImageUrl = (value: string): boolean => {
  if (value === null || value.trim() === '') {
    return false; // Null or empty string
  }
  return true;
};

export const validateImageExtension = (value: string): boolean => {
// Check if the file has an image extension
const imageExtensions = ['.jpg', '.jpeg', '.png'];
const fileExtension = value.substring(value.lastIndexOf('.')).toLowerCase();
if (!imageExtensions.includes(fileExtension)) {
  return false; // Invalid image file extension
}
  return true;
};