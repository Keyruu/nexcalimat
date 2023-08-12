import type { ToastSettings } from "@skeletonlabs/skeleton";

export function toastSuccess(message: string): ToastSettings {
  return {
    message,
    classes: 'text-green-300',
    background: 'variant-glass-success'
  };
}

export function toastError(message: string): ToastSettings {
  return {
    message,
    classes: 'text-red-300',
    background: 'variant-glass-error'
  };
}