import type { Maybe } from "$lib/generated/graphql";
import { env } from "$env/dynamic/public";
import dayjs from "dayjs";

export function formatDate(node: HTMLElement) {
  node.innerText = dayjs(node.innerText).format("hh:mm DD.MM.YYYY");
}

export function stringFormatDate(date: Maybe<string> | undefined): string {
  return dayjs.tz(date, env.PUBLIC_TIMEZONE).format("DD.MM.YYYY HH:mm");
}