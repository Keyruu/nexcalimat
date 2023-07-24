import dayjs from "dayjs";

export function formatDate(node: HTMLElement) {
  node.innerText = dayjs(node.innerText).format("hh:mm DD.MM.YYYY");
}