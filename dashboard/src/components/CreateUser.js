import React, { useState } from 'react';
import './CreateUser.css'; // Import your CSS file for styling
import './Form.css'
const CreateUser = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        confirmPassword: '',
    });

    const [errors, setErrors] = useState({});

    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    };

    const validatePassword = (password) => {
        // Password must be at least 8 characters, include an uppercase letter, a lowercase letter, a number, and a special character
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        return passwordRegex.test(password);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const newErrors = {};

        if (!validateEmail(formData.email)) {
            newErrors.email = 'Invalid email address';
        }

        if (!validatePassword(formData.password)) {
            newErrors.password =
                'Password must be at least 8 characters, include an uppercase letter, a lowercase letter, a number, and a special character';
        }

        if (formData.password !== formData.confirmPassword) {
            newErrors.confirmPassword = 'Passwords do not match';
        }

        setErrors(newErrors);

        if (Object.keys(newErrors).length === 0) {
            console.log('Form submitted successfully:', formData);
            // Add your form submission logic here
        }
    };

    return (
        <div className="form-container">
            <h2 className="form-title">Sign Up</h2>
            <form onSubmit={handleSubmit} className="form">
                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        className="form-input"
                    />
                    {errors.email && <p className="error-text">{errors.email}</p>}
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        className="form-input"
                    />
                    {errors.password && <p className="error-text">{errors.password}</p>}
                </div>
                <div className="form-group">
                    <label htmlFor="confirmPassword">Confirm Password:</label>
                    <input
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        className="form-input"
                    />
                    {errors.confirmPassword && (
                        <p className="error-text">{errors.confirmPassword}</p>
                    )}
                </div>
                <button type="submit" className="form-button">Register</button>
            </form>
        </div>
    );
};

export default CreateUser;