import gql from 'graphql-tag';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
export type Omit<T, K extends keyof T> = Pick<T, Exclude<keyof T, K>>;
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
  BigDecimal: { input: number; output: number; }
  BigInteger: { input: number; output: number; }
  DateTime: { input: string; output: string; }
};

export type Account = HasPicture & {
  __typename?: 'Account';
  balance?: Maybe<Scalars['BigInteger']['output']>;
  /** ISO-8601 */
  createdAt?: Maybe<Scalars['DateTime']['output']>;
  /** ISO-8601 */
  deletedAt?: Maybe<Scalars['DateTime']['output']>;
  email?: Maybe<Scalars['String']['output']>;
  extId?: Maybe<Scalars['String']['output']>;
  hasPin?: Maybe<Scalars['Boolean']['output']>;
  id?: Maybe<Scalars['BigInteger']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  picture?: Maybe<Scalars['String']['output']>;
};

export type AccountInput = {
  balance?: InputMaybe<Scalars['BigInteger']['input']>;
  /** ISO-8601 */
  createdAt?: InputMaybe<Scalars['DateTime']['input']>;
  /** ISO-8601 */
  deletedAt?: InputMaybe<Scalars['DateTime']['input']>;
  email?: InputMaybe<Scalars['String']['input']>;
  extId?: InputMaybe<Scalars['String']['input']>;
  id?: InputMaybe<Scalars['BigInteger']['input']>;
  name?: InputMaybe<Scalars['String']['input']>;
  picture?: InputMaybe<Scalars['String']['input']>;
};

export type ColumnPojoInput = {
  direction?: InputMaybe<DirectionPojo>;
  name?: InputMaybe<Scalars['String']['input']>;
};

export enum DirectionPojo {
  Ascending = 'Ascending',
  Descending = 'Descending'
}

export type HasPicture = {
  id?: Maybe<Scalars['BigInteger']['output']>;
  picture?: Maybe<Scalars['String']['output']>;
};

/** Mutation root */
export type Mutation = {
  __typename?: 'Mutation';
  /** Create Product */
  createProduct?: Maybe<Product>;
  /** Delete Account */
  deleteAccount?: Maybe<Scalars['Boolean']['output']>;
  /** Delete Product */
  deleteProduct?: Maybe<Scalars['Boolean']['output']>;
  /** Erase an Account permanently */
  eraseAccount?: Maybe<Scalars['Boolean']['output']>;
  /** Make Purchase */
  makePurchase?: Maybe<Array<Maybe<Purchase>>>;
  /** Set new PIN */
  pin?: Maybe<Scalars['Boolean']['output']>;
  /** Refund Purchase */
  refundPurchase?: Maybe<Scalars['Boolean']['output']>;
  /** Sign up with OIDC provider token and PIN */
  signUp?: Maybe<Account>;
  /** Toggle favorite */
  toggleFavorite?: Maybe<Scalars['Boolean']['output']>;
  /** Update Account */
  updateAccount?: Maybe<Account>;
  /** Update Product */
  updateProduct?: Maybe<Product>;
};


/** Mutation root */
export type MutationCreateProductArgs = {
  product?: InputMaybe<ProductInput>;
};


