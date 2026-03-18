import React from 'react';
import { Cv } from '../types/Cv';
import { CvListItem } from './CvListItem';

interface CvListProps {
  cvs: Cv[];
  submittingIds: number[];
  onView: (cv: Cv) => void;
  onDelete: (id: number) => Promise<void>;
  onViewSuggestions: (cv: Cv) => Promise<void>;
}

export function CvList({
  cvs,
  submittingIds = [],
  onView,
  onDelete,
  onViewSuggestions,
}: CvListProps) {
  const isSubmitting = (id: number) => submittingIds.includes(id);
  return (
    <div className="bg-white rounded-lg shadow-md mt-8">
      <h1 className="text-2xl font-bold p-6 border-b">CV List</h1>
      {cvs.length === 0 ? (
        <p className="p-6 text-gray-600">No CVs submitted yet.</p>
      ) : (
        <div>
          {cvs.map((cv) => (
            <CvListItem
              key={cv.id}
              cv={{
                ...cv,
                status: isSubmitting(cv.id) ? 'pending' : cv.status,
              }}
              onView={onView}
              onDelete={onDelete}
              onViewSuggestions={onViewSuggestions}
            />
          ))}
        </div>
      )}
    </div>
  );
}
