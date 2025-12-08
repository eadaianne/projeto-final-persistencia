import React from 'react';

const Loader = ({ size = 24 }) => (
	<div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', padding: 12 }}>
		<svg width={size} height={size} viewBox="0 0 50 50">
			<circle cx="25" cy="25" r="20" fill="none" stroke="var(--color-primary)" strokeWidth="4" strokeDasharray="31.4 31.4" transform="rotate(-90 25 25)">
				<animateTransform attributeName="transform" type="rotate" from="0 25 25" to="360 25 25" dur="1s" repeatCount="indefinite" />
			</circle>
		</svg>
	</div>
);

export default Loader;

