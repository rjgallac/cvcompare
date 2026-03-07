import React, { useState, useEffect } from 'react';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

// Uncomment this line to use CSS modules
// import styles from './app.module.css';

interface ProductFormData {
  id: string;
  name: string;
  price: number | string;
}

interface Product {
  id: string;
  name: string;
  price: number;
  status: string;
}

export function App() {
  const [formData, setFormData] = useState<ProductFormData>({
    id: '',
    name: '',
    price: '',
  });
  const [cvContent, setCvContent] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [message, setMessage] = useState('');
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [wsConnected, setWsConnected] = useState(false);
  const [pendingProductIds, setPendingProductIds] = useState<Set<string>>(
    new Set(),
  );

  useEffect(() => {
    fetchProducts();
  }, []);

  useEffect(() => {
    let stompClient: any = null;

    const connectWebSocket = () => {
      try {
        const socket = new SockJS('http://localhost:8080/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect(
          {},
          () => {
            setWsConnected(true);
            console.log('WebSocket connected');

            stompClient.subscribe('/topic/status', (message: any) => {
              const statusMessage = JSON.parse(message.body);
              console.log('Received WebSocket message:', statusMessage);

              setPendingProductIds((prev) => {
                const updated = new Set(prev);
                updated.delete(statusMessage.id);
                return updated;
              });

              setProducts((prevProducts) => {
                const existingIndex = prevProducts.findIndex(
                  (p) => p.id === statusMessage.id,
                );
                if (existingIndex >= 0) {
                  const updated = [...prevProducts];
                  updated[existingIndex] = {
                    ...updated[existingIndex],
                    status: statusMessage.status,
                  };
                  return updated;
                } else {
                  return [
                    ...prevProducts,
                    {
                      id: statusMessage.id,
                      name: '',
                      price: 0,
                      status: statusMessage.status,
                    },
                  ];
                }
              });
            });
          },
          (error: any) => {
            console.error('WebSocket connection failed:', error);
            setWsConnected(false);
          },
        );
      } catch (err) {
        console.error('Error creating WebSocket:', err);
        setWsConnected(false);
      }
    };

    connectWebSocket();

    return () => {
      if (stompClient && stompClient.disconnect) {
        try {
          stompClient.disconnect(() => {});
        } catch (err) {
          console.error('Error disconnecting WebSocket:', err);
        }
      }
    };
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await fetch('/api/test/getProducts');
      if (response.ok) {
        const data = await response.json();
        setProducts(data);
      }
    } catch (error) {
      console.error('Error fetching products:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setMessage('');

    if (cvContent) {
      try {
        const response = await fetch('/api/cv', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ curriculum_vitae_content: cvContent }),
        });

        if (response.ok) {
          setMessage('CV submitted successfully!');
          setCvContent('');
        } else {
          setMessage('Failed to submit CV');
        }
      } catch (error) {
        setMessage('Error connecting to backend');
      } finally {
        setIsSubmitting(false);
      }
      return;
    }

    const newProduct: Product = {
      id: formData.id,
      name: formData.name,
      price: Number(formData.price),
      status: 'Pending',
    };

    setProducts((prev) => [...prev, newProduct]);
    setPendingProductIds((prev) => new Set(prev).add(formData.id));
    setFormData({ id: '', name: '', price: '' });

    try {
      const response = await fetch('/api/test/addProduct', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        setMessage('Product added successfully!');
      } else {
        setMessage('Failed to add product');
        setProducts((prev) => prev.filter((p) => p.id !== formData.id));
        setPendingProductIds((prev) => {
          const updated = new Set(prev);
          updated.delete(formData.id);
          return updated;
        });
      }
    } catch (error) {
      setMessage('Error connecting to backend');
      setProducts((prev) => prev.filter((p) => p.id !== formData.id));
      setPendingProductIds((prev) => {
        const updated = new Set(prev);
        updated.delete(formData.id);
        return updated;
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCvSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!cvContent.trim()) return;

    setIsSubmitting(true);
    setMessage('');

    try {
      const response = await fetch('/api/cv', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ curriculum_vitae_content: cvContent }),
      });

      if (response.ok) {
        setMessage('CV submitted successfully!');
        setCvContent('');
      } else {
        setMessage('Failed to submit CV');
      }
    } catch (error) {
      setMessage('Error connecting to backend');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCvChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setCvContent(e.target.value);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div>
      <div style={{ marginBottom: '20px' }}>
        <span
          style={{
            color: wsConnected ? 'green' : 'red',
            fontWeight: 'bold',
          }}
        >
          WebSocket: {wsConnected ? 'Connected' : 'Disconnected'}
        </span>
      </div>
      <h2>Product List</h2>
      {loading ? (
        <p>Loading products...</p>
      ) : (
        <table
          style={{
            width: '100%',
            borderCollapse: 'collapse',
            marginBottom: '20px',
          }}
        >
          <thead>
            <tr style={{ backgroundColor: '#f5f5f5' }}>
              <th
                style={{
                  border: '1px solid #ddd',
                  padding: '8px',
                  textAlign: 'left',
                }}
              >
                ID
              </th>
              <th
                style={{
                  border: '1px solid #ddd',
                  padding: '8px',
                  textAlign: 'left',
                }}
              >
                Name
              </th>
              <th
                style={{
                  border: '1px solid #ddd',
                  padding: '8px',
                  textAlign: 'left',
                }}
              >
                Price
              </th>
              <th
                style={{
                  border: '1px solid #ddd',
                  padding: '8px',
                  textAlign: 'left',
                }}
              >
                Status
              </th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr
                key={product.id}
                style={{
                  backgroundColor: pendingProductIds.has(product.id)
                    ? '#fff3cd'
                    : 'transparent',
                }}
              >
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                  {product.id}
                </td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                  {product.name}
                </td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                  ${Number(product.price).toFixed(2)}
                </td>
                <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                  {pendingProductIds.has(product.id) ? (
                    <span style={{ color: 'blue' }}>Processing...</span>
                  ) : (
                    product.status
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <h1>Add Product</h1>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '10px' }}>
          <label htmlFor="id">ID:</label>
          <input
            type="text"
            id="id"
            name="id"
            value={formData.id}
            onChange={handleChange}
            required
            style={{ marginLeft: '10px', padding: '5px' }}
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label htmlFor="name">Name:</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
            style={{ marginLeft: '10px', padding: '5px' }}
          />
        </div>
        <div style={{ marginBottom: '10px' }}>
          <label htmlFor="price">Price:</label>
          <input
            type="number"
            id="price"
            name="price"
            value={formData.price}
            onChange={handleChange}
            required
            step="0.01"
            style={{ marginLeft: '10px', padding: '5px' }}
          />
        </div>
        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Adding...' : 'Add Product'}
        </button>
      </form>

      <h1>Add CV</h1>
      <form onSubmit={handleCvSubmit}>
        <div style={{ marginBottom: '10px' }}>
          <label htmlFor="curriculum_vitae_content">
            Curriculum Vitae Content:
          </label>
          <textarea
            id="curriculum_vitae_content"
            name="curriculum_vitae_content"
            value={cvContent}
            onChange={handleCvChange}
            required
            rows={5}
            style={{ marginLeft: '10px', padding: '5px', width: '300px' }}
          />
        </div>
        <button type="submit" disabled={isSubmitting || !cvContent.trim()}>
          {isSubmitting ? 'Submitting...' : 'Submit CV'}
        </button>
      </form>

      {message && (
        <p style={{ color: message.includes('success') ? 'green' : 'red' }}>
          {message}
        </p>
      )}
    </div>
  );
}

export default App;
