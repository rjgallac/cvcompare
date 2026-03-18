import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';

interface CompareItem {
  id: number;
  cvName: string;
  jobSpecName: string;
  status?: string;
  compareContent?: string;
}

const Modal = ({
  content,
  onClose,
}: {
  content: string;
  onClose: () => void;
}) => (
  <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div className="bg-white p-6 rounded-lg max-w-3xl w-[90%] max-h-[80vh] overflow-auto flex flex-col">
      <h2 className="text-xl font-semibold mb-4">Compare Content</h2>
      <pre className="bg-gray-100 p-3 rounded mt-1 whitespace-pre-wrap break-word flex-grow overflow-auto max-h-[60vh] text-sm">
        {content}
      </pre>
      <button
        onClick={onClose}
        className="mt-4 px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
      >
        Close
      </button>
    </div>
  </div>
);

export function CompareList() {
  const [compares, setCompares] = useState<CompareItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [viewingContent, setViewingContent] = useState<string | null>(null);
  let ws: WebSocket | null = null;

  useEffect(() => {
    fetch('http://localhost:8080/api/compare')
      .then((res) => res.json())
      .then((data) => {
        setCompares(
          data.map((c: CompareItem) => ({
            ...c,
            status: c.status || 'pending',
          })),
        );
        setLoading(false);
      })
      .catch((err) => {
        toast.error('Error loading compares');
        setLoading(false);
      });

    ws = new WebSocket('ws://localhost:8080/api/compare/ws');

    ws.onmessage = (event) => {
      try {
        const message = JSON.parse(event.data);
        if (message.type === 'status') {
          setCompares((prev) =>
            prev.map((c) =>
              c.id === message.id ? { ...c, status: message.status } : c,
            ),
          );
        } else if (message.type === 'complete' && message.content) {
          setCompares((prev) =>
            prev.map((c) =>
              c.id === message.id
                ? { ...c, status: 'completed', compareContent: message.content }
                : c,
            ),
          );
        } else if (message.type === 'complete' && !message.content) {
          setCompares((prev) =>
            prev.map((c) =>
              c.id === message.id ? { ...c, status: 'completed' } : c,
            ),
          );
        }
      } catch (err) {
        console.error('Error parsing websocket message:', err);
      }
    };

    ws.onclose = () => {
      console.log('WebSocket disconnected');
    };

    ws.onerror = (err) => {
      console.error('WebSocket error:', err);
    };

    return () => {
      if (ws) {
        ws.close();
      }
    };
  }, []);

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this item?')) return;

    try {
      await fetch(`http://localhost:8080/api/compare/${id}`, {
        method: 'DELETE',
      });
      setCompares(compares.filter((c) => c.id !== id));
      toast.success('Item deleted successfully');
    } catch (err) {
      toast.error('Error deleting item');
    }
  };

  const renderStatus = (item: CompareItem) => {
    if (item.status === 'pending') {
      return (
        <div className="flex items-center space-x-2 text-blue-600">
          <svg
            className="animate-spin h-4 w-4"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              className="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              strokeWidth="4"
            ></circle>
            <path
              className="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
            ></path>
          </svg>
          <span>Pending</span>
        </div>
      );
    }
    return item.status ?? '-';
  };

  if (loading) {
    return <div className="text-center py-4">Loading...</div>;
  }

  if (compares.length === 0) {
    return (
      <div className="text-center py-4 text-gray-500">No compares found</div>
    );
  }

  return (
    <>
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h1 className="text-2xl font-bold mb-4">Compare List</h1>
        <table className="w-full">
          <thead>
            <tr className="border-b">
              <th className="text-left py-2">ID</th>
              <th className="text-left py-2">CV Name</th>
              <th className="text-left py-2">Job Spec Name</th>
              <th className="text-left py-2">Status</th>
              <th className="text-left py-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            {compares.map((compare) => (
              <tr key={compare.id} className="border-b hover:bg-gray-50">
                <td className="py-3">{compare.id}</td>
                <td className="py-3">{compare.cvName}</td>
                <td className="py-3">{compare.jobSpecName}</td>
                <td className="py-3">{renderStatus(compare)}</td>
                <td className="py-3">
                  {compare.compareContent && (
                    <button
                      onClick={() =>
                        setViewingContent(compare.compareContent || null)
                      }
                      className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 mr-2"
                    >
                      View
                    </button>
                  )}
                  <button
                    onClick={() => handleDelete(compare.id)}
                    className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {viewingContent && (
        <Modal
          content={viewingContent}
          onClose={() => setViewingContent(null)}
        />
      )}
    </>
  );
}
