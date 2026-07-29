import { render, screen } from '@testing-library/react';
import { expect, test } from 'vitest';
import App from './App.jsx';

test('renders app branding', () => {
  render(<App />);
  const brandElement = screen.getAllByText(/WhereIsIvan/i)[0];
  expect(brandElement).toBeInTheDocument();
});
