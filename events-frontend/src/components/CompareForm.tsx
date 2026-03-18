import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';

interface Cv {
  id: number;
  name: string;
}

interface CompareFormProps {
  setActiveTab: (
    tab: 'cv' | 'job-spec' | 'compare-form' | 'compare-list',
  ) => void;
}

export function CompareForm({ setActiveTab }: CompareFormProps) {
  const [cvs, setCvs] = useState<Cv[]>([]);
  const [jobSpecs, setJobSpecs] = useState<Array<{ id: number; name: string }>>(
    [],
  );
  const [selectedCvId, setSelectedCvId] = useState<number | ''>('');
  const [selectedJobSpecId, setSelectedJobSpecId] = useState<number | ''>('');

  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    fetch('/api/cv')
      .then((res) => res.json())
      .then((data) => setCvs(data))
      .catch((err) => console.error('Error fetching CVs:', err));

    fetch('/api/jobspec')
      .then((res) => res.json())
      .then((data) => setJobSpecs(data))
      .catch((err) => console.error('Error fetching job specs:', err));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!selectedCvId || !selectedJobSpecId) return;

    setIsSubmitting(true);

    try {
      await fetch('http://localhost:8080/api/compare', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          cvId: Number(selectedCvId),
          jobSpecId: Number(selectedJobSpecId),
        }),
      });

      toast.success('Comparison submitted successfully!');
      setSelectedCvId('');
      setSelectedJobSpecId('');
      setActiveTab('compare-list');
    } catch (error) {
      toast.error('Error submitting comparison');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow-md">
      <h1 className="text-2xl font-bold mb-4">Compare CV with Job Spec</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label
            htmlFor="cvId"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            Select CV:
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
                {cv.name}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label
            htmlFor="jobSpecId"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            Select Job Spec:
          </label>
          <select
            id="jobSpecId"
            value={selectedJobSpecId}
            onChange={(e) =>
              setSelectedJobSpecId(
                e.target.value === '' ? '' : Number(e.target.value),
              )
            }
            required
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Select a Job Spec</option>
            {jobSpecs.map((spec) => (
              <option key={spec.id} value={spec.id}>
                {spec.name}
              </option>
            ))}
          </select>
        </div>

        <button
          type="submit"
          disabled={isSubmitting || !selectedCvId || !selectedJobSpecId}
          className={`w-full px-4 py-2 text-white rounded-md ${
            isSubmitting || !selectedCvId || !selectedJobSpecId
              ? 'bg-gray-400 cursor-not-allowed'
              : 'bg-blue-600 hover:bg-blue-700'
          }`}
        >
          {isSubmitting ? 'Comparing...' : 'Compare'}
        </button>
      </form>
    </div>
  );
}
