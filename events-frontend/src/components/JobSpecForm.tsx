import React, { useState } from 'react';
import { toast } from 'react-toastify';
import { JobSpecListItem } from './JobSpecListItem';
import { JobSpec } from '../types/JobSpec';

interface JobSpecFormProps {
  cvs: Array<{ id: number; name: string }>;
  onSubmitJobSpec: (cvId: number | null, content: string) => Promise<void>;
  onNavigateToList: () => void;
}

export function JobSpecForm({
  cvs,
  onSubmitJobSpec,
  onNavigateToList,
}: JobSpecFormProps) {
  const [jobSpecContent, setJobSpecContent] = useState('');
  const [selectedCvId, setSelectedCvId] = useState<number | ''>('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!jobSpecContent.trim()) return;

    setIsSubmitting(true);

    try {
      await onSubmitJobSpec(
        selectedCvId === '' ? null : Number(selectedCvId),
        jobSpecContent,
      );
      toast.success('Job spec submitted successfully!');
      setJobSpecContent('');
      setSelectedCvId('');
      onNavigateToList();
    } catch (error) {
      toast.error('Error submitting job spec');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow-md">
      <h1 className="text-2xl font-bold mb-4">Add Job Spec</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label
            htmlFor="cvId"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            CV:
          </label>
          <select
            id="cvId"
            value={selectedCvId}
            onChange={(e) =>
              setSelectedCvId(
                e.target.value === '' ? '' : Number(e.target.value),
              )
            }
            required
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Select a CV</option>
            {cvs.map((cv) => (
              <option key={cv.id} value={cv.id}>
                {cv.name} ({cv.id})
              </option>
            ))}
          </select>
        </div>
        <div>
          <label
            htmlFor="jobSpecContent"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            Job Spec Content:
          </label>
          <textarea
            id="jobSpecContent"
            value={jobSpecContent}
            onChange={(e) => setJobSpecContent(e.target.value)}
            required
            rows={5}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button
          type="submit"
          disabled={isSubmitting || !jobSpecContent.trim()}
          className={`w-full px-4 py-2 text-white rounded-md ${
            isSubmitting || !jobSpecContent.trim()
              ? 'bg-gray-400 cursor-not-allowed'
              : 'bg-blue-600 hover:bg-blue-700'
          }`}
        >
          {isSubmitting ? 'Submitting...' : 'Submit Job Spec'}
        </button>
      </form>
    </div>
  );
}
