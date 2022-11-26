import { gql } from '@apollo/client/core';

export const ACCOUNT_BY_ID = gql`
	query accountById($id: BigInteger) {
		account(id: $id) {
			id
			name
			email
			balance
			picture
			extId
		}
	}
`;
