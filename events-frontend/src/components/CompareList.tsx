import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

interface CompareItem {
  id: number;
  cvName: string;
  jobSpecName: string;
}

export function CompareList() {
  const [compares, setCompares] = useState<CompareItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/compare')
      .then((res) => res.json())
      .then((data) => {
        setCompares(data);
        setLoading(false);
      })
      .catch((err) => {
        toast.error('Error loading compares');
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div className="text-center py-4">Loading...</div>;
  }

  if (compares.length === 0) {
    return (
      <div className="text-center py-4 text-gray-500">No compares found</div>
    );
  }

  return (
    <div className="bg-white p-6 rounded-lg shadow-md">
      <h1 className="text-2xl font-bold mb-4">Compare List</h1>
      <table className="w-full">
        <thead>
          <tr className="border-b">
            <th className="text-left py-2">ID</th>
            <th className="text-left py-2">CV Name</th>
            <th className="text-left py-2">Job Spec Name</th>
          </tr>
        </thead>
        <tbody>
          {compares.map((compare) => (
            <tr key={compare.id} className="border-b hover:bg-gray-50">
              <td className="py-3">{compare.id}</td>
              <td className="py-3">{compare.cvName}</td>
              <td className="py-3">{compare.jobSpecName}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
