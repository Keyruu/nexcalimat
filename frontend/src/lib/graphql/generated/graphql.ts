export type Maybe<T> = T;
export type InputMaybe<T> = T;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
  BigDecimal: any;
  BigInteger: any;
  DateTime: any;
};

export type Account = HasPicture & {
  __typename?: 'Account';
  balance?: Maybe<Scalars['BigInteger']>;
  /** ISO-8601 */
  createdAt?: Maybe<Scalars['DateTime']>;
  /** ISO-8601 */
  deletedAt?: Maybe<Scalars['DateTime']>;
  email?: Maybe<Scalars['String']>;
  extId?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['BigInteger']>;
  name?: Maybe<Scalars['String']>;
  picture?: Maybe<Scalars['String']>;
};

export type AccountInput = {
  balance?: InputMaybe<Scalars['BigInteger']>;
  /** ISO-8601 */
  createdAt?: InputMaybe<Scalars['DateTime']>;
  /** ISO-8601 */
  deletedAt?: InputMaybe<Scalars['DateTime']>;
  email?: InputMaybe<Scalars['String']>;
  extId?: InputMaybe<Scalars['String']>;
  id?: InputMaybe<Scalars['BigInteger']>;
  name?: InputMaybe<Scalars['String']>;
  picture?: InputMaybe<Scalars['String']>;
};

export type ColumnPojoInput = {
  direction?: InputMaybe<DirectionPojo>;
  name?: InputMaybe<Scalars['String']>;
};

export enum DirectionPojo {
  Ascending = 'Ascending',
  Descending = 'Descending'
}

export type HasPicture = {
  id?: Maybe<Scalars['BigInteger']>;
  picture?: Maybe<Scalars['String']>;
};

/** Mutation root */
export type Mutation = {
  __typename?: 'Mutation';
  /** Create Product */
  createProduct?: Maybe<Product>;
  /** Delete Account */
  deleteAccount?: Maybe<Scalars['Boolean']>;
  /** Delete Product */
  deleteProduct?: Maybe<Scalars['Boolean']>;
  /** Erase an Account permanently */
  eraseAccount?: Maybe<Scalars['Boolean']>;
  /** Make Purchase */
  makePurchase?: Maybe<Purchase>;
  /** Set new PIN */
  pin?: Maybe<Scalars['Boolean']>;
  /** Refund Purchase */
  refundPurchase?: Maybe<Scalars['Boolean']>;
  /** Sign up with OIDC provider token and PIN */
  signUp?: Maybe<Account>;
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
  id?: InputMaybe<Scalars['BigInteger']>;
};


/** Mutation root */
export type MutationDeleteProductArgs = {
  id?: InputMaybe<Scalars['BigInteger']>;
};


/** Mutation root */
export type MutationEraseAccountArgs = {
  id?: InputMaybe<Scalars['BigInteger']>;
};


/** Mutation root */
export type MutationMakePurchaseArgs = {
  productId?: InputMaybe<Scalars['BigInteger']>;
};


/** Mutation root */
export type MutationPinArgs = {
  pin?: InputMaybe<Scalars['String']>;
};


/** Mutation root */
export type MutationRefundPurchaseArgs = {
  id?: InputMaybe<Scalars['BigInteger']>;
};


/** Mutation root */
export type MutationSignUpArgs = {
  pin?: InputMaybe<Scalars['String']>;
};


/** Mutation root */
export type MutationUpdateAccountArgs = {
  account?: InputMaybe<AccountInput>;
};


/** Mutation root */
export type MutationUpdateProductArgs = {
  product?: InputMaybe<ProductInput>;
};

export type PagePojoInput = {
  index?: InputMaybe<Scalars['Int']>;
  size?: InputMaybe<Scalars['Int']>;
};

export type PaginationResponse_Account = {
  __typename?: 'PaginationResponse_Account';
  data?: Maybe<Array<Maybe<Account>>>;
  page: Scalars['Int'];
  total: Scalars['BigInteger'];
};

export type PaginationResponse_Product = {
  __typename?: 'PaginationResponse_Product';
  data?: Maybe<Array<Maybe<Product>>>;
  page: Scalars['Int'];
  total: Scalars['BigInteger'];
};

