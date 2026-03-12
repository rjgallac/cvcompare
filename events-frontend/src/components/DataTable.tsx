import React from 'react';

interface Cv {
  id: number;
  name: string;
  curriculum_vitae_content: string;
}

interface DataTableProps {
  data: Cv[] | any[];
  columns: Array<{ key: keyof Cv | keyof any; header: string }>;
  onDelete?: (id: number) => void;
  onActionClick?: (item: any) => void;
  actionLabel?: string;
  emptyMessage: string;
}

export function DataTable({
  data,
  columns,
  onDelete,
  onActionClick,
  actionLabel = 'Delete',
  emptyMessage,
}: DataTableProps) {
  if (data.length === 0) {
    return <p>{emptyMessage}</p>;
  }

  return (
    <div className="mt-5">
      {data.length === 0 ? (
        <p className="text-gray-600">{emptyMessage}</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full border-collapse shadow-sm rounded-lg overflow-hidden bg-white">
            <thead className="bg-gray-100">
              <tr>
                {columns.map((col) => (
                  <th
                    key={String(col.key)}
                    className={`border px-4 py-3 text-left font-semibold ${
                      col.key === 'id' || col.header === 'Actions'
                        ? 'text-center'
                        : ''
                    }`}
                  >
                    {col.header}
                  </th>
                ))}
                <th className="border px-4 py-3 text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {data.map((item) => (
                <tr key={item.id} className="hover:bg-gray-50">
                  {columns.map((col) => (
                    <td
                      key={String(col.key)}
                      className={`border px-4 py-3 ${
                        col.key === 'id' ? 'text-center' : 'text-left'
                      }`}
                    >
                      {item[col.key]}
                    </td>
                  ))}
                  <td className="border px-4 py-3 text-center">
                    {onDelete && (
                      <button
                        onClick={() => onDelete(item.id)}
                        className="bg-red-600 text-white px-3 py-1.5 rounded hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 mr-2"
                      >
                        Delete
                      </button>
                    )}
                    {onActionClick && (
                      <button
                        onClick={() => onActionClick(item)}
                        className="bg-blue-600 text-white px-3 py-1.5 rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                      >
                        {actionLabel}
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
