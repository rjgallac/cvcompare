import React from 'react';
import { JobSpec } from '../types/JobSpec';

interface JobSpecModalProps {
  jobSpec: JobSpec;
  onClose: () => void;
}

export function JobSpecModal({ jobSpec, onClose }: JobSpecModalProps) {
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
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white p-6 rounded-lg max-w-[700px] w-[90%] max-h-[80vh] overflow-auto flex flex-col">
        <h2 className="text-xl font-semibold mb-4">Job Spec Details</h2>
        <div className="mb-3">
          <strong>ID:</strong> {jobSpec.id}
        </div>
        <div className="mb-3">
          <strong>CV ID:</strong> {renderValue(jobSpec.cvId)}
        </div>
        <div className="mb-3">
          <strong>Job Title:</strong> {renderValue(jobSpec.jobTitle)}
        </div>
        <div className="mb-3">
          <strong>Company:</strong> {renderValue(jobSpec.company)}
        </div>
        <div className="mb-3">
          <strong>Location:</strong> {renderValue(jobSpec.location)}
        </div>
        <div className="mb-3">
          <strong>Salary:</strong> ${renderValue(jobSpec.salary)}
        </div>
        <div className="mb-3">
          <strong>Score:</strong> {renderValue(jobSpec.score)}
        </div>
        <div className="mb-3">
          <strong>Status:</strong>{' '}
          {jobSpec.status === 'pending' ? (
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
          ) : (
            renderValue(jobSpec.status)
          )}
        </div>
        <div className="mb-3">
          <strong>Job Spec Content:</strong>
          <pre className="bg-gray-100 p-3 rounded mt-1 whitespace-pre-wrap break-word flex-grow overflow-auto max-h-[40vh]">
            {jobSpec.job_spec_content}
          </pre>
        </div>
        <button
          onClick={onClose}
          className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 mt-auto"
        >
          Close
        </button>
      </div>
    </div>
  );
}
