import React from 'react';
import { JobSpec } from '../types/JobSpec';

interface JobSpecListItemProps {
  jobSpec: JobSpec;
  onView: (jobSpec: JobSpec) => void;
  onDelete: (id: number) => Promise<void>;
}

export function JobSpecListItem({
  jobSpec,
  onView,
  onDelete,
}: JobSpecListItemProps) {
  const renderStatus = () => {
    if (jobSpec.status === 'pending') {
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
    return jobSpec.status ?? '-';
  };

  const renderValue = (value: any) => {
    if (value === null || value === undefined) return '-';
    if (typeof value === 'number') return `${value}`;
    return value;
  };

  return (
    <div className="border-b last:border-b-0 py-3 px-4 hover:bg-gray-50">
      <div className="grid grid-cols-[100px,1fr,1fr,1fr,80px,120px,1fr,auto] gap-2 items-center text-sm">
        <span>
          <strong>ID:</strong> {jobSpec.id}
        </span>
        <span>CV: {renderValue(jobSpec.cvId)}</span>
        <span>Location: {renderValue(jobSpec.location)}</span>
        <span>Salary: ${renderValue(jobSpec.salary)}</span>
        <span className="text-center">Score: {renderValue(jobSpec.score)}</span>
        <span>Company: {renderValue(jobSpec.company)}</span>
        <span>Job Title: {renderValue(jobSpec.jobTitle)}</span>
        <div className="flex items-center justify-between gap-2">
          <span>
            <strong>Status:</strong> {renderStatus()}
          </span>
          <button
            onClick={() => onView(jobSpec)}
            className="bg-blue-600 text-white px-3 py-1.5 rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            View
          </button>
          <button
            onClick={() => onDelete(jobSpec.id)}
            className="bg-red-600 text-white px-3 py-1.5 rounded hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}
