/* Common form validators used across the app */
export const required = (msg = 'Campo obrigatório') => (value) => {
  if (value === null || value === undefined) return msg;
  if (typeof value === 'string' && value.trim() === '') return msg;
  if (Array.isArray(value) && value.length === 0) return msg;
  return undefined;
};

export const minLength = (min, msg) => (value) => {
  if (value && value.length < min) return msg || `Mínimo ${min} caracteres`;
  return undefined;
};

export const email = (msg = 'Email inválido') => (value) => {
  if (!value) return undefined; // let required() handle presence
  const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return pattern.test(value) ? undefined : msg;
};

export const fileSizeMax = (maxBytes, msg) => (file) => {
  if (!file) return undefined;
  if (file.size && file.size > maxBytes) return msg || `Tamanho máximo ${maxBytes} bytes`;
  return undefined;
};

export const fileTypeAllowed = (types = [], msg = 'Tipo de arquivo inválido') => (file) => {
  if (!file) return undefined;
  return types.length === 0 || types.includes(file.type) ? undefined : msg;
};

export default {
  required,
  minLength,
  email,
  fileSizeMax,
  fileTypeAllowed,
};
