/* Centralized upload path reference, can use Vite env var if provided. */
const UPLOAD_PATH = import.meta.env?.VITE_UPLOAD_URL || '/uploads';

export default UPLOAD_PATH;