/** Mutation root */
export type MutationDeleteAccountArgs = {
  id?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Mutation root */
export type MutationDeleteProductArgs = {
  id?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Mutation root */
export type MutationEraseAccountArgs = {
  id?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Mutation root */
export type MutationMakePurchaseArgs = {
  amount?: InputMaybe<Scalars['Int']['input']>;
  productId?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Mutation root */
export type MutationPinArgs = {
  pin?: InputMaybe<Scalars['String']['input']>;
};


/** Mutation root */
export type MutationRefundPurchaseArgs = {
  id?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Mutation root */
export type MutationSignUpArgs = {
  pin?: InputMaybe<Scalars['String']['input']>;
};


/** Mutation root */
export type MutationToggleFavoriteArgs = {
  productId?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Mutation root */
export type MutationUpdateAccountArgs = {
  account?: InputMaybe<AccountInput>;
};


/** Mutation root */
export type MutationUpdateProductArgs = {
  product?: InputMaybe<ProductInput>;
};

export type MyAccount = {
  __typename?: 'MyAccount';
  account?: Maybe<Account>;
  isAdmin?: Maybe<Scalars['Boolean']['output']>;
};

export type PagePojoInput = {
  index?: InputMaybe<Scalars['Int']['input']>;
  size?: InputMaybe<Scalars['Int']['input']>;
};

export type PaginationResponse_Account = {
  __typename?: 'PaginationResponse_Account';
  data?: Maybe<Array<Maybe<Account>>>;
  page: Scalars['Int']['output'];
  total: Scalars['BigInteger']['output'];
};

export type PaginationResponse_Product = {
  __typename?: 'PaginationResponse_Product';
  data?: Maybe<Array<Maybe<Product>>>;
  page: Scalars['Int']['output'];
  total: Scalars['BigInteger']['output'];
};

export type PaginationResponse_ProductWithFavorite = {
  __typename?: 'PaginationResponse_ProductWithFavorite';
  data?: Maybe<Array<Maybe<ProductWithFavorite>>>;
  page: Scalars['Int']['output'];
  total: Scalars['BigInteger']['output'];
};

export type PaginationResponse_Purchase = {
  __typename?: 'PaginationResponse_Purchase';
  data?: Maybe<Array<Maybe<Purchase>>>;
  page: Scalars['Int']['output'];
  total: Scalars['BigInteger']['output'];
};

export type PinLoginInput = {
  id?: InputMaybe<Scalars['BigInteger']['input']>;
  pin?: InputMaybe<Scalars['String']['input']>;
};

export type Product = {
  __typename?: 'Product';
  bundleSize?: Maybe<Scalars['Int']['output']>;
  /** ISO-8601 */
  createdAt?: Maybe<Scalars['DateTime']['output']>;
  /** ISO-8601 */
  deletedAt?: Maybe<Scalars['DateTime']['output']>;
  id?: Maybe<Scalars['BigInteger']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  picture?: Maybe<Scalars['String']['output']>;
  price?: Maybe<Scalars['Int']['output']>;
  type?: Maybe<ProductType>;
};

export type ProductInput = {
  bundleSize?: InputMaybe<Scalars['Int']['input']>;
  /** ISO-8601 */
  createdAt?: InputMaybe<Scalars['DateTime']['input']>;
  /** ISO-8601 */
  deletedAt?: InputMaybe<Scalars['DateTime']['input']>;
  id?: InputMaybe<Scalars['BigInteger']['input']>;
  name?: InputMaybe<Scalars['String']['input']>;
  picture?: InputMaybe<Scalars['String']['input']>;
  price?: InputMaybe<Scalars['Int']['input']>;
  type?: InputMaybe<ProductType>;
};

export enum ProductType {
  ColdDrink = 'COLD_DRINK',
  HotDrink = 'HOT_DRINK'
}

export type ProductWithFavorite = {
  __typename?: 'ProductWithFavorite';
  bundleSize?: Maybe<Scalars['Int']['output']>;
  /** ISO-8601 */
  createdAt?: Maybe<Scalars['DateTime']['output']>;
  /** ISO-8601 */
  deletedAt?: Maybe<Scalars['DateTime']['output']>;
  id?: Maybe<Scalars['BigInteger']['output']>;
  isFavorite?: Maybe<Scalars['Boolean']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  picture?: Maybe<Scalars['String']['output']>;
  price?: Maybe<Scalars['Int']['output']>;
  type?: Maybe<ProductType>;
};

export type Purchase = {
  __typename?: 'Purchase';
  account?: Maybe<Account>;
  /** ISO-8601 */
  createdAt?: Maybe<Scalars['DateTime']['output']>;
  /** ISO-8601 */
  deletedAt?: Maybe<Scalars['DateTime']['output']>;
  id?: Maybe<Scalars['BigInteger']['output']>;
  paidPrice?: Maybe<Scalars['Int']['output']>;
  product?: Maybe<Product>;
};

export type PurchaseCount = {
  __typename?: 'PurchaseCount';
  count: Scalars['BigInteger']['output'];
  product?: Maybe<Product>;
  recommendedPurchaseAmount: Scalars['BigInteger']['output'];
};

/** Query root */
export type Query = {
  __typename?: 'Query';
  /** Get Account by ID */
  account?: Maybe<Account>;
  /** Get all Accounts */
  accounts?: Maybe<PaginationResponse_Account>;
  /** Get deleted Accounts */
  deletedAccounts?: Maybe<PaginationResponse_Account>;
  /** Get my account */
  myAccount?: Maybe<MyAccount>;
  /** Get personal Purchases */
  myPurchases?: Maybe<PaginationResponse_Purchase>;
  /** Login with PIN */
  pinLogin?: Maybe<Scalars['String']['output']>;
  /** Get Product by ID */
  product?: Maybe<Product>;
  /** Get ProductWithFavorite by ID */
  productWithFavorite?: Maybe<ProductWithFavorite>;
  /** Get all Products */
  products?: Maybe<PaginationResponse_Product>;
  /** Get all Products with Favorites */
  productsWithFavorites?: Maybe<PaginationResponse_ProductWithFavorite>;
  /** Get Purchase by ID */
  purchase?: Maybe<Purchase>;
  /** Get purchase counts for all bought products of the last month */
  purchaseCountsLastMonth?: Maybe<Array<Maybe<PurchaseCount>>>;
  /** Get all Purchases */
  purchases?: Maybe<PaginationResponse_Purchase>;
};


/** Query root */
export type QueryAccountArgs = {
  id?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Query root */
export type QueryAccountsArgs = {
  page?: InputMaybe<PagePojoInput>;
  searchByName?: InputMaybe<Scalars['String']['input']>;
  sort?: InputMaybe<SortPojoInput>;
};


/** Query root */
export type QueryDeletedAccountsArgs = {
  page?: InputMaybe<PagePojoInput>;
  sort?: InputMaybe<SortPojoInput>;
};


/** Query root */
export type QueryMyPurchasesArgs = {
  page?: InputMaybe<PagePojoInput>;
  sort?: InputMaybe<SortPojoInput>;
};


/** Query root */
export type QueryPinLoginArgs = {
  login?: InputMaybe<PinLoginInput>;
};


/** Query root */
export type QueryProductArgs = {
  id?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Query root */
export type QueryProductWithFavoriteArgs = {
  id?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Query root */
export type QueryProductsArgs = {
  page?: InputMaybe<PagePojoInput>;
  sort?: InputMaybe<SortPojoInput>;
};


/** Query root */
export type QueryProductsWithFavoritesArgs = {
  page?: InputMaybe<PagePojoInput>;
  sort?: InputMaybe<SortPojoInput>;
  type?: InputMaybe<ProductType>;
};


/** Query root */
export type QueryPurchaseArgs = {
  id?: InputMaybe<Scalars['BigInteger']['input']>;
};


/** Query root */
export type QueryPurchasesArgs = {
  page?: InputMaybe<PagePojoInput>;
  sort?: InputMaybe<SortPojoInput>;
};

export type SimpleProduct = HasPicture & {
  __typename?: 'SimpleProduct';
  bundleSize?: Maybe<Scalars['Int']['output']>;
  /** ISO-8601 */
  createdAt?: Maybe<Scalars['DateTime']['output']>;
  /** ISO-8601 */
  deletedAt?: Maybe<Scalars['DateTime']['output']>;
  id?: Maybe<Scalars['BigInteger']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  picture?: Maybe<Scalars['String']['output']>;
  price?: Maybe<Scalars['Int']['output']>;
  type?: Maybe<ProductType>;
};

export type SortPojoInput = {
  columns?: InputMaybe<Array<InputMaybe<ColumnPojoInput>>>;
};

export type AccountByIdQueryVariables = Exact<{
  id?: InputMaybe<Scalars['BigInteger']['input']>;
}>;


export type AccountByIdQuery = { __typename?: 'Query', account?: { __typename?: 'Account', id?: number | null, name?: string | null, email?: string | null, balance?: number | null, picture?: string | null, extId?: string | null } | null };

export type AccountsQueryVariables = Exact<{ [key: string]: never; }>;


export type AccountsQuery = { __typename?: 'Query', accounts?: { __typename?: 'PaginationResponse_Account', page: number, total: number, data?: Array<{ __typename?: 'Account', id?: number | null, name?: string | null, email?: string | null, balance?: number | null, picture?: string | null, extId?: string | null } | null> | null } | null };

export type AdminAccountsQueryVariables = Exact<{
  sort: SortPojoInput;
  searchByName?: InputMaybe<Scalars['String']['input']>;
}>;


export type AdminAccountsQuery = { __typename?: 'Query', accounts?: { __typename?: 'PaginationResponse_Account', page: number, total: number, data?: Array<{ __typename?: 'Account', id?: number | null, name?: string | null, email?: string | null, balance?: number | null, picture?: string | null, extId?: string | null } | null> | null } | null };

export type MakePurchaseMutationVariables = Exact<{
  id?: InputMaybe<Scalars['BigInteger']['input']>;
  amount?: InputMaybe<Scalars['Int']['input']>;
}>;


export type MakePurchaseMutation = { __typename?: 'Mutation', makePurchase?: Array<{ __typename?: 'Purchase', id?: number | null, paidPrice?: number | null } | null> | null };

export type MyAccountQueryVariables = Exact<{ [key: string]: never; }>;


export type MyAccountQuery = { __typename?: 'Query', myAccount?: { __typename?: 'MyAccount', isAdmin?: boolean | null, account?: { __typename?: 'Account', balance?: number | null, email?: string | null, extId?: string | null, hasPin?: boolean | null, id?: number | null, name?: string | null, picture?: string | null } | null } | null };

export type MyPurchasesQueryVariables = Exact<{ [key: string]: never; }>;


export type MyPurchasesQuery = { __typename?: 'Query', myPurchases?: { __typename?: 'PaginationResponse_Purchase', data?: Array<{ __typename?: 'Purchase', id?: number | null, createdAt?: string | null, paidPrice?: number | null, product?: { __typename?: 'Product', name?: string | null, picture?: string | null } | null } | null> | null } | null };

export type PinLoginQueryVariables = Exact<{
  login?: InputMaybe<PinLoginInput>;
}>;


export type PinLoginQuery = { __typename?: 'Query', pinLogin?: string | null };

export type ProductByIdWithFavoriteQueryVariables = Exact<{
  id?: InputMaybe<Scalars['BigInteger']['input']>;
}>;


export type ProductByIdWithFavoriteQuery = { __typename?: 'Query', productWithFavorite?: { __typename?: 'ProductWithFavorite', id?: number | null, name?: string | null, price?: number | null, type?: ProductType | null, picture?: string | null, isFavorite?: boolean | null } | null };

export type ProductsQueryVariables = Exact<{ [key: string]: never; }>;


export type ProductsQuery = { __typename?: 'Query', products?: { __typename?: 'PaginationResponse_Product', data?: Array<{ __typename?: 'Product', id?: number | null, name?: string | null, price?: number | null, type?: ProductType | null, picture?: string | null } | null> | null } | null };

export type ProductsWithFavoritesQueryVariables = Exact<{
  type?: InputMaybe<ProductType>;
}>;


export type ProductsWithFavoritesQuery = { __typename?: 'Query', productsWithFavorites?: { __typename?: 'PaginationResponse_ProductWithFavorite', data?: Array<{ __typename?: 'ProductWithFavorite', id?: number | null, name?: string | null, price?: number | null, type?: ProductType | null, picture?: string | null, isFavorite?: boolean | null } | null> | null } | null };

export type RefundMutationVariables = Exact<{
  id?: InputMaybe<Scalars['BigInteger']['input']>;
}>;


export type RefundMutation = { __typename?: 'Mutation', refundPurchase?: boolean | null };

export type SetPinMutationVariables = Exact<{
  pin?: InputMaybe<Scalars['String']['input']>;
}>;


export type SetPinMutation = { __typename?: 'Mutation', pin?: boolean | null };

export type SignUpMutationVariables = Exact<{
  pin?: InputMaybe<Scalars['String']['input']>;
}>;


export type SignUpMutation = { __typename?: 'Mutation', signUp?: { __typename?: 'Account', id?: number | null } | null };

export type ToggleFavoriteMutationVariables = Exact<{
  id?: InputMaybe<Scalars['BigInteger']['input']>;
}>;


export type ToggleFavoriteMutation = { __typename?: 'Mutation', toggleFavorite?: boolean | null };


export const AccountByIdDocument = gql`
    query AccountById($id: BigInteger) {
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
export const AccountsDocument = gql`
    query Accounts {
  accounts(sort: {columns: [{direction: Ascending, name: "name"}]}) {
    data {
      id
      name
      email
      balance
      picture
      extId
    }
    page
    total
  }
}
    `;
export const AdminAccountsDocument = gql`
    query AdminAccounts($sort: SortPojoInput!, $searchByName: String) {
  accounts(sort: $sort, searchByName: $searchByName) {
    data {
      id
      name
      email
      balance
      picture
      extId
    }
    page
    total
  }
}
    `;
export const MakePurchaseDocument = gql`
    mutation MakePurchase($id: BigInteger, $amount: Int) {
  makePurchase(productId: $id, amount: $amount) {
    id
    paidPrice
  }
}
    `;
export const MyAccountDocument = gql`
    query MyAccount {
  myAccount {
    account {
      balance
      email
      extId
      hasPin
      id
      name
      picture
    }
    isAdmin
  }
}
    `;
export const MyPurchasesDocument = gql`
    query MyPurchases {
  myPurchases(sort: {columns: [{direction: Descending, name: "createdAt"}]}) {
    data {
      id
      createdAt
      paidPrice
      product {
        name
        picture
      }
    }
  }
}
    `;
export const PinLoginDocument = gql`
    query PinLogin($login: PinLoginInput) {
  pinLogin(login: $login)
}
    `;
export const ProductByIdWithFavoriteDocument = gql`
    query ProductByIdWithFavorite($id: BigInteger) {
  productWithFavorite(id: $id) {
    id
    name
    price
    type
    picture
    isFavorite
  }
}
    `;
export const ProductsDocument = gql`
    query Products {
  products {
    data {
      id
      name
      price
      type
      picture
    }
  }
}
    `;
export const ProductsWithFavoritesDocument = gql`
    query ProductsWithFavorites($type: ProductType) {
  productsWithFavorites(
    type: $type
    sort: {columns: [{direction: Ascending, name: "name"}]}
  ) {
    data {
      id
      name
      price
      type
      picture
      isFavorite
    }
  }
}
    `;
export const RefundDocument = gql`
    mutation Refund($id: BigInteger) {
  refundPurchase(id: $id)
}
    `;
export const SetPinDocument = gql`
    mutation SetPin($pin: String) {
  pin(pin: $pin)
}
    `;
export const SignUpDocument = gql`
    mutation SignUp($pin: String) {
  signUp(pin: $pin) {
    id
  }
}
    `;
export const ToggleFavoriteDocument = gql`
    mutation ToggleFavorite($id: BigInteger) {
  toggleFavorite(productId: $id)
}
    `;