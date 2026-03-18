import React, { useState, useEffect, useRef } from 'react';
import { Client, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { CvForm } from './CvForm';
import { CvList } from './CvList';
import { CvModal } from './CvModal';
import { SuggestionModal } from './SuggestionModal';
import { Cv } from '../types/Cv';
import { toast } from 'react-toastify';

export function CvManager() {
  const [view, setView] = useState<'form' | 'list'>('form');
  const [cvs, setCvs] = useState<Cv[]>([]);
  const [selectedCv, setSelectedCv] = useState<Cv | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [showSuggestionModal, setShowSuggestionModal] = useState(false);
  const [submittingCvIds, setSubmittingCvIds] = useState<number[]>([]);
  const stompClientRef = useRef<Client | null>(null);

  useEffect(() => {
    fetchCvs();
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
      stompClient.subscribe('/topic/cv', (message: any) => {
        try {
          const statusMessage: any = JSON.parse(message.body);
          updateCVInList(statusMessage);
        } catch (error) {
          console.error('Error parsing WebSocket message:', error);
        }
      });
    };

    stompClient.onStompError = (frame: any) => {
      console.error('WebSocket connection error:', frame);
    };

    stompClient.activate();
    stompClientRef.current = stompClient;
  };

  const updateCVInList = (statusMessage: any) => {
    toast.success(`CV ${statusMessage.cvId || statusMessage.id} updated`, {
      autoClose: 3000,
    });

    setCvs((prevCvs) =>
      prevCvs.map((cv) => {
        if (cv.id === statusMessage.cvId || cv.id === statusMessage.id) {
          return {
            ...cv,
            curriculum_vitae_content_suggestions: statusMessage.suggestions,
            status: 'completed',
          };
        }
        return cv;
      }),
    );

    setSubmittingCvIds((prev) =>
      prev.filter((id) => id !== statusMessage.cvId),
    );
  };

  const fetchCvs = async () => {
    try {
      const response = await fetch('/api/cv');
      if (response.ok) {
        const data: Cv[] = await response.json();
        setCvs(data);
      }
    } catch (error) {
      console.error('Error fetching CVs:', error);
    }
  };

  const handleSubmitCv = async (name: string, content: string) => {
    try {
      const response = await fetch('/api/cv', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, curriculum_vitae_content: content }),
      });

      if (response.ok) {
        let parsedSuccessfully = false;
        try {
          const data = await response.json();
          if (data && data.id) {
            setSubmittingCvIds((prev) => [...prev, data.id]);
            parsedSuccessfully = true;
          } else {
            console.log('Response received but no ID:', data);
          }
        } catch (parseError) {
          console.log('Response not JSON, refreshing list');
          const tempId = Date.now();
          setSubmittingCvIds((prev) => [...prev, tempId]);

          setTimeout(() => {
            fetchCvs();
          }, 500);
        }

        if (parsedSuccessfully) {
          fetchCvs();
        }
      } else {
        toast.error('Failed to submit CV');
      }
    } catch (error) {
      console.error('Submission error:', error);
      toast.error('Error connecting to backend');
    }
  };

  const handleDeleteCv = async (id: number) => {
    try {
      const response = await fetch(`/api/cv/${id}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        fetchCvs();
      } else {
        throw new Error('Failed to delete CV');
      }
    } catch (error) {
      console.error('Delete error:', error);
      throw error;
    }
  };

  const handleViewClick = (cv: Cv) => {
    setSelectedCv(cv);
    setShowModal(true);
  };

  const handleSuggestCv = async (id: number) => {
    try {
      const response = await fetch(`/api/cv/${id}/suggest`);
      if (!response.ok) {
        throw new Error('Failed to get suggestion');
      }
      const data: Cv = await response.json();
      // setSelectedCv(data);
      // setShowSuggestionModal(true);
    } catch (error) {
      console.error('Suggest error:', error);
      throw error;
    }
  };

  const handleSuggestClick = (cv: Cv) => {
    setSelectedCv(cv);
    setShowSuggestionModal(true);
  };

  const handleViewSuggestionsClick = async (cv: Cv) => {
    try {
      const response = await fetch(`/api/cv/${cv.id}`);
      if (!response.ok) {
        throw new Error('Failed to get CV details');
      }
      const data: Cv = await response.json();
      setSelectedCv(data);
      setShowSuggestionModal(true);
    } catch (error) {
      console.error('View suggestions error:', error);
      throw error;
    }
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
          Add CV
        </button>
        <button
          onClick={() => {
            setView('list');
            fetchCvs();
          }}
          className={`px-4 py-2 font-medium ${
            view === 'list'
              ? 'border-b-2 border-blue-600 text-blue-600'
              : 'text-gray-600 hover:text-gray-900'
          }`}
        >
          CV List
        </button>
      </nav>

      {view === 'form' ? (
        <CvForm
          onSubmitCv={handleSubmitCv}
          onNavigateToList={() => setView('list')}
        />
      ) : (
        <CvList
          cvs={cvs}
          submittingIds={submittingCvIds}
          onView={handleViewClick}
          onDelete={handleDeleteCv}
          onSuggest={handleSuggestCv}
          onViewSuggestions={handleViewSuggestionsClick}
        />
      )}

      {showModal && selectedCv && (
        <CvModal cv={selectedCv} onClose={() => setShowModal(false)} />
      )}

      {showSuggestionModal && selectedCv && (
        <SuggestionModal
          cv={selectedCv}
          onClose={() => setShowSuggestionModal(false)}
        />
      )}
    </div>
  );
}