export type PaginationResponse_Purchase = {
  __typename?: 'PaginationResponse_Purchase';
  data?: Maybe<Array<Maybe<Purchase>>>;
  page: Scalars['Int'];
  total: Scalars['BigInteger'];
};

export type PinLoginInput = {
  id?: InputMaybe<Scalars['BigInteger']>;
  pin?: InputMaybe<Scalars['String']>;
};

export type Product = HasPicture & {
  __typename?: 'Product';
  bundleSize?: Maybe<Scalars['Int']>;
  /** ISO-8601 */
  createdAt?: Maybe<Scalars['DateTime']>;
  /** ISO-8601 */
  deletedAt?: Maybe<Scalars['DateTime']>;
  id?: Maybe<Scalars['BigInteger']>;
  name?: Maybe<Scalars['String']>;
  picture?: Maybe<Scalars['String']>;
  price?: Maybe<Scalars['Int']>;
  type?: Maybe<ProductType>;
};

export type ProductInput = {
  bundleSize?: InputMaybe<Scalars['Int']>;
  /** ISO-8601 */
  createdAt?: InputMaybe<Scalars['DateTime']>;
  /** ISO-8601 */
  deletedAt?: InputMaybe<Scalars['DateTime']>;
  id?: InputMaybe<Scalars['BigInteger']>;
  name?: InputMaybe<Scalars['String']>;
  picture?: InputMaybe<Scalars['String']>;
  price?: InputMaybe<Scalars['Int']>;
  type?: InputMaybe<ProductType>;
};

export enum ProductType {
  ColdDrink = 'COLD_DRINK',
  HotDrink = 'HOT_DRINK'
}

export type Purchase = {
  __typename?: 'Purchase';
  account?: Maybe<Account>;
  /** ISO-8601 */
  createdAt?: Maybe<Scalars['DateTime']>;
  /** ISO-8601 */
  deletedAt?: Maybe<Scalars['DateTime']>;
  id?: Maybe<Scalars['BigInteger']>;
  paidPrice?: Maybe<Scalars['Int']>;
  product?: Maybe<Product>;
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
  /** Get personal Purchases */
  myPurchases?: Maybe<PaginationResponse_Purchase>;
  /** Login with PIN */
  pinLogin?: Maybe<Scalars['String']>;
  /** Get Product by ID */
  product?: Maybe<Product>;
  /** Get all Products */
  products?: Maybe<PaginationResponse_Product>;
  /** Get Purchase by ID */
  purchase?: Maybe<Purchase>;
  /** Get all Purchases */
  purchases?: Maybe<PaginationResponse_Purchase>;
};


/** Query root */
export type QueryAccountArgs = {
  id?: InputMaybe<Scalars['BigInteger']>;
};


/** Query root */
export type QueryAccountsArgs = {
  page?: InputMaybe<PagePojoInput>;
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
  id?: InputMaybe<Scalars['BigInteger']>;
};


/** Query root */
export type QueryProductsArgs = {
  page?: InputMaybe<PagePojoInput>;
  sort?: InputMaybe<SortPojoInput>;
};


/** Query root */
export type QueryPurchaseArgs = {
  id?: InputMaybe<Scalars['BigInteger']>;
};


/** Query root */
export type QueryPurchasesArgs = {
  page?: InputMaybe<PagePojoInput>;
  sort?: InputMaybe<SortPojoInput>;
};

export type SortPojoInput = {
  columns?: InputMaybe<Array<InputMaybe<ColumnPojoInput>>>;
};

export type AccountByIdQueryVariables = Exact<{
  id?: InputMaybe<Scalars['BigInteger']>;
}>;


export type AccountByIdQuery = { __typename?: 'Query', account?: { __typename?: 'Account', id?: any, name?: string, email?: string, balance?: any, picture?: string, extId?: string } };

export type AccountsQueryVariables = Exact<{ [key: string]: never; }>;


export type AccountsQuery = { __typename?: 'Query', accounts?: { __typename?: 'PaginationResponse_Account', page: number, total: any, data?: Array<{ __typename?: 'Account', id?: any, name?: string, email?: string, balance?: any, picture?: string, extId?: string }> } };

export type PinLoginQueryVariables = Exact<{
  login?: InputMaybe<PinLoginInput>;
}>;


export type PinLoginQuery = { __typename?: 'Query', pinLogin?: string };
