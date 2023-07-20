import axios from 'axios'
import {useAppStore} from '@/store/app'

const redirect_uri = import.meta.env.VITE_REDIRECT_URI
const authorization_server = import.meta.env.VITE_AUTHORIZATION_SERVER

const scope = process.env.scope
const client_id = process.env.client_id
const client_secret = process.env.client_secret
const require_proof_key = process.env.require_proof_key
const basic_auth = {
  username: client_id,
  password: client_secret
}

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
  }).then((codeChallenge) => {
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
  return Promise.all([
    axios.post(revoke_uri, formData1, {auth: basic_auth}),
    axios.post(revoke_uri, formData2, {auth: basic_auth})
  ]).finally(() => {
    clearTokenInfo()
    login()
  })
}

export function obtainAccessToken(code) {
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
    axios.post(token_uri, formData, {auth: basic_auth})
      .then(response => {
        storeTokenInfo(response.data)
        resolve()
      })
      .catch(error => {
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
    axios.post(token_uri, formData, {auth: basic_auth})
      .then(response => {
        storeTokenInfo(response.data)
        resolve()
      })
      .catch(error => reject(error))
  })
}

export function obtainIntrospect(tokenType = 'access_token') {
  return new Promise((resolve, reject) => {
    const token = window.sessionStorage.getItem(tokenType)
    const formData = new FormData()
    formData.append('token', token)
    axios.post(introspect_uri, formData, {auth: basic_auth})
      .then(response => {
        if (response.data.active === false && tokenType === 'access_token') {
          const appStore = useAppStore()
          appStore.alertMessage('未授权！两秒后跳转到登录页面。')
          wait(2000).then(login)
        } else {
          resolve(response.data)
        }
      }).catch(error => reject(error))
  })
}

export function obtainUserinfo() {
  return new Promise((resolve, reject) => {
    axios.post(userinfo_uri)
      .then(response => {
        resolve(response.data)
      }).catch(error => reject(error))
  })
}

export function initAxios() {
  const access_token = window.sessionStorage.getItem('access_token')
  axios.defaults.headers.common['Authorization'] = access_token ? 'Bearer ' + access_token : undefined
  axios.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    return response
  }, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    if (error.response) {
      // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
      const appStore = useAppStore()
      const json = error.response.data
      if (error.response.status === 401) {
        appStore.alertMessage('未授权！两秒后跳转到登录页面。')
        wait(2000).then(login)
      } else {
        appStore.alertMessage(json.message || json.error || error.message)
      }
    } else if (error.request) {
      // 请求已经成功发起，但没有收到响应
      // `error.request` 在浏览器中是 XMLHttpRequest 的实例，
      // 而在node.js中是 http.ClientRequest 的实例
      console.log(error.request)
    } else {
      // 发送请求时出了点问题
      console.log('Error', error.message)
    }
    return Promise.reject(error)
  })
}

function storeTokenInfo(data) {
  // {token_type, access_token, id_token, scope, expires_in, refresh_token}
  window.sessionStorage.setItem('access_token', data.access_token)
  window.sessionStorage.setItem('id_token', data.id_token)
  if (data.refresh_token) {
    window.sessionStorage.setItem('refresh_token', data.refresh_token)
    wait(data.expires_in *  900).then(refreshAccessToken)
  }
  axios.defaults.headers.common['Authorization'] = data.token_type + ' ' + data.access_token
}

function clearTokenInfo() {
  window.sessionStorage.removeItem('access_token')
  window.sessionStorage.removeItem('id_token')
  window.sessionStorage.removeItem('refresh_token')
  window.sessionStorage.removeItem('code_verifier')
  axios.defaults.headers.common['Authorization'] = undefined
}

async function generateCodeChallenge(codeVerifier) {
  const digest = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(codeVerifier))
  return btoa(String.fromCharCode(...new Uint8Array(digest)))
    .replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_')
}

function generateRandomString(length) {
  let text = ''
  let possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  for (let i = 0; i < length; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length))
  }
  return text
}
