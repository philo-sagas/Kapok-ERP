import axios from 'axios'
import {useAppStore} from '@/store/app'

const redirect_uri = process.env.redirect_uri
const authorize_uri = process.env.authorize_uri
const token_uri = process.env.token_uri

export function login() {
  let codeVerifier = generateRandomString(64);
  Promise.resolve()
    .then(() => {
      return generateCodeChallenge(codeVerifier)
    })
    .then(function (codeChallenge) {
      window.sessionStorage.setItem('code_verifier', codeVerifier)
      let args = new URLSearchParams({
        response_type: "code",
        client_id: 'public-client',
        state: '1234zyx',
        redirect_uri: redirect_uri,
        code_challenge: codeChallenge,
        code_challenge_method: 'S256',
        scope: 'openid profile'
      });
      window.location = authorize_uri + '?' + args;
    });
}

export function obtainAccessToken(code) {
  return new Promise((resolve, reject) => {
    let formData = new FormData()
    formData.append('client_id', 'public-client')
    formData.append('grant_type', 'authorization_code')
    formData.append('redirect_uri', redirect_uri)
    formData.append('code', code)
    formData.append('code_verifier', window.sessionStorage.getItem('code_verifier'))
    axios.post(token_uri, formData,
      {
        headers: {
          'Authorization': null,
        }
      })
      .then(resp => {
        const authorization = resp.data.token_type + ' ' + resp.data.access_token
        axios.defaults.headers.common['Authorization'] = authorization
        window.sessionStorage.setItem('authorization', authorization)
        resolve()
      }).catch((err) => reject(err))
  })
}

export function initAxios() {
  axios.defaults.headers.common['Authorization'] = window.sessionStorage.getItem('authorization')
  axios.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    return response;
  }, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    if (error.response) {
      // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
      if (error.response.status === 401) {
        login()
        return
      }
      const appStore = useAppStore()
      const json = error.response.data
      appStore.setMessage(json.message || json.error || error.message)
    } else if (error.request) {
      // 请求已经成功发起，但没有收到响应
      // `error.request` 在浏览器中是 XMLHttpRequest 的实例，
      // 而在node.js中是 http.ClientRequest 的实例
      console.log(error.request);
    } else {
      // 发送请求时出了点问题
      console.log('Error', error.message);
    }
    return Promise.reject(error);
  });
}

async function generateCodeChallenge(codeVerifier) {
  let digest = await crypto.subtle.digest("SHA-256", new TextEncoder().encode(codeVerifier));
  return btoa(String.fromCharCode(...new Uint8Array(digest)))
    .replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_')
}

function generateRandomString(length) {
  let text = "";
  let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  for (let i = 0; i < length; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length));
  }
  return text;
}
