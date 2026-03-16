import React, { useState } from 'react';
import { toast } from 'react-toastify';
import { JobSpecListItem } from './JobSpecListItem';
import { JobSpec } from '../types/JobSpec';

interface JobSpecFormProps {
  onSubmitJobSpec: (cvName: string, content: string) => Promise<void>;
  onNavigateToList: () => void;
}

export function JobSpecForm({
  onSubmitJobSpec,
  onNavigateToList,
}: JobSpecFormProps) {
  const [jobSpecContent, setJobSpecContent] = useState('');
  const [cvName, setCvName] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!cvName.trim() || !jobSpecContent.trim()) return;

    setIsSubmitting(true);

    try {
      await onSubmitJobSpec(cvName, jobSpecContent);
      toast.success('Job spec submitted successfully!');
      setCvName('');
      setJobSpecContent('');
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
            htmlFor="cvName"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            CV Name:
          </label>
          <input
            type="text"
            id="cvName"
            value={cvName}
            onChange={(e) => setCvName(e.target.value)}
            placeholder="Enter CV name"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
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
