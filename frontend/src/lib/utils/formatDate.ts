import type { Maybe } from "$lib/generated/graphql";
import dayjs from "dayjs";

export function formatDate(node: HTMLElement) {
  node.innerText = dayjs(node.innerText).format("hh:mm DD.MM.YYYY");
}

export function stringFormatDate(date: Maybe<string> | undefined): string {
  return dayjs(date).format("DD.MM.YYYY HH:mm");
}