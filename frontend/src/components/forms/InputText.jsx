import React from 'react';

const InputText = ({ label, value, onChange, name, placeholder, type = 'text' }) => {
	return (
		<label style={{ display: 'block', marginBottom: '.75rem' }}>
			{label && <div style={{ marginBottom: '.25rem' }}>{label}</div>}
			<input
				name={name}
				type={type}
				value={value}
				onChange={(e) => onChange?.(e.target.value)}
				placeholder={placeholder}
				style={{ display: 'block', width: '100%', padding: '.5rem', borderRadius: '6px', border: '1px solid #ddd' }}
			/>
		</label>
	);
};

export default InputText;

