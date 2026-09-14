import { render, screen } from '@testing-library/react';
import { beforeEach, expect, test, vi } from 'vitest';
import App from './App.jsx';
import Dashboard from './components/Dashboard.jsx';

beforeEach(() => {
  vi.restoreAllMocks();
});

test('renders app branding', () => {
  render(<App />);
  const brandElement = screen.getAllByText(/WhereIsIvan/i)[0];
  expect(brandElement).toBeInTheDocument();
});

test('shows no current activity when the dashboard endpoint returns no content', async () => {
  vi.stubGlobal('fetch', vi.fn(() => Promise.resolve({
    ok: true,
    status: 204,
    json: async () => {
      throw new Error('No JSON body');
    }
  })));

  render(<Dashboard />);

  expect(await screen.findByText(/No current activity/i)).toBeInTheDocument();
});
