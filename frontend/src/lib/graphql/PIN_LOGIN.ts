import { gql } from '@apollo/client/core';

export const PIN_LOGIN = gql`
	query pinLogin($login: PinLoginInput) {
		pinLogin(login: $login)
	}
`;
