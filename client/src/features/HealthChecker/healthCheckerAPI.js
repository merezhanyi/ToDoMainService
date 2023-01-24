
export function fetchStatus() {
  return fetch("/healthcheck", {
    mode: "cors",
    "Content-Type": "text/plain",
  });
}
