import React from 'react';
import { JobSpec } from '../types/JobSpec';
import { JobSpecListItem } from './JobSpecListItem';

interface JobSpecListProps {
  jobSpecs: JobSpec[];
  submittingIds: number[];
  onView: (jobSpec: JobSpec) => void;
  onDelete: (id: number) => Promise<void>;
}

export function JobSpecList({
  jobSpecs,
  submittingIds,
  onView,
  onDelete,
}: JobSpecListProps) {
  const isSubmitting = (id: number) => submittingIds.includes(id);

  return (
    <div className="bg-white rounded-lg shadow-md mt-8">
      <h1 className="text-2xl font-bold p-6 border-b">Job Spec List</h1>
      {jobSpecs.length === 0 ? (
        <p className="p-6 text-gray-600">No job specs submitted yet.</p>
      ) : (
        <div>
          {jobSpecs.map((jobSpec) => (
            <JobSpecListItem
              key={jobSpec.id}
              jobSpec={{
                ...jobSpec,
                status: isSubmitting(jobSpec.id) ? 'pending' : jobSpec.status,
              }}
              onView={onView}
              onDelete={onDelete}
            />
          ))}
        </div>
      )}
    </div>
  );
}
