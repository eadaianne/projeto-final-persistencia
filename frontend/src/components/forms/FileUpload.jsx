import React from 'react';

const FileUpload = ({ label, onChange, name, accept }) => {
	return (
		<label style={{ display: 'block', marginBottom: '.75rem' }}>
			{label && <div style={{ marginBottom: '.25rem' }}>{label}</div>}
			<input type="file" name={name} accept={accept} onChange={(e) => onChange?.(e.target.files?.[0])} />
		</label>
	);
};

export default FileUpload;

