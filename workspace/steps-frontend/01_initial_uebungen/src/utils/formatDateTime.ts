export function formatDateTime(isoDate: string): string {
  const data = new Date(Date.parse(isoDate));
  return `${data.toLocaleDateString()} ${data.toLocaleTimeString()}`;
}

export function formatLongDate(date: string) {
  return new Intl.DateTimeFormat(undefined, {
    month: "long",
    day: "2-digit",
    year: "2-digit",
  }).format(new Date(date));
}

export function formatShortDate(date: string) {
  return new Intl.DateTimeFormat(undefined, {
    month: "short",
    day: "2-digit",
    year: "2-digit",
  }).format(new Date(date));
}
