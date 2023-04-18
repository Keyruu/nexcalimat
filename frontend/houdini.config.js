/// <references types="houdini-svelte">

/** @type {import('houdini').ConfigFile} */
const config = {
    "watchSchema": {
        "url": "http://localhost:8080/nexcalimat/graphql"
    },
    "plugins": {
        "houdini-svelte": {}
    },
    "scalars": {
        /* in your case, something like */
        BigInteger: {                  // <- The GraphQL Scalar
            type: "number"  // <-  The TypeScript type
        }
    }
}

export default config
