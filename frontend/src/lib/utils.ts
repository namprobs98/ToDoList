import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatDate(dateString: string | null): string {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  });
}

export function formatDateTime(dateString: string | null): string {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

export function getPriorityColor(priority: string): string {
  switch (priority) {
    case 'HIGH':
      return 'text-red-500 bg-red-50 dark:bg-red-950';
    case 'MEDIUM':
      return 'text-yellow-500 bg-yellow-50 dark:bg-yellow-950';
    case 'LOW':
      return 'text-green-500 bg-green-50 dark:bg-green-950';
    default:
      return 'text-gray-500 bg-gray-50 dark:bg-gray-950';
  }
}

export function getStatusColor(status: string): string {
  switch (status) {
    case 'COMPLETED':
      return 'text-green-500 bg-green-50 dark:bg-green-950';
    case 'IN_PROGRESS':
      return 'text-blue-500 bg-blue-50 dark:bg-blue-950';
    case 'TODO':
      return 'text-gray-500 bg-gray-50 dark:bg-gray-950';
    default:
      return 'text-gray-500 bg-gray-50 dark:bg-gray-950';
  }
}