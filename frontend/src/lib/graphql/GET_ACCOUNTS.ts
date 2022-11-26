import { gql } from '@apollo/client/core';

export const GET_ACCOUNTS = gql`
	query accounts {
		accounts {
			id
			name
			email
			balance
			picture
			extId
		}
	}
`;
