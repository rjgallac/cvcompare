import React from 'react';
import { CvManager } from '../components/CvManager';
import { JobSpecManager } from '../components/JobSpecManager';

export function App() {
  return (
    <div style={{ padding: '20px' }}>
      <CvManager />
      <JobSpecManager />
    </div>
  );
}

export default App;
