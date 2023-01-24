export function fetchList() {
  return fetch("/api/v1/tasks/", {
    mode: "cors",
  });
}

export function requestDeleteItem(id) {
  return fetch(`/api/v1/tasks/${id}`, { mode: "cors", method: "delete" });
}

export function requestUpdateItem(item) {
  return fetch(`/api/v1/tasks/${item.id}`, {
    body: JSON.stringify(item),
    headers: {
      "Content-Type": "application/json",
    },
    mode: "cors",
    method: "put",
  });
}

export function requestAddItem(description) {
  const today = new Date();

  return fetch(`/api/v1/tasks/`, {
    body: JSON.stringify({
      description,
      date: today.toISOString(),
      done: false,
    }),
    headers: {
      "Content-Type": "application/json",
    },
    mode: "cors",
    method: "post",
  });
}
