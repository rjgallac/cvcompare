import React from 'react';
import { CvManager } from '../components/CvManager';
import { JobSpecManager } from '../components/JobSpecManager';

export function App() {
  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4">
      <div className="max-w-3xl mx-auto space-y-8">
        <CvManager />
        <JobSpecManager />
      </div>
    </div>
  );
}

export default App;
