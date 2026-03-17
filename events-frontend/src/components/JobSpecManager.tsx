import React, { useState, useEffect, useRef } from 'react';
import { Client, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { JobSpecForm } from './JobSpecForm';
import { JobSpecList } from './JobSpecList';
import { JobSpecModal } from './JobSpecModal';
import { JobSpec } from '../types/JobSpec';
import { toast } from 'react-toastify';

export function JobSpecManager() {
  const [view, setView] = useState<'form' | 'list'>('form');
  const [jobSpecs, setJobSpecs] = useState<JobSpec[]>([]);
  const [cvs, setCvs] = useState<Array<{ id: number; name: string }>>([]);
  const [selectedJobSpec, setSelectedJobSpec] = useState<JobSpec | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [submittingJobSpecIds, setSubmittingJobSpecIds] = useState<number[]>(
    [],
  );
  const stompClientRef = useRef<Client | null>(null);

  useEffect(() => {
    fetchCvs();
    fetchJobSpecs();
    connectWebSocket();
    return () => {
      if (stompClientRef.current) {
        stompClientRef.current.deactivate();
      }
    };
  }, []);

  const connectWebSocket = () => {
    const stompClient: Client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
    });

    stompClient.onConnect = (frame: any) => {
      console.log('Connected to WebSocket:', frame);
      const subscription: StompSubscription = stompClient.subscribe(
        '/topic/jobspec',
        (message: any) => {
          try {
            const statusMessage: any = JSON.parse(message.body);
            updateJobSpecInList(statusMessage);
          } catch (error) {
            console.error('Error parsing WebSocket message:', error);
          }
        },
      );
    };

    stompClient.onStompError = (frame: any) => {
      console.error('WebSocket connection error:', frame);
    };

    stompClient.activate();
    stompClientRef.current = stompClient;
  };

  const updateJobSpecInList = (statusMessage: any) => {
    toast.success(`Job Spec ${statusMessage.jobSpecId} updated`, {
      autoClose: 3000,
    });

    setJobSpecs((prevJobSpecs) =>
      prevJobSpecs.map((jobSpec) => {
        if (jobSpec.id === statusMessage.jobSpecId) {
          return {
            ...jobSpec,
            score: statusMessage.score,
            location: statusMessage.location,
            jobTitle: statusMessage.title,
            company: statusMessage.company,
            salary: statusMessage.salary
              ? parseInt(statusMessage.salary)
              : null,
            status: 'completed',
          };
        }
        return jobSpec;
      }),
    );

    setSubmittingJobSpecIds((prev) =>
      prev.filter((id) => id !== statusMessage.jobSpecId),
    );
  };

  const fetchCvs = async () => {
    try {
      const response = await fetch('/api/cv');
      if (response.ok) {
        const data = await response.json();
        setCvs(data);
      }
    } catch (error) {
      console.error('Error fetching CVs:', error);
    }
  };

  const fetchJobSpecs = async () => {
    try {
      const response = await fetch('/api/jobspec');
      if (response.ok) {
        const data: JobSpec[] = await response.json();
        setJobSpecs(data);

        const existingIds = data.map((js: JobSpec) => js.id);
        setSubmittingJobSpecIds((prev) =>
          prev.filter((id) => existingIds.includes(id)),
        );
      }
    } catch (error) {
      console.error('Error fetching job specs:', error);
    }
  };

  const handleSubmitJobSpec = async (name: string, content: string) => {
    try {
      const response = await fetch('/api/jobspec', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, job_spec_content: content }),
      });

      if (response.ok) {
        let parsedSuccessfully = false;
        try {
          const data = await response.json();
          if (data && data.id) {
            setSubmittingJobSpecIds((prev) => [...prev, data.id]);
            parsedSuccessfully = true;
          } else {
            console.log('Response received but no ID:', data);
          }
        } catch (parseError) {
          console.log('Response not JSON, refreshing list');
          const tempId = Date.now();
          setSubmittingJobSpecIds((prev) => [...prev, tempId]);

          setTimeout(() => {
            fetchJobSpecs();
          }, 500);
        }

        if (parsedSuccessfully) {
          fetchJobSpecs();
        }
      } else {
        toast.error('Failed to submit job spec');
      }
    } catch (error) {
      console.error('Submission error:', error);
      toast.error('Error connecting to backend');
    }
  };

  const handleDeleteJobSpec = async (id: number) => {
    try {
      const response = await fetch(`/api/jobspec/${id}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        toast.success('Job spec deleted successfully!');
        fetchJobSpecs();
      } else {
        toast.error('Failed to delete job spec');
      }
    } catch (error) {
      toast.error('Error deleting job spec');
    }
  };

  const handleViewClick = (jobSpec: JobSpec) => {
    setSelectedJobSpec(jobSpec);
    setShowModal(true);
  };

  return (
    <div className="space-y-6">
      <nav className="flex space-x-4 border-b">
        <button
          onClick={() => setView('form')}
          className={`px-4 py-2 font-medium ${
            view === 'form'
              ? 'border-b-2 border-blue-600 text-blue-600'
              : 'text-gray-600 hover:text-gray-900'
          }`}
        >
          Add Job Spec
        </button>
        <button
          onClick={() => {
            setView('list');
            fetchJobSpecs();
          }}
          className={`px-4 py-2 font-medium ${
            view === 'list'
              ? 'border-b-2 border-blue-600 text-blue-600'
              : 'text-gray-600 hover:text-gray-900'
          }`}
        >
          Job Spec List
        </button>
      </nav>

      {view === 'form' ? (
        <JobSpecForm
          onSubmitJobSpec={handleSubmitJobSpec}
          onNavigateToList={() => setView('list')}
        />
      ) : (
        <JobSpecList
          jobSpecs={jobSpecs}
          submittingIds={submittingJobSpecIds}
          onView={handleViewClick}
          onDelete={handleDeleteJobSpec}
        />
      )}

      {showModal && selectedJobSpec && (
        <JobSpecModal
          jobSpec={selectedJobSpec}
          onClose={() => setShowModal(false)}
        />
      )}
    </div>
  );
}
