const redirect_uri = process.env.NEXT_PUBLIC_REDIRECT_URI
const authorization_server = process.env.NEXT_PUBLIC_AUTHORIZATION_SERVER

const client_id = process.env.NEXT_PUBLIC_OAUTH2_CLIENT_ID
const client_secret = process.env.NEXT_PUBLIC_OAUTH2_CLIENT_SECRET
const scope = process.env.NEXT_PUBLIC_OAUTH2_SCOPE
const require_proof_key = process.env.NEXT_PUBLIC_OAUTH2_REQUIRE_PROOF_KEY == 'true'

const authorize_uri = authorization_server + '/oauth2/authorize'
const token_uri = authorization_server + '/oauth2/token'
const introspect_uri = authorization_server + '/oauth2/introspect'
const userinfo_uri = authorization_server + '/userinfo'
const revoke_uri = authorization_server + '/oauth2/revoke'
const logout_uri = authorization_server + '/logout'
const oidc_logout_uri = authorization_server + '/connect/logout'

const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms))


export function login() {
  new Promise((resolve) => {
    if (require_proof_key) {
      const codeVerifier = generateRandomString(64)
      window.sessionStorage.setItem('code_verifier', codeVerifier)
      const codeChallenge = generateCodeChallenge(codeVerifier)
      resolve(codeChallenge)
    } else {
      resolve(require_proof_key)
    }
  }).then((codeChallenge: string) => {
    const args = new URLSearchParams({
      scope: scope,
      client_id: client_id,
      response_type: "code",
      redirect_uri: redirect_uri,
      state: window.location.pathname
    })
    if (codeChallenge) {
      args.set('code_challenge', codeChallenge)
      args.set('code_challenge_method', 'S256')
    }
    window.location = authorize_uri + '?' + args
  })
}

export function logout() {
  const access_token = window.sessionStorage.getItem('access_token')
  const id_token = window.sessionStorage.getItem('id_token')
  const formData1 = new FormData()
  formData1.append('token', access_token)
  const formData2 = new FormData()
  formData2.append('token', id_token)
  const headers = {
    Authorization: 'Basic ' + btoa(client_id + ":" + client_secret)
  }
  return Promise.all([
    fetch(revoke_uri, {
      method: 'POST',
      headers: headers,
      body: formData1
    }),
    fetch(revoke_uri, {
      method: 'POST',
      headers: headers,
      body: formData2
    })
  ]).finally(() => {
    clearTokenInfo()
    login()
  })
}

export function obtainAccessToken(code, state) {
  return new Promise((resolve) => {
    const formData = new FormData()
    formData.append('code', code)
    formData.append('client_id', client_id)
    formData.append('redirect_uri', redirect_uri)
    formData.append('grant_type', 'authorization_code')
    if (require_proof_key) {
      const code_verifier = window.sessionStorage.getItem('code_verifier')
      formData.append('code_verifier', code_verifier)
    }
    fetch(token_uri, {
      method: 'POST',
      headers: {
        Authorization: 'Basic ' + btoa(client_id + ":" + client_secret)
      },
      body: formData
    })
      .then((response) => response.json())
      .then((data) => {
        storeTokenInfo(data)
        resolve()
      })
      .catch((error) => {
        if (error && error.response && error.response.data
          && error.response.data.error === 'invalid_grant') {
          login()
        } else {
          console.info(error)
        }
      })
  })
}

export function refreshAccessToken() {
  const refresh_token = window.sessionStorage.getItem('refresh_token')
  return new Promise((resolve, reject) => {
    const formData = new FormData()
    formData.append('refresh_token', refresh_token)
    formData.append('grant_type', 'refresh_token')
    fetch(token_uri, {
      method: 'POST',
      headers: {
        Authorization: 'Basic ' + btoa(client_id + ":" + client_secret)
      },
      body: formData
    })
      .then((response) => response.json())
      .then((data) => {
        storeTokenInfo(data)
        resolve()
      })
      .catch((error) => reject(error))
  })
}

export function obtainIntrospect(tokenType: string = 'access_token') {
  return new Promise((resolve, reject) => {
    const token = window.sessionStorage.getItem(tokenType)
    const formData = new FormData()
    formData.append('token', token)
    fetch(introspect_uri, {
      method: 'POST',
      headers: {
        Authorization: 'Basic ' + btoa(client_id + ":" + client_secret)
      },
      body: formData
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.active === false && tokenType === 'access_token') {
          wait(100).then(login)
        } else {
          resolve(data)
        }
      })
      .catch((error) => reject(error))
  })
}

export function obtainUserinfo() {
  return new Promise((resolve, reject) => {
    const access_token = window.sessionStorage.getItem('access_token')
    fetch(userinfo_uri, {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + access_token
      }
    })
      .then((response) => response.json())
      .then((data) => {
        resolve(data)
      })
      .catch((error) => reject(error))
  })
}

export async function defaultFetchToJson(response: Response) {
  if (response.ok) {
    return response.json()
  } else if (response.status === 401) {
    wait(100).then(login)
  } else if (response.status === 500) {
    const json = await response.json()
    return Promise.reject(json)
  } else {
    return Promise.reject(response)
  }
}

function storeTokenInfo(data: {
  token_type: string,
  access_token: string,
  id_token: string,
  scope: string,
  expires_in: number,
  refresh_token: string
}): void {
  window.sessionStorage.setItem('access_token', data.access_token)
  window.sessionStorage.setItem('id_token', data.id_token)
  if (data.refresh_token) {
    window.sessionStorage.setItem('refresh_token', data.refresh_token)
    wait(data.expires_in * 900).then(refreshAccessToken)
  }
}

function clearTokenInfo() {
  window.sessionStorage.removeItem('access_token')
  window.sessionStorage.removeItem('id_token')
  window.sessionStorage.removeItem('refresh_token')
  window.sessionStorage.removeItem('code_verifier')
}

async function generateCodeChallenge(codeVerifier: string): Promise<string> {
  const digest = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(codeVerifier))
  return btoa(String.fromCharCode(...new Uint8Array(digest)))
    .replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_')
}

function generateRandomString(length: number): string {
  let text = ''
  const possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  for (let i = 0; i < length; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length))
  }
  return text
}
