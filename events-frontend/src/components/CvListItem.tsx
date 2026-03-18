import React from 'react';
import { Cv } from '../types/Cv';

interface CvListItemProps {
  cv: Cv;
  onView: (cv: Cv) => void;
  onDelete: (id: number) => Promise<void>;
  onViewSuggestions?: (cv: Cv) => Promise<void>;
}

export function CvListItem({
  cv,
  onView,
  onDelete,
  onViewSuggestions,
}: CvListItemProps) {
  const renderStatus = () => {
    if (cv.status === 'pending') {
      return (
        <div className="flex items-center space-x-2 text-blue-600">
          <svg
            className="animate-spin h-4 w-4"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              className="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              strokeWidth="4"
            ></circle>
            <path
              className="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
            ></path>
          </svg>
          <span>Pending</span>
        </div>
      );
    }
    return cv.status ?? '-';
  };

  return (
    <div className="border-b last:border-b-0 py-3 px-4 hover:bg-gray-50">
      <div className="grid grid-cols-[100px,1fr] gap-2 items-center text-sm">
        <div>
          <strong>ID:</strong> {cv.id}
          <br />
          <strong>Status:</strong> {renderStatus()}
        </div>
        <div className="flex items-center justify-between gap-2">
          <span>
            <strong>Name:</strong> {cv.name}
          </span>
          <button
            onClick={() => onView(cv)}
            className="bg-blue-600 text-white px-3 py-1.5 rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            View
          </button>
          <button
            onClick={() => onDelete(cv.id)}
            className="bg-red-600 text-white px-3 py-1.5 rounded hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500"
          >
            Delete
          </button>
          <button
            onClick={() => onViewSuggestions?.(cv)}
            className="bg-purple-600 text-white px-3 py-1.5 rounded hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500"
          >
            View Suggestions
          </button>
        </div>
      </div>
    </div>
  );
}
