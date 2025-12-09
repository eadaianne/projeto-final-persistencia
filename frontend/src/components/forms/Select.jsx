import React from 'react';

const Select = ({ label, value, onChange, name, options = [] }) => (
	<label style={{ display: 'block', marginBottom: '.75rem' }}>
		{label && <div style={{ marginBottom: '.25rem' }}>{label}</div>}
		<select name={name} value={value} onChange={(e) => onChange?.(e.target.value)} style={{ padding: '.5rem', borderRadius: '6px' }}>
			<option value="">— selecione —</option>
			{options.map((opt) => (
				<option key={opt.value ?? opt} value={opt.value ?? opt}>{opt.label ?? opt}</option>
			))}
		</select>
	</label>
);

export default Select;

