/* Utility to format ISO dates into readable strings */
export function formatDate(isoString, options = {}) {
  if (!isoString) return '';
  const date = new Date(isoString);
  if (Number.isNaN(date.getTime())) return isoString;
  const defaultOptions = { year: 'numeric', month: 'short', day: 'numeric' };
  return date.toLocaleDateString(undefined, { ...defaultOptions, ...options });
}

export default formatDate;
