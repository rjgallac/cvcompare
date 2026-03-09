interface MessageProps {
  message: string;
  marginTop?: string;
}

export function MessageDisplay({ message, marginTop = 'mt-5' }: MessageProps) {
  if (!message) return null;

  const isSuccess = message.toLowerCase().includes('success');

  return (
    <p
      className={`${marginTop} ${isSuccess ? 'text-green-600 font-medium' : 'text-red-600 font-medium'} px-4 py-2 rounded bg-opacity-80 ${isSuccess ? 'bg-green-100' : 'bg-red-100'}`}
    >
      {message}
    </p>
  );
}
